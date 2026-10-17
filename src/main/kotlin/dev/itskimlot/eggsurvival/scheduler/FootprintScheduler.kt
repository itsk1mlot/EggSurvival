package dev.itskimlot.eggsurvival.scheduler

import dev.itskimlot.eggsurvival.EggSurvivalPlugin
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.entity.TextDisplay
import org.bukkit.persistence.PersistentDataType
import org.bukkit.scheduler.BukkitTask

class FootprintScheduler(val createdKey: NamespacedKey, val footprintText: Component) {

    var scheduler: BukkitTask? = null

    fun start() {
        scheduler = Bukkit.getScheduler().runTaskTimer(EggSurvivalPlugin.instance, Runnable {
            val overworld = Bukkit.getWorld("world") ?: return@Runnable
            val time = overworld.fullTime
            val isDay = overworld.isDayTime

            Bukkit.getWorlds().forEach { world ->
                world.getEntitiesByClass(TextDisplay::class.java).forEach { display ->
                    val created = display.persistentDataContainer.get(createdKey, PersistentDataType.LONG) ?: return@forEach
                    if (time - created >= (EggSurvivalPlugin.configValue.dragonEggPenalty.footprintDuration) * 24000L) {
                        display.remove()
                        return@forEach
                    }
                    display.text(
                        if (isDay) footprintText else Component.empty()
                    )
                }
            }
        }, 0L, 20L)
    }

    fun stop() {
        scheduler?.cancel()
        scheduler = null
    }
}