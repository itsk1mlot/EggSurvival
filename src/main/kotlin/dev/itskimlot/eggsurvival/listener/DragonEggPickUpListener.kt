package dev.itskimlot.eggsurvival.listener

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

class DragonEggPickUpListener: Listener {

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (event.clickedBlock?.type != Material.DRAGON_EGG) return
        event.isCancelled = true
        event.clickedBlock?.type = Material.AIR
        event.player.inventory.addItem(ItemStack(Material.DRAGON_EGG))
    }
}