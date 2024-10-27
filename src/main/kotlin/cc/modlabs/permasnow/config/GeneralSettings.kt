package cc.modlabs.permasnow.config

import com.mojang.serialization.Codec
import dev.isxander.yacl3.api.OptionFlag
import dev.isxander.yacl3.config.v3.JsonFileCodecConfig
import dev.isxander.yacl3.config.v3.register
import dev.isxander.yacl3.dsl.*
import dev.isxander.yacl3.platform.YACLPlatform
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

object GeneralSettings : JsonFileCodecConfig<GeneralSettings>(YACLPlatform.getConfigDir().resolve("permasnow.json")) {
    val alwaysSnow by register<Boolean>(false, Codec.BOOL)
    val weatherChange by register<Boolean>(true, Codec.BOOL)

    init {
        if (!loadFromFile()) {
            saveToFile()
        }
    }

    fun generateConfigScreen(lastScreen: Screen?) = YetAnotherConfigLib("permasnow") {
        title(Component.literal("Permasnow Settings"))

        save {
            saveToFile()
        }

        val settingsCategory by categories.registering {
            name { Component.literal("Settings") }

            tooltip {
                +Component.translatable("Configure your weather settings.")
            }

            val snowOption by rootOptions.registering<Boolean> {
                name { Component.literal("Always Snow") }
                binding = alwaysSnow.asBinding()
                controller = textSwitch { bool -> Component.literal(if (bool) "Enabled" else "Disabled") }
                flag(OptionFlag.ASSET_RELOAD)
            }

            val weatherChangeOption by rootOptions.registering<Boolean> {
                name { Component.literal("Weather Change") }
                binding = weatherChange.asBinding()
                controller = textSwitch { bool -> Component.literal(if (bool) "Enabled" else "Disabled") }
                flag(OptionFlag.ASSET_RELOAD)
            }
        }
    }.generateScreen(lastScreen)
}

