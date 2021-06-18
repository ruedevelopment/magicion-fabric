package io.github.ruedevelopment.magicion.client

import io.github.ruedevelopment.magicion.utils.LoggerDelegate
import net.fabricmc.api.ClientModInitializer

object MagicionClient : ClientModInitializer {
    private val LOGGER by LoggerDelegate()

    override fun onInitializeClient() {
        LOGGER.info("test but on the client")
    }
}