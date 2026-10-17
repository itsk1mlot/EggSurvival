package dev.itskimlot.eggsurvival.listener.removeChat

import io.papermc.paper.event.player.AsyncChatEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class RemoveChatListener: Listener {

    @EventHandler
    fun onChat(event: AsyncChatEvent) {
        event.isCancelled = true
    }
}