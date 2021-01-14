package com.jansedlon.marketplace.Inventories

import com.jansedlon.marketplace.Marketplace
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class MainMenu {
    private val inventory: Inventory = Bukkit.createInventory(null, 54, "Marketplace")

    init {

        // Search by id time
        val searchByIdItem = ItemStack(Material.APPLE)
        val searchByIdItemMeta = searchByIdItem.itemMeta!!

        searchByIdItemMeta.setDisplayName("${ChatColor.YELLOW}Search by ID ${ChatColor.RED}(Unsupported))")
        searchByIdItem.itemMeta = searchByIdItemMeta

        // Search by name item
        val searchByNameItem = ItemStack(Material.NAME_TAG)
        val searchByNameItemMeta = searchByIdItem.itemMeta!!

        searchByNameItemMeta.setDisplayName("${ChatColor.YELLOW}Search by name")
        searchByNameItem.itemMeta = searchByNameItemMeta

        // Search by lore item
        val searchByLoreItem = ItemStack(Material.BOOK)
        val searchByLoreItemMeta = searchByIdItem.itemMeta!!

        searchByLoreItemMeta.setDisplayName("${ChatColor.YELLOW}Search by lore")
        searchByLoreItem.itemMeta = searchByLoreItemMeta

        // Search all marketplace item
        val searchAllMarketplaceItem = ItemStack(Material.GOLDEN_APPLE)
        val searchAllMarketplaceItemMeta = searchByIdItem.itemMeta!!

        searchAllMarketplaceItemMeta.setDisplayName("${ChatColor.YELLOW}Search all items")
        searchAllMarketplaceItem.itemMeta = searchAllMarketplaceItemMeta

        inventory.setItem(11, searchByNameItem)
        inventory.setItem(13, searchByIdItem)
        inventory.setItem(15, searchByLoreItem)
        inventory.setItem(31, searchByNameItem)
        inventory.setItem(31, searchAllMarketplaceItem)
    }

    fun openForPlayer(player: Player) {
        player.openInventory(inventory)
    }
}