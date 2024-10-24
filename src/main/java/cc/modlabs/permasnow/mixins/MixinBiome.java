package cc.modlabs.permasnow.mixins;

import cc.modlabs.permasnow.PermaSnow;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Biome.class)
public abstract class MixinBiome {

    @Shadow public abstract boolean hasPrecipitation();

    public abstract boolean coldEnoughToSnow(BlockPos blockPos);

    @Inject(at = @At("HEAD"), method = "getPrecipitationAt", cancellable = true)
    public void getPrecipitationAt(BlockPos blockPos, CallbackInfoReturnable<Biome.Precipitation> cir) {
        if (!hasPrecipitation()) {
            cir.setReturnValue(Biome.Precipitation.NONE);
        } else {
            if (PermaSnow.Companion.getConfig().getAlwaysSnow().get()) {
                cir.setReturnValue(Biome.Precipitation.SNOW);
            } else {
                cir.setReturnValue(coldEnoughToSnow(blockPos) ? Biome.Precipitation.SNOW : Biome.Precipitation.RAIN);
            }
        }
    }
}
