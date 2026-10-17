package dev.itskimlot.eggsurvival.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage

object MessageUtil {

    val mm = MiniMessage.miniMessage()

    fun String.toMiniMessage(): Component {
        return mm.deserialize("<!i>$this")
    }
}