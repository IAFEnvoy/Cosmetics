package net.iafenvoy.cosmetics.commands;

import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.iafenvoy.cosmetics.Cosmetics;
import net.iafenvoy.cosmetics.configs.ConfigsLoader;
import net.iafenvoy.cosmetics.configs.CosmeticsOPs;
import net.iafenvoy.cosmetics.configs.PlayerSetting;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import static net.minecraft.server.command.CommandManager.*;

import java.util.ArrayList;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public class AdminsCommands {
  public static void registryCommands() {
    CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
      dispatcher.register(literal(Cosmetics.MOD_ID)
          .then(literal("reload")
              .requires(AdminsCommands::isOP)
              .executes(context -> {
                ConfigsLoader.loadConfig();
                return 0;
              }))
          .then(literal("setting")
              .requires(AdminsCommands::isOP)
              .then(argument("playername", StringArgumentType.word())
                  .then(literal("list").executes(context -> {
                    String name = StringArgumentType.getString(context, "playername");
                    if (PlayerSetting.data.containsKey(name))
                      context.getSource().sendFeedback(new LiteralText(PlayerSetting.data.get(name).toString()), false);
                    else
                      context.getSource().sendFeedback(new LiteralText("Player Profile Not Found!"), false);
                    return 0;
                  }))
                  .then(literal("new").executes(context -> {
                    String name = StringArgumentType.getString(context, "playername");
                    PlayerSetting.data.put(name, new PlayerSetting(name, new ArrayList<>(), new ArrayList<>(), -1, -1));
                    context.getSource().sendFeedback(new LiteralText("Successfully Create Nick List!"), false);
                    ConfigsLoader.saveConfig();
                    return 0;
                  }))
                  .then(
                      literal("addNick").then(argument("nick", StringArgumentType.greedyString()).executes(context -> {
                        String name = StringArgumentType.getString(context, "playername");
                        String nick = StringArgumentType.getString(context, "nick");
                        if (PlayerSetting.data.containsKey(name)) {
                          if (!nick.contains("%s"))
                            nick = nick + "%s";
                          PlayerSetting.data.get(name).nick.add(nick.replace("$", "§"));
                          context.getSource().sendFeedback(new LiteralText("Nick Added!"), false);
                          ConfigsLoader.saveConfig();
                        } else
                          context.getSource().sendFeedback(new LiteralText("Player Profile Not Found!"), false);
                        return 0;
                      })))
                  .then(literal("removeNick")
                      .then(argument("nick", StringArgumentType.greedyString()).executes(context -> {
                        String name = StringArgumentType.getString(context, "playername");
                        String nick = StringArgumentType.getString(context, "nick");
                        if (PlayerSetting.data.containsKey(name)) {
                          PlayerSetting.data.get(name).nick.remove(nick.replace("$", "§"));
                          context.getSource().sendFeedback(new LiteralText("Nick Removed!"), false);
                          ConfigsLoader.saveConfig();
                        } else
                          context.getSource().sendFeedback(new LiteralText("Player Profile Not Found!"), false);
                        return 0;
                      }))))));
    });
  }

  public static boolean isOP(ServerCommandSource player) {
    try {
      return CosmeticsOPs.ops.contains(player.getPlayer().getName().getString().toLowerCase());
    } catch (CommandSyntaxException e) {
      e.printStackTrace();
      return false;
    }
  }
}
