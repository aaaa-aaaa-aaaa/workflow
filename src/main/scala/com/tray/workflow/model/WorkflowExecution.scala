package com.tray.workflow.model

import java.time.LocalDateTime
import java.util.concurrent.atomic.AtomicInteger

/**
  * Represents an execution of a Workflow
  *
  * @param id identifier for this execution, unique across the associated workflow.
  * @param maxSteps the maximum number of steps for this workflow.
  */
case class WorkflowExecution(id: String, maxSteps: Int) {

    private val currentStep: AtomicInteger = new AtomicInteger()
    val created: LocalDateTime = LocalDateTime.now()

    /**
      * Checks whether the current execution has finished, by comparing
      * the current step to the max permitted steps.
      *
      * @return true if current step >= max steps - 1, false otherwise
      */
    def finished(): Boolean = synchronized(currentStep.get() >= maxSteps - 1)

    /**
      * Increments the current step count, if the current execution has
      * not finished.
      *
      * @return true if increment was successful, false otherwise
      */
    def increment() = synchronized{
        // this is a touch clunky given the atomic primitives
        val fin = finished()
        if (!fin) {
            currentStep.getAndIncrement()
        }
        !fin
    }
}
