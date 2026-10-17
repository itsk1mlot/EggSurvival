package dev.itskimlot.eggsurvival.listener.eggPenalty

import dev.itskimlot.eggsurvival.util.MessageUtil.toMiniMessage
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Display
import org.bukkit.entity.Entity
import org.bukkit.entity.TextDisplay
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.persistence.PersistentDataType
import org.bukkit.util.Transformation
import org.joml.Quaternionf
import org.joml.Vector3f
import java.util.UUID

class FootprintListener(val lastFootprint: MutableMap<UUID, Location>, val nextLeft: MutableMap<UUID, Boolean>, val createdKey: NamespacedKey, val footprintText: Component): Listener {

    @EventHandler
    fun onPlayerMovement(event: PlayerMoveEvent) {
        val player = event.player
        if (!player.inventory.contains(Material.DRAGON_EGG)) return
        if (!(player as Entity).isOnGround) return

        val to = event.to
        val from = event.from
        if (from.x == to.x && from.z == to.z) return

        val last = lastFootprint[player.uniqueId]
        if (last != null && last.world == to.world && last.distanceSquared(to) < 0.36) return

        val direction = to.toVector().subtract(from.toVector()).setY(0.0)

        if (direction.lengthSquared() == 0.0) return
        direction.normalize()

        val left = nextLeft.getOrDefault(player.uniqueId, true)
        val offset = if (left) -0.22 else 0.22
        val location = to.clone().add(-direction.z * offset, 0.0, direction.x * offset)

        val overworld = Bukkit.getWorld("world") ?: return
        val time = overworld.fullTime
        val isDay = overworld.isDayTime

        location.world.spawn(location, TextDisplay::class.java) { display ->
            display.text(
                if (isDay) footprintText else Component.empty()
            )

            display.billboard = Display.Billboard.FIXED
            display.isShadowed = false
            display.isSeeThrough = true
            display.backgroundColor = Color.fromARGB(0, 0, 0, 0)

            display.transformation = Transformation(
                Vector3f(0f, 0f, 0f),
                Quaternionf().rotationX((-Math.PI / 2).toFloat()),
                Vector3f(0.045f, 0.045f, 0.045f),
                Quaternionf()
            )

            display.persistentDataContainer.set(
                createdKey,
                PersistentDataType.LONG,
                time
            )
        }

        lastFootprint[player.uniqueId] = to.clone()
        nextLeft[player.uniqueId] = !left
    }
}