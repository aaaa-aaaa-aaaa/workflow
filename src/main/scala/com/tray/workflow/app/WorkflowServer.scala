package com.tray.workflow.app

import com.twitter.finagle.http.{Request, Response}
import com.twitter.finatra.http.HttpServer
import com.twitter.finatra.http.filters.{CommonFilters, LoggingMDCFilter, TraceIdMDCFilter}
import com.twitter.finatra.http.routing.HttpRouter

object WorkflowServerMain extends WorkflowServer

class WorkflowServer extends HttpServer {
    override val defaultFinatraHttpPort: String = ":9000"

    override def configureHttp(router: HttpRouter): Unit = {
        router
            .filter[LoggingMDCFilter[Request, Response]]
            .filter[TraceIdMDCFilter[Request, Response]]
            .filter[CommonFilters]
            .add[WorkflowController]
    }
}
