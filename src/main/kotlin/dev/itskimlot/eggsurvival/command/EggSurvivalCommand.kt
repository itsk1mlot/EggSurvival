package dev.itskimlot.eggsurvival.command

import dev.itskimlot.eggsurvival.EggSurvivalPlugin
import dev.itskimlot.eggsurvival.util.MessageUtil.toMiniMessage
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class EggSurvivalCommand: CommandExecutor, TabCompleter {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        fun sendHelpMessage() {
            sender.sendMessage("<red>/eggsurvival [reload] 로 사용할 수 있습니다.".toMiniMessage())
        }

        if (args.isEmpty()) {
            sendHelpMessage()
            return true
        }

        if (args[0] == "reload") {
            EggSurvivalPlugin.instance.configSetup()
            sender.sendMessage("<green>EggSurvival 설정 파일을 다시 불러왔습니다.".toMiniMessage())
        } else sendHelpMessage()

        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>): List<String> {
        return if (args.size == 1) {
            listOf("reload").filter { it.startsWith(args[0]) }
        } else {
            emptyList()
        }
    }
}