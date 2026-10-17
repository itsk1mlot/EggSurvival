package dev.itskimlot.eggsurvival.data.config

import org.bukkit.Material

data class ItemData(
    val potionLimit: Int,
    val goldenAppleDuration: Int,
    val NetheriteTemplateRecipe: Boolean,

    val banItems: Map<Material, BanItemSettingsData>
)