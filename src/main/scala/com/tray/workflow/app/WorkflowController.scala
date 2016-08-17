package com.tray.workflow.app
import com.tray.workflow.persistence.{InMemoryWorkflowStore, WorkflowStore}
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
}
