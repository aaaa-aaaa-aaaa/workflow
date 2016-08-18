package com.tray.workflow.model

import java.time.LocalDateTime
import java.util.concurrent.atomic.AtomicInteger

case class WorkflowExecution(id: String, maxSteps: Int) {

    private val currentStep: AtomicInteger = new AtomicInteger()
    val created: LocalDateTime = LocalDateTime.now()

    def next(): Option[Int] = {
        // TODO this is overall still not atomic
        val steps = currentStep.incrementAndGet()
        if (steps >= maxSteps) {
            None
        }
        Some(steps)
    }
}
