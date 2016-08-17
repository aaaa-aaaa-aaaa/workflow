package com.tray.workflow.app

import java.util.UUID.randomUUID

import com.tray.workflow.domain.WorkflowGetRequest
import com.tray.workflow.model.Workflow
import com.tray.workflow.persistence.{InMemoryWorkflowStore, WorkflowStore}
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import org.json4s.DefaultFormats
import org.json4s.native.JsonMethods.parse

class WorkflowController extends Controller {

    implicit val formats = DefaultFormats

    // TODO method docs
    // TODO error handling
    // TODO incorrect route handling
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

    post("/workflows") { request: Request =>
        // TODO move ID generation lower down?
        val json = parse(request.contentString)
        val steps = (json \ "number_of_steps").extract[Int]

        val workflow = store.add(Workflow(randomUUID().toString, steps))
        response
            .created
            .json(s"""{
                "workflow_id": "${workflow.id}"
            }""")
    }

    get("/workflows/:workflow_id") { request: WorkflowGetRequest =>
        val workflow = store.getById(request.workflow_id)
        workflow match {
            case w: Workflow => response.ok.json(w)
            case _ => response.notFound.jsonError(s"No Workflow found with id ${request.workflow_id}")
        }
    }

    get("/workflows/:workflow_id/executions") { request: WorkflowGetRequest =>
        val workflow = store.getById(request.workflow_id)
        workflow match {
            case w: Workflow => response.ok.json(w.getExecutions())
            case _ => response.notFound.jsonError(s"No Workflow found with id ${request.workflow_id}")
        }
    }

    // TODO should create new execution against workflow, or 404 if no such workflow
    post("/workflows/:workflow_id/executions") { request: Request =>
        response.notImplemented.body("TODO")
    }

    // TODO should return workflow execution status here, or 404 if workflow/execution doesn't exist
    get("/workflows/:workflow_id/executions/:workflow_execution_id") { request: Request =>
        response.notImplemented.body("TODO")
    }
    // TODO should increment workflow execution step count, or 404 if workflow/execution doesn't exist, or 400 if step count > max
    put("/workflows/:workflow_id/executions/:workflow_execution_id") { request: Request =>
        response.notImplemented.body("TODO")
    }
}
