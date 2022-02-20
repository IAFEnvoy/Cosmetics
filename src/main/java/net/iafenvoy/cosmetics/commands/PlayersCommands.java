package net.iafenvoy.cosmetics.commands;

import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.iafenvoy.cosmetics.Cosmetics;
import net.iafenvoy.cosmetics.configs.PlayerSetting;
import net.minecraft.command.CommandSource;
import net.minecraft.text.LiteralText;
import static net.minecraft.server.command.CommandManager.*;

import com.mojang.brigadier.arguments.StringArgumentType;

public class PlayersCommands {
  public static void registryCommands() {
    CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
      dispatcher.register(literal(Cosmetics.MOD_ID)
          .then(literal("nick")
              .then(literal("use")
                  .then(argument("name", StringArgumentType.string())
                      .suggests((context, builder) -> CommandSource.suggestMatching(
                          PlayerSetting.data.get(context.getSource().getPlayer().getName().getString()).nick, builder))
                      .executes(context -> {
                        String name = StringArgumentType.getString(context, "name");
                        for (int i = 0; i < PlayerSetting.data
                            .get(context.getSource().getPlayer().getName().getString()).nick.size(); i++) {
                          if (PlayerSetting.data.get(context.getSource().getPlayer().getName().getString()).nick.get(i)
                              .equals(name)) {
                            PlayerSetting.data
                                .get(context.getSource().getPlayer().getName().getString()).nowUsingNick = i;
                            context.getSource().sendFeedback(new LiteralText("Set Nick To: " + name), false);
                            return 0;
                          }
                        }
                        context.getSource().sendFeedback(new LiteralText("Nick Not Found: " + name), false);
                        return 0;
                      })))
              .then(literal("clear").executes(context -> {
                PlayerSetting.data.get(context.getSource().getPlayer().getName().getString()).nowUsingNick = -1;
                context.getSource().sendFeedback(new LiteralText("Cleared Nick!"), false);
                return 0;
              }))));
    });
  }
}
