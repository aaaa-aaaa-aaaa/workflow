package com.tray.workflow.app
import com.tray.workflow.domain.WorkflowGetRequest
import com.tray.workflow.model.Workflow
import com.tray.workflow.persistence.{InMemoryWorkflowStore, WorkflowStore}
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
class WorkflowController extends Controller {

    private val store: WorkflowStore = new InMemoryWorkflowStore()

    get("/") { request: Request =>
        response.ok
    }

    //TODO support pagination
    get("/workflows") { request: Request =>
        val workflows = store.getAll()
        response
            .ok
            .json(workflows)
    }

    get("/workflows/:workflow_id") { request: WorkflowGetRequest =>
        val workflow = store.getById(request.workflow_id)
        workflow match {
            case w: Workflow => response.ok.json(w)
            case _ => response.notFound.jsonError(s"No Workflow found with id ${request.workflow_id}")
        }
    }
}
