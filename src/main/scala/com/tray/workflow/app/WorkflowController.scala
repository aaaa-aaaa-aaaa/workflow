package com.tray.workflow.app

import java.util.UUID.randomUUID
import javax.inject.{Inject, Singleton}

import com.tray.workflow.cleanup.BackgroundTask
import com.tray.workflow.domain.{WorkflowExecutionGetRequest, WorkflowGetRequest}
import com.tray.workflow.model.Workflow
import com.tray.workflow.persistence.WorkflowStore
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import org.json4s.DefaultFormats
import org.json4s.ParserUtil.ParseException
import org.json4s.native.JsonMethods.parse

@Singleton
class WorkflowController @Inject()(store: WorkflowStore, task: BackgroundTask) extends Controller {

    implicit val formats = DefaultFormats

    val background = new Thread(task)
    background.start()

    // TODO incorrect route handling

    /**
      * create a new workflow
      */
    post("/workflows") { request: Request =>
        val contentType = Option(request.headerMap.getOrElse("Content-Type", null))
        if (contentType.isEmpty || !contentType.get.contains("application/json")) {
            response.badRequest.jsonError("Content-Type must be application/json")
        }
        val json = parse(request.contentString)
        val steps = (json \ "number_of_steps").extract[Int]

        // TODO move ID generation lower down?
        val workflow = store.add(Workflow(randomUUID().toString, steps))
        response
            .created
            .json(s"""{
            "workflow_id": "${workflow.id}"
        }""")
    }

    /**
      * create a new workflow execution for a specific workflow
      */
    post("/workflows/:workflow_id/executions") { request: WorkflowGetRequest =>
        store.getById(request.workflow_id) match {
            case Some(w) =>
                // TODO move ID generation lower down?
                val execution = w.add(randomUUID().toString)
                response.created
                    .json(s"""{ "workflow_execution_id": "${execution.id}" }""")
            case None => response
                .notFound
                .jsonError(s"No Workflow found with id ${request.workflow_id}")
        }
    }

    /**
      * fetch whether a workflow execution has finished
      */
    get("/workflows/:workflow_id/executions/:workflow_execution_id") { request: WorkflowExecutionGetRequest =>
        store.getById(request.workflow_id) match {
            case Some(w) =>
                w.getById(request.workflow_execution_id) match {
                    case Some(e) => response
                        .ok
                        .json(s"""{ "finished": "${e.finished()}" }""")
                    case None => response
                        .notFound
                        .jsonError(s"""No Workflow Execution found with id ${request.workflow_execution_id}
                            on Workflow ${request.workflow_id}""")
                }
            case None => response
                .notFound
                .jsonError(s"No Workflow found with id ${request.workflow_id}")
        }
    }

    /**
      * increment the current step of a workflow execution
      */
    put("/workflows/:workflow_id/executions/:workflow_execution_id") { request: WorkflowExecutionGetRequest =>
        store.getById(request.workflow_id) match {
            case Some(w) =>
                w.getById(request.workflow_execution_id) match {
                    case Some(e) =>
                        if (e.increment()) {
                            response.noContent
                        } else {
                            response.badRequest
                        }
                    case None => response
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
