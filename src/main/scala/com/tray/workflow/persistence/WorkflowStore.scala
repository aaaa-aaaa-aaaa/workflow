package com.tray.workflow.persistence

import com.tray.workflow.model.Workflow

abstract class WorkflowStore {

    def getById(id: String): Option[Workflow]

    def add(workflow: Workflow): Workflow

    def getAll(): Iterable[Workflow]
}
