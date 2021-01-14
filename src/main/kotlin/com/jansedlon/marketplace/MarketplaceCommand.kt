package com.jansedlon.marketplace

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class MarketplaceCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            Marketplace.log.info("Only players are supported for this example plugin, but you should not do this!!")
            return true
        }

        if (command.label != "marketplace") return true
        if (args.isEmpty()) return false

        val player = sender
        val subcommand = args[0]

        when(subcommand) {
            "search" -> {
                if (!Marketplace.perms.has(player, "marketplace.search")) {
                    player.sendMessage("${ChatColor.RED}You're not allowed to use this command.")
                    return true
                }

                Marketplace.marketplaceManager.showSearchMenu(player)
            }
            "publish" -> {
                if (!Marketplace.perms.has(player, "marketplace.publish")) {
                    player.sendMessage("${ChatColor.RED}You're not allowed to use this command.")
                    return true
                }

                val price: Double

                try {
                    price = args[1].toDouble()
                } catch (e: ArrayIndexOutOfBoundsException) {
                    player.sendMessage("${ChatColor.RED}You need to enter price!")
                    return true
                }

                if (player.inventory.itemInMainHand.type == Material.AIR) {
                    player.sendMessage("${ChatColor.RED}You cannot sell your hand. (Your hand is empty)")
                    return true
                }

                Marketplace.marketplaceManager.publishItem(player, player.inventory.itemInMainHand, price)
                player.inventory.removeItem(player.inventory.itemInMainHand)

                player.sendMessage("${ChatColor.GREEN}You item has been successfully published for ${price}$")

                for(item in Marketplace.marketplaceManager.items) {
                    player.sendMessage("Item ${item.item.itemMeta!!.displayName} costs ${item.price} from ${Bukkit.getOfflinePlayer(item.ownerId).player?.name}")
                }
            }
        }

        return true
    }
}
