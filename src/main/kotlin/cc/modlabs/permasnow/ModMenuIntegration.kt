package cc.modlabs.permasnow

import cc.modlabs.permasnow.config.SettingsGuiFactory
import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi

object ModMenuIntegration : ModMenuApi {
    override fun getModConfigScreenFactory(): ConfigScreenFactory<*> = ConfigScreenFactory { parent ->
        SettingsGuiFactory().createSettingsGui(parent)
    }
}
