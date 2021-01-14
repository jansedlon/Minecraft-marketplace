package com.jansedlon.marketplace

import com.jansedlon.marketplace.Inventories.MainMenu
import com.jansedlon.marketplace.Inventories.ResultMenu
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.UUID
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MarketplaceManager {
    var items = ArrayList<MarketplaceItem>()
    val openedResultMenu = HashMap<UUID, ResultMenu>()

    init {
        for(i in 0..100) {
//            items.add(MarketplaceItem(Bukkit.getOfflinePlayer("JanSedlon"), ItemStack(Material.DIRT), 10.00))
        }
    }

    fun showSearchMenu(player: Player) {
        val menu = MainMenu()
        menu.openForPlayer(player)
    }

    fun showMarketplaceForItems(player: Player, items: List<MarketplaceItem>) {
        val searchResultInventory = ResultMenu(items)
        openedResultMenu[player.uniqueId] = searchResultInventory
        searchResultInventory.showForPlayer(player)
    }

    fun publishItem(player: Player, item: ItemStack, price: Double) {
        val itemInMarketplace = MarketplaceItem(player.uniqueId, item, price)

        items.add(itemInMarketplace)
        Marketplace.storageManager.saveMarketplaceItem(itemInMarketplace)
    }

    fun showSearchByNameItems(searchTerm: String, player: Player) {
        val matchedItems = Utils.searchItemsByName(searchTerm, items)
        showMarketplaceForItems(player, matchedItems)
    }

    fun showSearchForAllItems(player: Player) {
        showMarketplaceForItems(player, items)
    }

    fun buyItem(itemId: String, player: Player) {
        val foundItem = items.find { it.id == itemId }

        if (foundItem == null) {
            player.sendMessage("${ChatColor.RED}I'm sorry, but this item no longer exists :(")
            return
        }

        if (player.inventory.addItem(foundItem.item).isNotEmpty()) {
            player.inventory.removeItem(foundItem.item)
            player.sendMessage("${ChatColor.RED}You don't have enough space in your inventory.")
            return
        }


        if (Marketplace.economy.getBalance(player) < foundItem.price) {
            player.sendMessage("${ChatColor.RED}You don't have sufficient amount of money.")
            return
        }

        val depositTransaction = Marketplace.economy.depositPlayer(Bukkit.getOfflinePlayer(foundItem.ownerId).player!!, foundItem.price)

        if (!depositTransaction.transactionSuccess()) {
            player.sendMessage("${ChatColor.RED}Something bad happened! This transaction could not be completed.")
            return
        }

        val withdrawTransaction = Marketplace.economy.withdrawPlayer(player, foundItem.price)

        if (!withdrawTransaction.transactionSuccess()) {
            player.sendMessage("${ChatColor.RED}Something bad happened! This transaction could not be completed.")
            Marketplace.economy.withdrawPlayer(Bukkit.getOfflinePlayer(foundItem.ownerId).player!!, foundItem.price)
        }

        items.remove(foundItem)

        player.sendMessage("Transaction completed!")
        player.closeInventory()
    }

    fun onCloseResultsInventoryForPlayer(player: Player) {
        openedResultMenu.remove(player.uniqueId)
    }
}