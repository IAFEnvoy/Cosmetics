package net.iafenvoy.cosmetics.mixins;

import com.mojang.authlib.GameProfile;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.iafenvoy.cosmetics.configs.PlayerSetting;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(PlayerEntity.class)
public class AddNickMixin {
  private GameProfile profile;

  @Inject(method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;FLcom/mojang/authlib/GameProfile;)V", at = @At("RETURN"))
  private void init(World world, BlockPos pos, float yaw, GameProfile profile, CallbackInfo info) {
    this.profile = profile;
  }

  @Inject(method = "getDisplayName", at = @At("HEAD"), cancellable = true)
  private void getDisplayName(CallbackInfoReturnable<Text> ret) {
    String nick = "%s";
    if (PlayerSetting.data.containsKey(profile.getName()))
      nick = PlayerSetting.data.get(profile.getName()).getNick();
    ret.setReturnValue(new LiteralText(String.format(nick, profile.getName())));
  }

  // @Inject(method = "getName", at = @At("HEAD"), cancellable = true)
  // private void getName(CallbackInfoReturnable<String> ret) {
  // String nick = "%s";
  // if (PlayerSetting.data.containsKey(profile.getName()))
  // nick = PlayerSetting.data.get(profile.getName()).getNick();
  // ret.setReturnValue(String.format(nick, profile.getName()));
  // }
}
