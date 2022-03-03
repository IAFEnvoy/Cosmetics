package net.iafenvoy.cosmetics.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.iafenvoy.cosmetics.configs.PlayerSetting;
import net.iafenvoy.cosmetics.nicks.Nick;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

@Mixin(ServerWorld.class)
public class PlayerJoinMixin {
  @Shadow
  private MinecraftServer server;

  @Inject(method = "addPlayer", at = @At("RETURN"))
  private void addPlayer(ServerPlayerEntity player, CallbackInfo info) {
    if (PlayerSetting.data.containsKey(player.getName().getString())) {
      Nick.applyPreFix(server.getCommandSource(), player.getName().getString(),
          PlayerSetting.data.get(player.getName().getString()).getPrefix());
      Nick.applySuffix(server.getCommandSource(), player.getName().getString(),
          PlayerSetting.data.get(player.getName().getString()).getSuffix());
    }
  }
}
