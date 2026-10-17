package dev.itskimlot.eggsurvival.data.config

data class BanItemSettingsData(
    val drop: Boolean = false,
    val pickup: Boolean = false,
    val craft: Boolean = false,
    val use: Boolean = false,
    val place: Boolean = false,
    val breakBlock: Boolean = false // break가 예약어네
)
