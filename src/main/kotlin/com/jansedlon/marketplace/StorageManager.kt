package com.jansedlon.marketplace

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack
import java.io.File

class StorageManager {
    private val storageFile: File = File(Marketplace.main.dataFolder, "storage.yaml")
    private val storage: YamlConfiguration

    init {

        if (!storageFile.exists()) {
            storageFile.createNewFile()
        }

        storage = YamlConfiguration.loadConfiguration(storageFile)
        storage.addDefault("marketplaceItems", ArrayList<MarketplaceItem>())

        Bukkit.getConsoleSender().sendMessage(storage.getList("marketplaceItems").toString())
    }

    fun saveMarketplaceItem(item: MarketplaceItem) {
        storage.createSection("marketplaceItem.${item.id}", item.serialize())

        storage.save(storageFile)
    }

    fun loadMarketplaceItems() {
        val items = storage.getConfigurationSection("marketplaceItems")!!.getValues(false)
        storage.set("marketplaceItems", items)

        for(item in items) {
            Marketplace.marketplaceManager.items.add(MarketplaceItem.deserialize(item as MutableMap<String, Any>))
        }
    }
}