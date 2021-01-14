package com.jansedlon.marketplace

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.scheduler.BukkitRunnable

class MarketplaceChat : Listener {
    @EventHandler
    fun onChat(e: AsyncPlayerChatEvent) {
        val player = e.player

        if (player.hasMetadata("AwaitingSearchByNameInput")) {
            player.removeMetadata("AwaitingSearchByNameInput", Marketplace.main)
            val enteredSearch = e.message
            e.isCancelled = true

            Marketplace.marketplaceManager.showSearchByNameItems(enteredSearch, player)
        }
    }
}
