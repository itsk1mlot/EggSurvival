package dev.itskimlot.eggsurvival.manager

import dev.itskimlot.eggsurvival.EggSurvivalPlugin
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

class ConfigManager {

    private val configs: MutableList<String> = mutableListOf()

    fun loadConfig(name: String) {
        EggSurvivalPlugin.instance.saveResource(name, false)
        configs.add(name)
        EggSurvivalPlugin.instance.logger.info("Config file '$name' has been loaded.")
    }

    fun getConfig(name: String): YamlConfiguration {
        if (!configs.contains(name)) {
            EggSurvivalPlugin.instance.logger.severe("Config file '$name' does not exist.")
            throw IllegalArgumentException("Config file '$name' does not exist.")
        }

        val file = File(EggSurvivalPlugin.instance.dataFolder, name)
        if (!file.exists()) {
            EggSurvivalPlugin.instance.logger.severe("Config file '$name' does not exist in the data folder.")
            throw IllegalArgumentException("Config file '$name' does not exist in the data folder.")
        }
        if (!file.isFile) {
            EggSurvivalPlugin.instance.logger.severe("Config file '$name' is not a valid file.")
            throw IllegalArgumentException("Config file '$name' is not a valid file.")
        }

        return YamlConfiguration.loadConfiguration(file)
    }
}