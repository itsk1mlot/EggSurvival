package dev.itskimlot.eggsurvival.listener

import dev.itskimlot.eggsurvival.EggSurvivalPlugin
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent

class ExplosionDamageRatioListener: Listener {

    @EventHandler
    fun onDamage(event: EntityDamageEvent) {
        if (event.cause != EntityDamageEvent.DamageCause.BLOCK_EXPLOSION && event.cause != EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) return
        event.damage *= EggSurvivalPlugin.configValue.explosionDamageRatio
    }
}