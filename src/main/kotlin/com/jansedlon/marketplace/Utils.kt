package com.jansedlon.marketplace

import com.github.vickumar1981.stringdistance.StringDistance
import com.github.vickumar1981.stringdistance.`StringDistance$`
import com.github.vickumar1981.stringdistance.util.StringDistance.levenshteinDist
import org.bukkit.entity.Player

class Utils {
    companion object {
        fun searchItemsByName(searchTerm: String, allItems: ArrayList<MarketplaceItem>): List<MarketplaceItem> {
            return allItems.filter { searchTerm == it.item.type.name.replace("_", " ").toLowerCase() }
        }

        fun searchItemsByLore(searchTerm: String, allItems: ArrayList<MarketplaceItem>): List<MarketplaceItem> {
            return allItems.filter { it.item.itemMeta?.lore?.any { lore -> levenshteinDist(searchTerm, lore) <= 2} ?: false }
        }

        fun playerHasPermission(player: Player, permissionTag: String): Boolean {
            return Marketplace.perms.has(player, permissionTag)
        }
    }
}