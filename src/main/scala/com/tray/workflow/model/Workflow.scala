package com.tray.workflow.model

/**
  * A representation of a simple Workflow, specified by a number of steps.
  *
  * @param id the unique ID for the workflow
  * @param stepCount the number of steps in the workflow
  * @param executions a set of executions of this workflow (initially empty)
  */
case class Workflow(id: String, stepCount: Int,
                    private var executions: Map[String, WorkflowExecution] = Map[String, WorkflowExecution]()) {

    /**
      * Fetch an execution by ID
      *
      * @param id the
      * @return the execution if found, null otherwise
      */
    def getById(id: String): Option[WorkflowExecution] = {
        executions.getOrElse(id, null)
    }

    /**
      * Adds a new execution with the specified ID to the current Workflow.
      *
      * @param execution_id the ID of the execution to be added
      * @return the created workflow execution
      */
    def add(execution_id: String): WorkflowExecution = {
        val execution = WorkflowExecution(execution_id, stepCount)
        executions = executions + (execution.id -> execution)
        execution
    }

    /**
      * Fetch all executions for this Workflow.
      *
      * @return an Iterable of all executions
      */
    def getExecutions(): Iterable[WorkflowExecution] = {
        executions.values
    }
}
