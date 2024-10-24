package cc.modlabs.permasnow.mixins;

import cc.modlabs.permasnow.PermaSnow;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Level.class)
public abstract class MixinLevel {

    @Shadow protected float oRainLevel;

    @Shadow protected float rainLevel;

    @Inject(at = @At("RETURN"), method = "getRainLevel", cancellable = true)
    public void getRainLevel(float delta, CallbackInfoReturnable<Float> cir) {
        if (PermaSnow.Companion.getConfig().getWeatherChange().get()) {
            cir.setReturnValue(1.0F);
        } else {
            cir.setReturnValue(Mth.lerp(delta, oRainLevel, rainLevel));
        }
    }
}
