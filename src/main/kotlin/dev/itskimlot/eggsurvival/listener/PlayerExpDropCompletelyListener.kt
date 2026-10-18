package dev.itskimlot.eggsurvival.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

class PlayerExpDropCompletelyListener: Listener {

    @EventHandler
    fun onDeath(event: PlayerDeathEvent) {
        val player = event.player
        val level = player.level
        val progress = player.exp

        val baseExp = when {
            level <= 16 -> level * level + 6 * level
            level <= 31 -> (2.5 * level * level - 40.5 * level + 360).toInt()
            else -> (4.5 * level * level - 162.5 * level + 2220).toInt()
        }

        val currentLevelExp = player.expToLevel
        event.droppedExp = baseExp + (progress * currentLevelExp).toInt()
    }
}