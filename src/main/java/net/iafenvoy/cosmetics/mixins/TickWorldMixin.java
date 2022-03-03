package net.iafenvoy.cosmetics.mixins;

import java.util.function.BooleanSupplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.iafenvoy.cosmetics.configs.PlayerSetting;
import net.iafenvoy.cosmetics.particles.MultiParticleType;
import net.minecraft.server.MinecraftServer;

@Mixin(MinecraftServer.class)
public class TickWorldMixin {
  @Inject(method = "tickWorlds(Ljava/util/function/BooleanSupplier;)V", at = @At("HEAD"))
  private void tickWorlds(BooleanSupplier shouldKeepTicking, CallbackInfo info) {
    for (PlayerSetting setting : PlayerSetting.data.values()) {
      MultiParticleType type = setting.getParticle();
      if (type != null)
        type.summon((MinecraftServer) (Object) this, setting.getName());
    }
  }
}
