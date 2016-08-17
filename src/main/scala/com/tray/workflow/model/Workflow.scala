package com.tray.workflow.model

case class Workflow(id: String, stepCount: Int,
                    private var executions: Map[String, WorkflowExecution] = Map[String, WorkflowExecution]()) {

    def getById(id: String): WorkflowExecution = {
        executions.getOrElse(id, null)
    }

    def add(execution_id: String): WorkflowExecution = {
        val execution = WorkflowExecution(execution_id, stepCount)
        executions = executions + (execution.id -> execution)
        execution
    }

    def getExecutions(): Iterable[WorkflowExecution] = {
        executions.values
    }
}
