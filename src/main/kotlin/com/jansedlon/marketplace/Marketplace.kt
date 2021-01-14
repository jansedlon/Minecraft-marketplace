package com.jansedlon.marketplace

import com.jansedlon.marketplace.Listeners.MarketplaceInteract
import net.milkbowl.vault.chat.Chat
import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.configuration.serialization.ConfigurationSerialization
import org.bukkit.conversations.ConversationFactory
import org.bukkit.plugin.RegisteredServiceProvider
import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Logger

class Marketplace : JavaPlugin() {

    companion object {
        lateinit var economy: Economy
        lateinit var perms: net.milkbowl.vault.permission.Permission
        lateinit var chat: Chat
        lateinit var marketplaceManager: MarketplaceManager
        lateinit var main: Marketplace
        lateinit var conversationFactory: ConversationFactory
        lateinit var storageManager: StorageManager
        val log = Logger.getLogger("Minecraft")
    }

    override fun onLoad() {
        ConfigurationSerialization.registerClass(MarketplaceItem::class.java)
    }

    override fun onEnable() {
        main = this

        config.options().copyDefaults(true)
        saveDefaultConfig()

        if (!setupEconomy()) {
            log.severe("[${description.name}] - Deisabled due to no Vault dependency found!")
            server.pluginManager.disablePlugin(this)
            return
        }

        setupPermissions()
        setupChat()

        Bukkit.getPluginCommand("marketplace")?.setExecutor(MarketplaceCommand())
        Bukkit.getPluginManager().registerEvents(MarketplaceInteract(), this)
        Bukkit.getPluginManager().registerEvents(MarketplaceChat(), this)

        marketplaceManager = MarketplaceManager()

        conversationFactory = ConversationFactory(this)
            .withModality(false)
            .withFirstPrompt(SearchItemPrompt())
            .withEscapeSequence("/quit")
            .withTimeout(30)
            .thatExcludesNonPlayersWithMessage("Go away evil console!")

        storageManager = StorageManager()
        storageManager.loadMarketplaceItems()
    }

    override fun onDisable() {

    }

    private fun setupEconomy(): Boolean {
        if (server.pluginManager.getPlugin("Vault") == null) {
            return false
        }

        val rsp: RegisteredServiceProvider<Economy> = server.servicesManager.getRegistration(Economy::class.java)
            ?: return false

        economy = rsp.provider

        return true
    }

    private fun setupChat(): Boolean {
        val rsp: RegisteredServiceProvider<Chat> = server.servicesManager.getRegistration(Chat::class.java) ?: return false
        chat = rsp.provider

        return true
    }

    private fun setupPermissions(): Boolean {
        val rsp: RegisteredServiceProvider<net.milkbowl.vault.permission.Permission> = server.servicesManager.getRegistration(net.milkbowl.vault.permission.Permission::class.java) ?: return false

        perms = rsp.provider

        return true
    }
}