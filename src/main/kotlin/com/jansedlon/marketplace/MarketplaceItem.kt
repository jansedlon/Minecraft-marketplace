package com.jansedlon.marketplace

import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import java.util.UUID
import kotlin.collections.HashMap

class MarketplaceItem(val ownerId: UUID, val item: ItemStack, val price: Double) : ConfigurationSerializable {
    val id = UUID.randomUUID().toString()

    companion object {
        fun deserialize(data: MutableMap<String, Any>): MarketplaceItem {
            Bukkit.getConsoleSender().sendMessage("data: ${data.toString()}")
            Bukkit.getConsoleSender().sendMessage(ItemStack.deserialize(data["item"] as HashMap<String, Any>).toString())
            Bukkit.getConsoleSender().sendMessage(data.get("price") as String)
            return MarketplaceItem(UUID.fromString(data["ownerId"] as String), ItemStack.deserialize(data["item"] as HashMap<String, Any>), (data.get("price") as String).toDouble())
        }
    }

    init {
        // Set a special ID to this item so it can be identified later during InventoryItemClick event.
        val meta = item.itemMeta!!
        meta.persistentDataContainer.set(NamespacedKey(Marketplace.main, "MarketplaceItemID"), PersistentDataType.STRING, id)

        item.itemMeta = meta
    }

    override fun serialize(): MutableMap<String, Any> {
        val data = HashMap<String, Any>()
        data["item"] = item
        data["ownerId"] = id
        data["price"] = price

        return data
    }
}