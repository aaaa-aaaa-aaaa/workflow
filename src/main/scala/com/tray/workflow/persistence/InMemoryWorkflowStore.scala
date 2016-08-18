package com.tray.workflow.persistence

import com.tray.workflow.model.Workflow

class InMemoryWorkflowStore(private var workflows: Map[String, Workflow] = Map[String, Workflow]()) extends WorkflowStore {

    override def getById(id: String): Option[Workflow] = {
        Option(workflows.getOrElse(id, null))
    }

    override def add(workflow: Workflow): Workflow = {
        workflows = workflows + (workflow.id -> workflow)
        workflow
    }

    override def getAll(): Iterable[Workflow] = {
        workflows.values
    }
}
