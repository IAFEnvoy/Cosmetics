package net.iafenvoy.cosmetics.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.server.command.ParticleCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

@Mixin(ParticleCommand.class)
public abstract class RemoveFeedbackMixin {
  @Redirect(method = "execute", at = @At(value = "INVOKE", target = " Lnet/minecraft/server/command/ServerCommandSource;sendFeedback(Lnet/minecraft/text/Text;Z)V"))
  private static void sendFeedback(ServerCommandSource source, Text message, boolean broadcastToOps) {
    // do nothing 
  }
}
