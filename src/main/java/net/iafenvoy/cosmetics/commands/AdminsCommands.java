package net.iafenvoy.cosmetics.commands;

import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.iafenvoy.cosmetics.Cosmetics;
import net.iafenvoy.cosmetics.configs.ConfigsLoader;
import net.iafenvoy.cosmetics.configs.CosmeticsOPs;
import net.iafenvoy.cosmetics.configs.PlayerSetting;
import net.minecraft.text.LiteralText;
import static net.minecraft.server.command.CommandManager.*;

import java.util.ArrayList;

import com.mojang.brigadier.arguments.StringArgumentType;

public class AdminsCommands {
  public static void registryCommands() {
    CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
      dispatcher.register(literal(Cosmetics.MOD_ID)
          .requires((player) -> CosmeticsOPs.ops.contains(player.getName()))
          .then(literal("reload").executes(context -> {
            ConfigsLoader.loadConfig();
            return 0;
          }))
          .then(literal("setting")
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
                    return 0;
                  }))
                  .then(literal("addNick").then(argument("nick", StringArgumentType.word()).executes(context -> {
                    String name = StringArgumentType.getString(context, "playername");
                    String nick = StringArgumentType.getString(context, "nick");
                    if (PlayerSetting.data.containsKey(name)) {
                      PlayerSetting.data.get(name).nick.add(nick);
                      context.getSource().sendFeedback(new LiteralText("Nick Added!"), false);
                    } else
                      context.getSource().sendFeedback(new LiteralText("Player Profile Not Found!"), false);
                    return 0;
                  })))
                  .then(literal("removeNick").then(argument("nick", StringArgumentType.word()).executes(context -> {
                    String name = StringArgumentType.getString(context, "playername");
                    String nick = StringArgumentType.getString(context, "nick");
                    if (PlayerSetting.data.containsKey(name)) {
                      PlayerSetting.data.get(name).nick.remove(nick);
                      context.getSource().sendFeedback(new LiteralText("Nick Removed!"), false);
                    } else
                      context.getSource().sendFeedback(new LiteralText("Player Profile Not Found!"), false);
                    return 0;
                  }))))));
    });
  }
}
