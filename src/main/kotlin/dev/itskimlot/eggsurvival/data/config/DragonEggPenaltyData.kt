package dev.itskimlot.eggsurvival.data.config

import org.bukkit.Particle

data class DragonEggPenaltyData(
    val dimensionMovement: Boolean,
    val effect: Particle,

    val alter: Boolean,
    val alterBeacon: Boolean,
    val alterDuration: Int,

    val footprint: Boolean,
    val footprintDuration: Int,
    val footprintVisibleAtNight: Boolean
)
