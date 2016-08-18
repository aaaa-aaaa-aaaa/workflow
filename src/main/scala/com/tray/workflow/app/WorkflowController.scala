package com.tray.workflow.app

import java.util.UUID.randomUUID

import com.tray.workflow.domain.{WorkflowExecutionGetRequest, WorkflowGetRequest}
import com.tray.workflow.model.{Workflow, WorkflowExecution}
import com.tray.workflow.persistence.{InMemoryWorkflowStore, WorkflowStore}
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finatra.http.Controller
import org.json4s.DefaultFormats
import org.json4s.native.JsonMethods.parse

class WorkflowController extends Controller {

    implicit val formats = DefaultFormats

    // TODO method docs
    // TODO error handling
    // TODO incorrect route handling
    private val store: WorkflowStore = new InMemoryWorkflowStore()

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
            case w: Workflow => response
                .ok
                .json(w)
            case _ => response
                .notFound
                .jsonError(s"No Workflow found with id ${request.workflow_id}")
        }
    }

    get("/workflows/:workflow_id/executions") { request: WorkflowGetRequest =>
        val workflow = store.getById(request.workflow_id)
        workflow match {
            // TODO executions can't be serialized automatically by Jackson
            // either a custom marshaller is needed, or the utility GET methods can be removed
            case w: Workflow => response
                .ok
                .json(w.getExecutions())
            case _ => response
                .notFound
                .jsonError(s"No Workflow found with id ${request.workflow_id}")
        }
    }

    post("/workflows/:workflow_id/executions") { request: WorkflowGetRequest =>
        // TODO move ID generation lower down?
        val workflow = store.getById(request.workflow_id)
        workflow match {
            case w: Workflow =>
                val execution = workflow.add(randomUUID().toString)
                response
                    .created
                    .json(s"""{
                        "workflow_execution_id": "${execution.id}"
                    }""")
            case _ => response
                .notFound
                .jsonError(s"No Workflow found with id ${request.workflow_id}")
        }
    }

    get("/workflows/:workflow_id/executions/:workflow_execution_id") { request: WorkflowExecutionGetRequest =>
        val workflow = store.getById(request.workflow_id)
        workflow match {
            case w: Workflow =>
                val execution = w.getById(request.workflow_execution_id)
                execution match {
                    case e: WorkflowExecution =>
                        response
                            .ok
                            .json(
                            s"""{
                                "finished": "${e.finished()}"
                            }""")
                    case _ =>
                        response
                            .notFound
                            .jsonError(
                                s"""No Workflow Execution found with id ${request.workflow_execution_id}
                                on Workflow ${request.workflow_id}""")
                }
            case _ => response
                .notFound
                .jsonError(s"No Workflow found with id ${request.workflow_id}")
        }
    }

    put("/workflows/:workflow_id/executions/:workflow_execution_id") { request: WorkflowExecutionGetRequest =>
        val workflow = store.getById(request.workflow_id)
        workflow match {
            case w: Workflow =>
                val execution = w.getById(request.workflow_execution_id)
                execution match {
                    case e: WorkflowExecution =>
                        // TODO 400 if execution has finished
                        response
                            .noContent
                    case _ =>
                        response
                            .notFound
                            .jsonError(
                                s"""No Workflow Execution found with id ${request.workflow_execution_id}
                                on Workflow ${request.workflow_id}""")
                }
            case _ => response
                .notFound
                .jsonError(s"No Workflow found with id ${request.workflow_id}")
        }
    }
}
