package com.example.imjangmarket.global.utils

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging

interface Loggable {
     val log: KLogger
          get() = KotlinLogging.logger(this.javaClass.name)
}