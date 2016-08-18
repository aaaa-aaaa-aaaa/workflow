package com.tray.workflow.model

import java.time.LocalDateTime
import java.util.concurrent.atomic.AtomicInteger

case class WorkflowExecution(id: String, maxSteps: Int) {

    private val currentStep: AtomicInteger = new AtomicInteger()
    val created: LocalDateTime = LocalDateTime.now()

    def finished(): Boolean = synchronized(currentStep.get() >= maxSteps - 1)

    def increment() = currentStep.getAndIncrement()
}
