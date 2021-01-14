package com.jansedlon.marketplace.Inventories

import com.jansedlon.marketplace.MarketplaceItem
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.lang.IndexOutOfBoundsException

class ResultMenu(items: List<MarketplaceItem>) {
    private var page = 1
    private var inventory = Bukkit.createInventory(null, 54, "Results - Page $page")
    private val maxItemsPerPage = 52
    private var prevPageItem: ItemStack
    private var nextPageItem: ItemStack
    private var pagesWithItems = items.chunked(maxItemsPerPage)

    init {
        prevPageItem = buildPrevPageItem()
        nextPageItem = buildNextPageItem()
    }

    private fun buildPrevPageItem(): ItemStack {
        val isPrevPageAvailable = page > 1

        val item = when(isPrevPageAvailable) {
            true -> ItemStack(Material.LIME_STAINED_GLASS_PANE)
            false -> ItemStack(Material.RED_STAINED_GLASS_PANE)
        }

        val itemMeta = item.itemMeta!!
        itemMeta.setLocalizedName(page.toString())

        when(isPrevPageAvailable) {
            true -> itemMeta.setDisplayName("${ChatColor.GREEN}Prev page")
            false -> itemMeta.setDisplayName("${ChatColor.RED}No prev page available")
        }

        item.itemMeta = itemMeta

        return item
    }

    private fun buildNextPageItem(): ItemStack {
        val isNextPageAvailable = page < pagesWithItems.size

        val item = when(isNextPageAvailable) {
            true -> ItemStack(Material.LIME_STAINED_GLASS_PANE)
            false -> ItemStack(Material.RED_STAINED_GLASS_PANE)
        }

        val itemMeta = item.itemMeta!!
        itemMeta.setLocalizedName(page.toString())

        when(isNextPageAvailable) {
            true -> itemMeta.setDisplayName("${ChatColor.GREEN}Next page")
            false -> itemMeta.setDisplayName("${ChatColor.RED}No next page available")
        }

        item.itemMeta = itemMeta

        return item
    }

    private fun rebuildInteractionItems() {
        prevPageItem = buildPrevPageItem()
        nextPageItem = buildNextPageItem()

        inventory.setItem(0, prevPageItem)
        inventory.setItem(8, nextPageItem)
    }

    fun goToPage(page: Int) {
        if (page > pagesWithItems.size) {
            throw IndexOutOfBoundsException("Page is out of bound")
        }

        this.page = page
        inventory = Bukkit.createInventory(null, 54, "Results - Page ${this.page}")
    }

    fun showForPlayer(player: Player) {
        inventory.clear()
        rebuildInteractionItems()

        val itemsInPage = pagesWithItems.getOrElse(page - 1) { ArrayList() }

        for (item in itemsInPage) {
            val clonedItem = item.item.clone()
            val itemWithPriceMeta = clonedItem.itemMeta

            // Get lore or create a new one
            val itemWithPriceLore = itemWithPriceMeta!!.lore ?: ArrayList<String>()

            // Add price
            itemWithPriceLore.add("${ChatColor.GOLD}${item.price}$")

            // Replace modified values
            itemWithPriceMeta.lore = itemWithPriceLore
            clonedItem.itemMeta = itemWithPriceMeta
            inventory.addItem(clonedItem)
        }
        player.openInventory(inventory)
    }
}