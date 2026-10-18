package dev.itskimlot.eggsurvival.listener

import dev.itskimlot.eggsurvival.EggSurvivalPlugin
import dev.itskimlot.eggsurvival.util.MessageUtil.toMiniMessage
import io.papermc.paper.event.player.PlayerTradeEvent
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.io.File
import java.util.UUID

class VillagerTradeLimitListener: Listener {

    private val tradeCounts = mutableMapOf<UUID, Int>()
    private var lastDay = -1L

    @EventHandler
    fun onVillagerTrade(event: PlayerTradeEvent) {
        val player = event.player
        val world = Bukkit.getWorld("world") ?: return
        val day = world.fullTime / 24000L

        if (lastDay != day) {
            tradeCounts.clear()
            lastDay = day
        }

        val count = tradeCounts.getOrDefault(player.uniqueId, 0)
        if (count >= EggSurvivalPlugin.configValue.villagerTradeLimit) {
            event.isCancelled = true
            player.sendMessage("<red>주민거래는 하루에 ${EggSurvivalPlugin.configValue.villagerTradeLimit}회로 제한되어 있습니다!".toMiniMessage())
            return
        }

        tradeCounts[player.uniqueId] = count + 1
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        if (!File(EggSurvivalPlugin.instance.dataFolder, "data/${event.player.uniqueId}.yml").exists()) return

        val configManager = EggSurvivalPlugin.configManager
        configManager.loadConfig("data/${event.player.uniqueId}.yml")
        val dataConfig = configManager.getConfig("data/${event.player.uniqueId}.yml")
        val tradeCount = dataConfig.getInt("trade_count", 0)
        tradeCounts[event.player.uniqueId] = tradeCount
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        if (!tradeCounts.containsKey(event.player.uniqueId)) return

        val configManager = EggSurvivalPlugin.configManager
        configManager.loadConfig("data/${event.player.uniqueId}.yml")
        val dataConfig = configManager.getConfig("data/${event.player.uniqueId}.yml")
        dataConfig.set("trade_count", tradeCounts[event.player.uniqueId] ?: 0)
        dataConfig.save(File(EggSurvivalPlugin.instance.dataFolder, "data/${event.player.uniqueId}.yml"))
        tradeCounts.remove(event.player.uniqueId)
    }
}