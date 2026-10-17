package dev.itskimlot.eggsurvival.listener.eggPenalty

import dev.itskimlot.eggsurvival.util.MessageUtil.toMiniMessage
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerPortalEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.UUID

class ChangeDimensionListener: Listener {
    private val lastMessageTime: MutableMap<UUID, Long> = mutableMapOf()

    @EventHandler
    fun onChangeDimension(event: PlayerPortalEvent) {
        val player = event.player

        if (player.inventory.contains(Material.DRAGON_EGG)) {
            event.isCancelled = true

            val now = System.currentTimeMillis()
            val last = lastMessageTime[player.uniqueId] ?: 0L
            if (now - last >= 10_000L) {
                player.sendMessage("<red>드래곤 알을 보유한 채로 차원을 이동할 수 없습니다.".toMiniMessage())
                lastMessageTime[player.uniqueId] = now
            }
        }
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        lastMessageTime.remove(event.player.uniqueId)
    }
}