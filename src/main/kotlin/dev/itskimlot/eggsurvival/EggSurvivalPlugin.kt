package dev.itskimlot.eggsurvival

import dev.itskimlot.eggsurvival.command.EggSurvivalCommand
import dev.itskimlot.eggsurvival.data.config.BanItemSettingsData
import dev.itskimlot.eggsurvival.data.config.DragonEggPenaltyData
import dev.itskimlot.eggsurvival.data.config.EnchantData
import dev.itskimlot.eggsurvival.data.config.ItemData
import dev.itskimlot.eggsurvival.data.config.MapData
import dev.itskimlot.eggsurvival.data.config.SettingsData
import dev.itskimlot.eggsurvival.listener.DragonEggPickUpListener
import dev.itskimlot.eggsurvival.listener.EndGatewayDisableListener
import dev.itskimlot.eggsurvival.listener.ExplosionDamageRatioListener
import dev.itskimlot.eggsurvival.listener.PlayerExpDropCompletelyListener
import dev.itskimlot.eggsurvival.listener.RemoveEndingListener
import dev.itskimlot.eggsurvival.listener.RespawnedDragonExpListener
import dev.itskimlot.eggsurvival.listener.VillagerTradeLimitListener
import dev.itskimlot.eggsurvival.listener.eggPenalty.AlterListener
import dev.itskimlot.eggsurvival.listener.eggPenalty.ChangeDimensionListener
import dev.itskimlot.eggsurvival.listener.eggPenalty.FootprintListener
import dev.itskimlot.eggsurvival.listener.removeChat.RemoveChatListener
import dev.itskimlot.eggsurvival.listener.removeChat.RemoveJoinMessageListener
import dev.itskimlot.eggsurvival.manager.ConfigManager
import dev.itskimlot.eggsurvival.scheduler.AlterScheduler
import dev.itskimlot.eggsurvival.scheduler.FootprintScheduler
import dev.itskimlot.eggsurvival.scheduler.MainScheduler
import dev.itskimlot.eggsurvival.util.MessageUtil.toMiniMessage
import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import org.bukkit.GameRules
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Particle
import org.bukkit.Registry
import org.bukkit.Sound
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.util.UUID

class EggSurvivalPlugin: JavaPlugin() {

    companion object {
        lateinit var instance: EggSurvivalPlugin; private set

        lateinit var configValue: SettingsData; private set

        lateinit var mainScheduler: MainScheduler; private set
        lateinit var footprintScheduler: FootprintScheduler; private set
        lateinit var alterScheduler: AlterScheduler; private set

        lateinit var configManager: ConfigManager; private set
    }
    init { instance = this }

    override fun onEnable() {
        configManager = ConfigManager()

        // load configuration files
        configManager.loadConfig("config.yml")

        configSetup()
        setupGame()

        getCommand("eggsurvival")?.setExecutor(EggSurvivalCommand())
        getCommand("eggsurvival")?.tabCompleter = EggSurvivalCommand()

        val lastFootprint = mutableMapOf<UUID, Location>()
        val nextLeft = mutableMapOf<UUID, Boolean>()
        val createdKey = NamespacedKey("eggsurvival", "footprint_created")
        val footprintText = "<#F0B5F5>▼".toMiniMessage()

        mainScheduler = MainScheduler()
        footprintScheduler = FootprintScheduler(createdKey, footprintText)
        alterScheduler = AlterScheduler()

        mainScheduler.start()
        if (configValue.dragonEggPenalty.footprint) footprintScheduler.start()

        server.pluginManager.registerEvents(DragonEggPickUpListener(), this)
        server.pluginManager.registerEvents(VillagerTradeLimitListener(), this)
        server.pluginManager.registerEvents(RespawnedDragonExpListener(), this)
        server.pluginManager.registerEvents(ExplosionDamageRatioListener(), this)
        if (!configValue.endIslandPortal) {
            server.pluginManager.registerEvents(EndGatewayDisableListener(), this)
        }
        if (!configValue.ending) {
            server.pluginManager.registerEvents(RemoveEndingListener(), this)
        }
        if (configValue.deathPlayerExpDropCompletely) {
            server.pluginManager.registerEvents(PlayerExpDropCompletelyListener(), this)
        }
        if (configValue.removeChat) {
            server.pluginManager.registerEvents(RemoveJoinMessageListener(), this)
            server.pluginManager.registerEvents(RemoveChatListener(), this)
        }
        if (configValue.dragonEggPenalty.alter) {
            server.pluginManager.registerEvents(AlterListener(), this)
        }
        if (!configValue.dragonEggPenalty.dimensionMovement) {
            server.pluginManager.registerEvents(ChangeDimensionListener(), this)
        }
        if (configValue.dragonEggPenalty.footprint) {
            server.pluginManager.registerEvents(FootprintListener(lastFootprint, nextLeft, createdKey, footprintText), this)
        }
    }

    // ------------------------------------------------
    fun configSetup() {
        val config = configManager.getConfig("config.yml")

        fun FileConfiguration.getBanItemSettings(path: String): Map<Material, BanItemSettingsData> {
            return getList(path).orEmpty().mapNotNull { entry ->
                when (entry) {
                    is String -> {
                        val material = Material.matchMaterial(entry)
                            ?: return@mapNotNull null

                        material to BanItemSettingsData()
                    }

                    is Map<*, *> -> {
                        val material = Material.matchMaterial(entry["id"] as? String ?: return@mapNotNull null) ?: return@mapNotNull null
                        val settings = entry["settings"] as? Map<*, *>
                        material to BanItemSettingsData(
                            settings?.get("drop") as? Boolean ?: false,
                            settings?.get("pickup") as? Boolean ?: false,
                            settings?.get("craft") as? Boolean ?: false,
                            settings?.get("use") as? Boolean ?: false,
                            settings?.get("place") as? Boolean ?: false,
                            settings?.get("break") as? Boolean ?: false
                        )
                    }

                    else -> null
                }
            }.toMap()
        }

        configValue = SettingsData(
            config.getInt("settings.end_day"),
            config.getInt("settings.villager_trade_limit"),
            config.getBoolean("settings.death_player_exp_drop_completely"),
            config.getInt("settings.respawned_dragon_drop_exp"),
            config.getDouble("settings.explosion_damage_ratio"),
            config.getBoolean("settings.cannot_see_location"),
            config.getBoolean("settings.remove_chat"),
            config.getBoolean("settings.ending"),
            config.getBoolean("settings.end_island_portal"),

            config.getBoolean("settings.play_sound_dragon_egg.enabled"),
            Registry.SOUND_EVENT.get(NamespacedKey.minecraft(config.getString("settings.play_sound_dragon_egg.sound") ?: "block.beacon.ambient")) ?: Sound.BLOCK_BEACON_AMBIENT,

            DragonEggPenaltyData(
                config.getBoolean("settings.dragon_egg_penalty.dimension_movement"),
                Registry.PARTICLE_TYPE.get(NamespacedKey.minecraft(config.getString("settings.dragon_egg_penalty.effect") ?: "portal")) ?: Particle.PORTAL,

                config.getBoolean("settings.dragon_egg_penalty.alter.enabled"),
                config.getInt("settings.dragon_egg_penalty.alter.duration"),

                config.getBoolean("settings.dragon_egg_penalty.footprint.enabled"),
                config.getInt("settings.dragon_egg_penalty.footprint.duration"),
                config.getBoolean("settings.dragon_egg_penalty.footprint.visible_at_night")
            ),

            MapData(
                config.getBoolean("settings.map.center_location.reset"),
                Location(
                    server.getWorld("world")!!,
                    config.getDouble("settings.map.center_location.x"),
                    0.0,
                    config.getDouble("settings.map.center_location.z"),
                ),

                config.getInt("settings.map.overworld.x"),
                config.getInt("settings.map.overworld.z"),
                config.getInt("settings.map.nether.x"),
                config.getInt("settings.map.nether.z"),
                config.getInt("settings.map.end.x"),
                config.getInt("settings.map.end.z")
            ),

            ItemData(
                config.getInt("settings.item.potion_limit"),
                config.getInt("settings.item.golden_apple_duration"),
                config.getBoolean("settings.item.netherite_template_recipe"),

                config.getBanItemSettings("settings.item.ban_items")
            ),

            EnchantData(
                config.getBoolean("settings.enchant.remove_so_expensive"),
                config.getConfigurationSection("settings.enchant.max_level")?.getKeys(false)?.mapNotNull { entry ->
                    val enchantment = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).get(NamespacedKey.minecraft(entry)) ?: return@mapNotNull null
                    enchantment to config.getInt("settings.enchant.max_level.$entry")
                }?.toMap() ?: emptyMap(),
                config.getStringList("settings.enchant.ban_enchants").mapNotNull { RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).get(NamespacedKey.minecraft(it)) }
            )
        )
    }

    fun setupGame() {
        server.worlds.forEach {
            it.setGameRule(GameRules.ADVANCE_TIME, true)
            it.setGameRule(GameRules.SHOW_ADVANCEMENT_MESSAGES, false)
            it.setGameRule(GameRules.SHOW_DEATH_MESSAGES, false)

            if (configValue.cannotSeeLocation) {
                it.setGameRule(GameRules.REDUCED_DEBUG_INFO, true)
            }
        }
    }
}