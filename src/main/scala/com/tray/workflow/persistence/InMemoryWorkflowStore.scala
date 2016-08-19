package com.tray.workflow.persistence

import javax.inject.Singleton

import com.tray.workflow.model.Workflow

@Singleton
class InMemoryWorkflowStore extends WorkflowStore {

    private var workflows: Map[String, Workflow] = Map[String, Workflow]()

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
