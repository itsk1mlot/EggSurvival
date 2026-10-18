package dev.itskimlot.eggsurvival.scheduler

import dev.itskimlot.eggsurvival.EggSurvivalPlugin
import dev.itskimlot.eggsurvival.util.MessageUtil.toMiniMessage
import net.kyori.adventure.title.TitlePart
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.scheduler.BukkitTask

class MainScheduler {

    var scheduler: BukkitTask? = null

    fun start() {
        if (scheduler != null) return

        val world = Bukkit.getServer().getWorld("world")!!
        scheduler = Bukkit.getScheduler().runTaskTimer(EggSurvivalPlugin.instance, Runnable {
            // day표시
            val day = world.fullTime / 24000L + 1
            Bukkit.getOnlinePlayers().forEach { player ->
                player.sendActionBar("<white>Day: $day / ${EggSurvivalPlugin.configValue.endDay}".toMiniMessage())
            }

            // endday 확인
            if (day >= EggSurvivalPlugin.configValue.endDay) {
                Bukkit.getOnlinePlayers().forEach { player ->
                    player.sendActionBar("".toMiniMessage())
                    player.sendMessage("<green>게임이 종료되었습니다 - ${getWinner()} 승리!".toMiniMessage())

                    player.sendTitlePart(TitlePart.TITLE, "<green>GAME OVER".toMiniMessage())
                    player.sendTitlePart(TitlePart.SUBTITLE, "<gray>Winner: <white>${getWinner()}".toMiniMessage())
                }
                stop()
            }

            // 드래곤 알 보유 패널티
            Bukkit.getOnlinePlayers().forEach { player ->
                if (player.inventory.contains(Material.DRAGON_EGG)) {
                    player.sendActionBar("<white>Day: $day <gray>| <light_purple>당신은 드래곤 알을 가지고 있습니다!".toMiniMessage())
                    player.world.spawnParticle(EggSurvivalPlugin.configValue.dragonEggPenalty.effect, player.location, 50, 0.5, 1.0, 0.5, 0.1)
                }
            }
        }, 0L, 20L)
    }

    fun stop() {
        scheduler?.cancel()
        scheduler = null
    }

    // ---------------------------------
    private fun getWinner(): String {
        val world = Bukkit.getServer().getWorld("world")!!
        val day = world.fullTime / 24000L + 1
        if (day < EggSurvivalPlugin.configValue.endDay) return ""

        return Bukkit.getOnlinePlayers()
            .firstOrNull { it.inventory.contains(Material.DRAGON_EGG) }
            ?.name ?: ""
    }
}