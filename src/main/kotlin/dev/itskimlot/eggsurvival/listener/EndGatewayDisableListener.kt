package dev.itskimlot.eggsurvival.listener

import com.destroystokyo.paper.event.player.PlayerTeleportEndGatewayEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class EndGatewayDisableListener: Listener {

    @EventHandler
    fun onEndGateway(event: PlayerTeleportEndGatewayEvent) {
        event.isCancelled = true
    }
}