package com.tray.workflow.app

import java.util.UUID.randomUUID

import com.tray.workflow.domain.{WorkflowExecutionGetRequest, WorkflowGetRequest}
import com.tray.workflow.model.{Workflow, WorkflowExecution}
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

    post("/workflows/:workflow_id/executions") { request: WorkflowGetRequest =>
        // TODO move ID generation lower down?
        store.getById(request.workflow_id) match {
            case Some(w) =>
                val execution = store.getById(request.workflow_id).add(randomUUID().toString)
                response
                    .created
                    .json(s"""{
                        "workflow_execution_id": "${execution.id}"
                    }""")
            case None => response
                .notFound
                .jsonError(s"No Workflow found with id ${request.workflow_id}")
        }
    }

    get("/workflows/:workflow_id/executions/:workflow_execution_id") { request: WorkflowExecutionGetRequest =>
        store.getById(request.workflow_id) match {
            case Some(w) =>
                w.getById(request.workflow_execution_id) match {
                    case Some(e) =>
                        response
                            .ok
                            .json(
                            s"""{
                                "finished": "${e.finished()}"
                            }""")
                    case None =>
                        response
                            .notFound
                            .jsonError(
                                s"""No Workflow Execution found with id ${request.workflow_execution_id}
                                on Workflow ${request.workflow_id}""")
                }
            case None => response
                .notFound
                .jsonError(s"No Workflow found with id ${request.workflow_id}")
        }
    }

    put("/workflows/:workflow_id/executions/:workflow_execution_id") { request: WorkflowExecutionGetRequest =>
        store.getById(request.workflow_id) match {
            case Some(w) =>
                w.getById(request.workflow_execution_id) match {
                    case Some(e) =>
                        if (e.increment()) {
                            response.badRequest
                        } else {
                            response.noContent
                        }
                    case None =>
                        response
                            .notFound
                            .jsonError(s"""No Workflow Execution found with id ${request.workflow_execution_id}
                                on Workflow ${request.workflow_id}""")
                }
            case None => response
                .notFound
                .jsonError(s"No Workflow found with id ${request.workflow_id}")
        }
    }
}
