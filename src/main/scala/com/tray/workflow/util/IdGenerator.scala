package com.tray.workflow.util

import java.util.UUID._
import javax.inject.Singleton

/**
  * generates a random string ID
  */
@Singleton
class IdGenerator {

    def generate(): String = {
        randomUUID().toString
    }
}
