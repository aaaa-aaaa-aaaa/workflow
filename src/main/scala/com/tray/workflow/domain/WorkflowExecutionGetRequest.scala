package com.tray.workflow.domain

import com.twitter.finatra.request.RouteParam

case class WorkflowExecutionGetRequest(@RouteParam workflow_id: String, @RouteParam workflow_execution_id: String) {
}
