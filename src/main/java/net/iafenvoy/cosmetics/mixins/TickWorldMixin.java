package net.iafenvoy.cosmetics.mixins;

import java.util.function.BooleanSupplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.iafenvoy.cosmetics.particles.MultiParticleType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerNetworkIo;

@Mixin(MinecraftServer.class)
public class TickWorldMixin {

  @Shadow
  private ServerNetworkIo networkIo;

  @Inject(method = "tickWorlds(Ljava/util/function/BooleanSupplier;)V", at = @At("HEAD"))
  private void tickWorlds(BooleanSupplier shouldKeepTicking, CallbackInfo info) {
    try {
      MultiParticleType.getByName("Fire").summon(networkIo.getServer(), "IAFEnvoy");
    } catch (Exception e) {
    }
  }
}
