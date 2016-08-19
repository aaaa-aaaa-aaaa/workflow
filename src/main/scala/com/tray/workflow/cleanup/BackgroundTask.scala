package com.tray.workflow.cleanup

import java.time.LocalDateTime
import javax.inject.Inject

import com.tray.workflow.persistence.WorkflowStore

/**
  * Basic Background task to iterate through all workflow executions
  * and remove those that are finished and older than 1 min.
  */
class BackgroundTask @Inject()(store: WorkflowStore) extends Runnable {
    override def run() = {
        for (wf <- store.getAll()) {
            for (wfe <- wf.getExecutions()) {
                if (wfe.created.isBefore(LocalDateTime.now().minusMinutes(1)) && wfe.finished()) {
                    wf.removeById(wfe.id)
                }
            }
        }

        Thread.sleep(1000)
    }
}
