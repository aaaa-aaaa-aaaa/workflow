package com.tray.workflow.persistence

import com.google.inject.ImplementedBy
import com.tray.workflow.model.Workflow

@ImplementedBy(classOf[InMemoryWorkflowStore])
abstract class WorkflowStore {

    def getById(id: String): Option[Workflow]

    def add(workflow: Workflow): Workflow

    def getAll(): Iterable[Workflow]
}
