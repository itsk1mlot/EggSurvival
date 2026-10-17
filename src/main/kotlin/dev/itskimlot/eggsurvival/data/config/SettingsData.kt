package dev.itskimlot.eggsurvival.data.config

import org.bukkit.Sound

data class SettingsData(
    val endDay: Int,
    val villagerTradeLimit: Int,
    val deathPlayerExpDropCompletely: Boolean,
    val respawnedDragonDropExp: Int,
    val explosionDamageRatio: Double,
    val cannotSeeLocation: Boolean,
    val removeChat: Boolean,
    val ending: Boolean,
    val endIslandPortal: Boolean,

    val playSoundAtDragonEgg: Boolean,
    val playSoundAtDragonEggSound: Sound,

    val dragonEggPenalty: DragonEggPenaltyData,
    val map: MapData,
    val item: ItemData,
    val enchant: EnchantData
)
