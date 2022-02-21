package net.iafenvoy.cosmetics.commands;

import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.iafenvoy.cosmetics.Cosmetics;
import net.iafenvoy.cosmetics.configs.ConfigsLoader;
import net.iafenvoy.cosmetics.configs.PlayerSetting;
import net.minecraft.text.LiteralText;
import static net.minecraft.server.command.CommandManager.*;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public class PlayersCommands {
  public static void registryCommands() {
    CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
      dispatcher.register(literal(Cosmetics.MOD_ID)
          .then(literal("nick")
              .requires((player) -> {
                try {
                  return PlayerSetting.data.containsKey(player.getPlayer().getName().getString());
                } catch (CommandSyntaxException e) {
                  e.printStackTrace();
                }
                return false;
              })
              .then(literal("useByID")
                  .then(argument("name", StringArgumentType.word())
                      .executes((context) -> {
                        String name = StringArgumentType.getString(context, "name");
                        if (PlayerSetting.data.containsKey(context.getSource().getPlayer().getName().getString()))
                          PlayerSetting.data
                              .get(context.getSource().getPlayer().getName().getString()).nowUsingNick = Integer
                                  .parseInt(name);
                        else
                          context.getSource().sendFeedback(new LiteralText("Player Profile Not Found!"), false);
                        return 0;
                      })))
              .then(literal("use")
                  .then(argument("name", StringArgumentType.greedyString())
                      .suggests((context, builder) -> {
                        String name = context.getSource().getPlayer().getName().getString();
                        if (PlayerSetting.data.containsKey(name))
                          for (String nick : PlayerSetting.data.get(name).nick)
                            builder.suggest(nick.replace("§", "$"));
                        return builder.buildFuture();
                      })
                      .executes(context -> {
                        String name = StringArgumentType.getString(context, "name");
                        for (int i = 0; i < PlayerSetting.data
                            .get(context.getSource().getPlayer().getName().getString()).nick.size(); i++) {
                          if (PlayerSetting.data.get(context.getSource().getPlayer().getName().getString()).nick.get(i)
                              .replace("§", "$").equals(name)) {
                            PlayerSetting.data
                                .get(context.getSource().getPlayer().getName().getString()).nowUsingNick = i;
                            context.getSource().sendFeedback(new LiteralText("Set Nick To: " + name.replace("$", "§")),
                                false);
                            
                            ConfigsLoader.saveConfig();
                            return 0;
                          }
                        }
                        context.getSource().sendFeedback(new LiteralText("Nick Not Found: " + name.replace("$", "§")),
                            false);
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
