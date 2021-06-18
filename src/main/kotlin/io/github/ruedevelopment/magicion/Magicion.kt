package io.github.ruedevelopment.magicion

import io.github.ruedevelopment.magicion.utils.LoggerDelegate
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.minecraft.item.ItemGroup
import net.minecraft.item.Items
import net.minecraft.util.Identifier

object Magicion : ModInitializer {
    const val MOD_ID = "magicion"
    val ITEM_GROUP: ItemGroup = FabricItemGroupBuilder.create(id("all"))
        .icon { Items.DIRT.defaultStack } // TODO: replace
        .build()
    private val LOGGER by LoggerDelegate()

    // small util
    fun id(path: String) = Identifier(MOD_ID, path)

    override fun onInitialize() {
        LOGGER.info("test")
    }
}