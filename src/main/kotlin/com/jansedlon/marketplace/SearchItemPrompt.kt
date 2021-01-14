package com.jansedlon.marketplace

import org.bukkit.ChatColor
import org.bukkit.conversations.ConversationContext
import org.bukkit.conversations.Prompt
import org.bukkit.conversations.StringPrompt
import org.bukkit.entity.Player

class SearchItemPrompt : StringPrompt() {
    override fun getPromptText(context: ConversationContext): String {
        return "${ChatColor.GREEN}Enter item name to search for"
    }

    override fun acceptInput(context: ConversationContext, input: String?): Prompt? {
        if (input == null) {
            return END_OF_CONVERSATION
        }

        Marketplace.marketplaceManager.showSearchByNameItems(input, context.forWhom as Player)

        return END_OF_CONVERSATION
    }
}