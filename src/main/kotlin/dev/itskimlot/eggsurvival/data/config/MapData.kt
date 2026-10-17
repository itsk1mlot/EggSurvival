package dev.itskimlot.eggsurvival.data.config

import org.bukkit.Location

data class MapData(
    val centerReset: Boolean, // true면 센터 좌표 초기화, false면 기존 좌표 유지 (서버 실행시 항상 false로 변경할것)
    val centerLocation: Location,

    val overWorldX: Int,
    val overWorldZ: Int,

    val netherX: Int,
    val netherZ: Int,

    val endX: Int,
    val endZ: Int
)
