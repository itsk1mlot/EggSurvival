package dev.itskimlot.eggsurvival.scheduler

import dev.itskimlot.eggsurvival.EggSurvivalPlugin
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.scheduler.BukkitTask

class AlterScheduler {

    val scheduler: MutableMap<List<Location>, BukkitTask?> = mutableMapOf()

    fun start(alterBlocks: List<Location>) {
        scheduler[alterBlocks] = Bukkit.getScheduler().runTaskLater(EggSurvivalPlugin.instance, Runnable {
            alterBlocks.forEach { location ->
                location.block.type = Material.AIR
            }
        }, EggSurvivalPlugin.configValue.dragonEggPenalty.alterDuration * 24000L)
    }

    fun stop(alterBlocks: List<Location>) {
        scheduler[alterBlocks]?.cancel()
        scheduler.remove(alterBlocks)
    }
}