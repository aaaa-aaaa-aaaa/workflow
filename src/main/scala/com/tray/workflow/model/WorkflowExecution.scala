package com.tray.workflow.model

import java.time.LocalDateTime

// TODO created not timezone insensitive
// TODO no checks on input
// TODO is this even the right way to instantiate params?
case class WorkflowExecution(val id: String, val workflowId: String, val stepCount: Int, val created: LocalDateTime) {
    val id: String = id
    val workflowId: String = workflowId
    val stepCount: Int = id
    val created: LocalDateTime = created
}
