package dev.itskimlot.eggsurvival.listener.eggPenalty

import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

class DeathListener: Listener {

    @EventHandler
    fun onDeath(event: PlayerDeathEvent) {
        val player = event.player
        if (player.inventory.contains(Material.DRAGON_EGG)) {
            player.inventory.remove(Material.DRAGON_EGG)
            event.drops.removeIf { item -> item.type == Material.EGG }

            // spawn alter
            player.location.block.type = Material.DRAGON_EGG

            player.location.subtract(0.0, 1.0, 0.0).block.type = Material.BEDROCK
            player.location.subtract(1.0, 1.0, 0.0).block.type = Material.BEDROCK
            player.location.subtract(0.0, 1.0, 1.0).block.type = Material.BEDROCK
            player.location.subtract(-1.0, 1.0, 0.0).block.type = Material.BEDROCK
            player.location.subtract(0.0, 1.0, -1.0).block.type = Material.BEDROCK
            player.location.subtract(1.0, 1.0, 1.0).block.type = Material.BEDROCK
            player.location.subtract(-1.0, 1.0, -1.0).block.type = Material.BEDROCK
            player.location.subtract(1.0, 1.0, -1.0).block.type = Material.BEDROCK
            player.location.subtract(-1.0, 1.0, 1.0).block.type = Material.BEDROCK

            player.location.add(1.0, 0.0, 0.0).block.type = Material.MOSSY_COBBLESTONE_STAIRS
            player.location.add(-1.0, 0.0, 0.0).block.type = Material.MOSSY_COBBLESTONE_STAIRS
            player.location.add(0.0, 0.0, 1.0).block.type = Material.MOSSY_COBBLESTONE_STAIRS
            player.location.add(0.0, 0.0, -1.0).block.type = Material.MOSSY_COBBLESTONE_STAIRS
            player.location.add(1.0, 0.0, 1.0).block.type = Material.MOSSY_COBBLESTONE_STAIRS
            player.location.add(-1.0, 0.0, -1.0).block.type = Material.MOSSY_COBBLESTONE_STAIRS
            player.location.add(1.0, 0.0, -1.0).block.type = Material.MOSSY_COBBLESTONE_STAIRS
            player.location.add(-1.0, 0.0, 1.0).block.type = Material.MOSSY_COBBLESTONE_STAIRS
        }
    }
}