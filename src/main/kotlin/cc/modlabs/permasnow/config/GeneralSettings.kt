package cc.modlabs.permasnow.config

import com.mojang.serialization.Codec
import dev.isxander.yacl3.config.v3.JsonFileCodecConfig
import dev.isxander.yacl3.config.v3.register
import dev.isxander.yacl3.config.v3.value
import net.fabricmc.loader.api.FabricLoader

open class GeneralSettings() : JsonFileCodecConfig<GeneralSettings>(
    FabricLoader.getInstance().configDir.resolve("permasnow.json")
) {
    val alwaysSnow by register<Boolean>(default = true, Codec.BOOL)
    val weatherChange by register<Boolean>(default = false, Codec.BOOL)

    final val allSettings = arrayOf(
        alwaysSnow,
        weatherChange
    )

    constructor(settings: GeneralSettings) : this() {
        this.alwaysSnow.value = settings.alwaysSnow.value
        this.weatherChange.value = settings.weatherChange.value
    }

    companion object : GeneralSettings() {
        init {
            if (!loadFromFile()) {
                saveToFile()
            }
        }
    }
}
