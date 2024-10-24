package cc.modlabs.permasnow.config

import dev.isxander.yacl3.config.v3.register
import dev.isxander.yacl3.dsl.*
import net.minecraft.client.gui.screens.Screen

class SettingsGuiFactory {
    fun createSettingsGui(parent: Screen?) = YetAnotherConfigLib("PermaSnow") {
        save(GeneralSettings::saveToFile)
        val secondary by categories.registering {
            val infoLabel by rootOptions.registeringLabel

            rootOptions.register(GeneralSettings.alwaysSnow) {
                descriptionBuilder {
                    addDefaultText("Toggle snow override")
                }
                controller = tickBox()
            }

            rootOptions.register(GeneralSettings.weatherChange) {
                descriptionBuilder {
                    addDefaultText("Toggle always raining")
                }
                controller = tickBox()
            }
        }
    }.generateScreen(parent)
}

