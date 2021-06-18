/*
 * Sourced from https://github.com/ruedevelopment/healthmod-fabric/blob/development/1.16.x/src/main/kotlin/io/github/teambluemods/healthmod/util/LoggerDelegate.kt.
 * Original source possibly from https://stackoverflow.com/questions/34416869/idiomatic-way-of-logging-in-kotlin.
 * I don't have the original link from where this came from, but this code is certainly modified.
 */

package io.github.ruedevelopment.magicion.utils

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class LoggerDelegate<in T : Any> : ReadOnlyProperty<T, Logger> {
    private lateinit var logger: Logger

    override fun getValue(thisRef: T, property: KProperty<*>): Logger {
        if (!this::logger.isInitialized) logger = LogManager.getLogger(thisRef.javaClass)

        return logger
    }
}