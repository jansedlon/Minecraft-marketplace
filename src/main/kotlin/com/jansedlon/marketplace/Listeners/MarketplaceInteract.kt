package com.jansedlon.marketplace.Listeners

import com.jansedlon.marketplace.Marketplace
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.metadata.MetadataValue
import org.bukkit.persistence.PersistentDataType

class MarketplaceInteract : Listener {
    @EventHandler
    fun onClick(e: InventoryClickEvent) {
        val player = e.whoClicked as Player

        if (e.currentItem == null) return

        if (e.view.title == "Marketplace") {
            when(e.currentItem!!.itemMeta?.displayName) {
                "${ChatColor.YELLOW}Search by name" -> {
                    e.isCancelled = true

                    Marketplace.conversationFactory.buildConversation(player).begin()

                    player.closeInventory()

                    return
                }
                "${ChatColor.YELLOW}Search by ID" -> {
                    e.isCancelled = true
                    return
                }
                "${ChatColor.YELLOW}Search by lore" -> {
                    e.isCancelled = true
                    return
                }
                "${ChatColor.YELLOW}Search all items" -> {
                    e.isCancelled = true

                    Marketplace.marketplaceManager.showSearchForAllItems(player)
                    return
                }
            }
        } else if (e.view.title.contains("Results - Page")) {
            val itemMarketplaceID = e.currentItem!!.itemMeta?.persistentDataContainer?.get(NamespacedKey(Marketplace.main, "MarketplaceItemID"), PersistentDataType.STRING)

            // Clicked on item that can be bought
            if (itemMarketplaceID != null) {
                e.isCancelled = true
                Marketplace.marketplaceManager.buyItem(itemMarketplaceID, player)
                return
            }

            // Clicked on next or prev page item
            e.isCancelled = true
            val page = e.view.getItem(0)!!.itemMeta!!.localizedName.toInt()
            val inv = Marketplace.marketplaceManager.openedResultMenu[player.uniqueId]!!

            if (e.rawSlot == 0 && e.currentItem?.type == Material.LIME_STAINED_GLASS_PANE) {
                inv.goToPage(page - 1)
                player.closeInventory()
                inv.showForPlayer(player)
            } else if (e.rawSlot == 8 && e.currentItem?.type == Material.LIME_STAINED_GLASS_PANE) {
                inv.goToPage(page + 1)
                player.closeInventory()
                inv.showForPlayer(player)
            }
        }
    }

    @EventHandler
    fun onClose(e: InventoryCloseEvent) {
        if (!e.view.title.contains("Results - Page")) return

        Marketplace.marketplaceManager.onCloseResultsInventoryForPlayer(e.player as Player)
    }
}
