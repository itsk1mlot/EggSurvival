package dev.itskimlot.eggsurvival.data.config

import org.bukkit.enchantments.Enchantment

data class EnchantData(
    val removeSoExpensive: Boolean,
    val enchantMaxLevel: Map<Enchantment, Int>,
    val banEnchants: List<Enchantment>
)
