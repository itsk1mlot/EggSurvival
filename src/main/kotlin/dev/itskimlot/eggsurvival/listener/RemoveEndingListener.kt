package dev.itskimlot.eggsurvival.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class RemoveEndingListener: Listener {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        event.player.setHasSeenWinScreen(true)
    }
}