package dev.itskimlot.eggsurvival.listener.removeChat

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class RemoveJoinMessageListener: Listener {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        event.joinMessage(null)
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        event.quitMessage(null)
    }
}