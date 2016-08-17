package com.tray.workflow.app
import com.twitter.finatra.http.Controller
class WorkflowController extends Controller {

    get("/") { request: Request =>
        response.ok
    }
}
