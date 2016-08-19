package com.tray.workflow.domain

import com.twitter.finatra.request.RouteParam

case class WorkflowGetRequest(@RouteParam workflow_id: String) {
}
