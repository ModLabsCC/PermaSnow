package cc.modlabs.permasnow

import cc.modlabs.permasnow.config.GeneralSettings
import net.fabricmc.api.ClientModInitializer
import org.slf4j.LoggerFactory

class PermaSnow : ClientModInitializer {
    val logger = LoggerFactory.getLogger("PermaSnow")

    companion object {
        fun getConfig() = GeneralSettings()
    }

    override fun onInitializeClient() {
        logger.info("PermaSnow initialized")
        // imports on <init>
        GeneralSettings
    }
}
