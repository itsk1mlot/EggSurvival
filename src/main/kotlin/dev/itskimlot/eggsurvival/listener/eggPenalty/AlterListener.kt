package dev.itskimlot.eggsurvival.listener.eggPenalty

import dev.itskimlot.eggsurvival.EggSurvivalPlugin
import dev.itskimlot.eggsurvival.util.MessageUtil.toMiniMessage
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.block.data.Bisected
import org.bukkit.block.data.type.Stairs
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerInteractEvent

class AlterListener : Listener {

    val alterBlockLocations = mutableListOf<Location>()

    @EventHandler
    fun onDeath(event: PlayerDeathEvent) {
        val player = event.player
        if (!player.inventory.contains(Material.DRAGON_EGG) && event.drops.none { it.type == Material.DRAGON_EGG }) return

        player.inventory.remove(Material.DRAGON_EGG)
        event.drops.removeIf { it.type == Material.DRAGON_EGG }

        if (alterBlockLocations.isNotEmpty()) {
            EggSurvivalPlugin.alterScheduler.stop(alterBlockLocations)

            alterBlockLocations.forEach {
                it.block.type = Material.AIR
            }

            alterBlockLocations.clear()
        }

        val center = player.location.block.location

        fun placeBlock(location: Location, material: Material) {
            location.block.setType(material, false)
            alterBlockLocations.add(location.block.location)
        }

        for (x in -1..1) {
            for (z in -1..1) {
                val netheriteLoc = center.clone().add(x.toDouble(), -2.0, z.toDouble())
                placeBlock(netheriteLoc, Material.NETHERITE_BLOCK)

                if (x == 0 && z == 0) continue

                val stairsLoc = center.clone().add(x.toDouble(), 0.0, z.toDouble())
                placeBlock(stairsLoc, Material.MOSSY_COBBLESTONE_STAIRS)
            }
        }

        for (x in -1..1) {
            for (z in -1..1) {
                if (x == 0 && z == 0) continue

                val block = center.clone().add(x.toDouble(), 0.0, z.toDouble()).block
                val stairs = block.blockData as Stairs

                val (facing, shape) = when {
                    x == -1 && z == -1 -> BlockFace.SOUTH to Stairs.Shape.INNER_LEFT
                    x == 1 && z == -1 -> BlockFace.WEST to Stairs.Shape.INNER_LEFT
                    x == 1 && z == 1 -> BlockFace.NORTH to Stairs.Shape.INNER_LEFT
                    x == -1 && z == 1 -> BlockFace.EAST to Stairs.Shape.INNER_LEFT

                    z == -1 -> BlockFace.SOUTH to Stairs.Shape.STRAIGHT
                    z == 1 -> BlockFace.NORTH to Stairs.Shape.STRAIGHT
                    x == -1 -> BlockFace.EAST to Stairs.Shape.STRAIGHT
                    else -> BlockFace.WEST to Stairs.Shape.STRAIGHT
                }

                stairs.facing = facing
                stairs.half = Bisected.Half.BOTTOM
                stairs.shape = shape

                block.setBlockData(stairs, false)
            }
        }

        placeBlock(center.clone().add(0.0, -1.0, 0.0), Material.BEACON)
        placeBlock(center, Material.PURPLE_STAINED_GLASS)
        placeBlock(center.clone().add(0.0, 1.0, 0.0), Material.DRAGON_EGG)

        EggSurvivalPlugin.alterScheduler.start(alterBlockLocations)
    }

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        val block = event.clickedBlock ?: return
        if (!alterBlockLocations.contains(block.location)) return
        if (block.type == Material.DRAGON_EGG) return

        event.isCancelled = true
        event.player.sendMessage("<red>제단의 블록은 상호작용할 수 없습니다!".toMiniMessage())
    }
}