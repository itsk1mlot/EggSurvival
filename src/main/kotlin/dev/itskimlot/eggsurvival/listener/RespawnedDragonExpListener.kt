package dev.itskimlot.eggsurvival.listener

import dev.itskimlot.eggsurvival.EggSurvivalPlugin
import org.bukkit.entity.EnderDragon
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent

class RespawnedDragonExpListener: Listener {

    @EventHandler
    fun onDeath(event: EntityDeathEvent) {
        if (event.entity.type != EntityType.ENDER_DRAGON) return
        val entity = event.entity as? EnderDragon ?: return
        val battle = entity.dragonBattle ?: return

        if (battle.hasBeenPreviouslyKilled()) {
            event.droppedExp = EggSurvivalPlugin.configValue.respawnedDragonDropExp
        }
    }
}