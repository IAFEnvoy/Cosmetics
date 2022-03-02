package net.iafenvoy.cosmetics.commands;

import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.iafenvoy.cosmetics.Cosmetics;
import net.iafenvoy.cosmetics.configs.ConfigsLoader;
import net.iafenvoy.cosmetics.configs.PlayerSetting;
import net.iafenvoy.cosmetics.nicks.Nick;
import net.minecraft.text.LiteralText;
import static net.minecraft.server.command.CommandManager.*;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public class PlayersCommands {
  public static void registryCommands() {
    CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
      dispatcher.register(literal(Cosmetics.MOD_ID)
          .then(literal("particle")
              .requires((player) -> {
                try {
                  return PlayerSetting.data.containsKey(player.getPlayer().getName().getString());
                } catch (CommandSyntaxException e) {
                  e.printStackTrace();
                }
                return false;
              })
              .then(literal("clear").executes((context) -> {
                PlayerSetting.data.get(context.getSource().getPlayer().getName().getString()).nowUsingParticle = -1;
                return 0;
              }))
              .then(literal("useByID")
                  .then(argument("id", IntegerArgumentType.integer())
                      .executes((context) -> {
                        try {
                          PlayerSetting.data
                              .get(context.getSource().getPlayer().getName()
                                  .getString()).nowUsingParticle = IntegerArgumentType.getInteger(context, "id");
                          ConfigsLoader.saveConfig();
                          return 0;
                        } catch (Exception e) {
                          return 1;
                        }
                      })))
              .then(literal("use")
                  .then(argument("name", StringArgumentType.greedyString())
                      .suggests((context, builder) -> {
                        String name = context.getSource().getPlayer().getName().getString();
                        if (PlayerSetting.data.containsKey(name))
                          for (String particle : PlayerSetting.data.get(name).particle)
                            builder.suggest(particle);
                        return builder.buildFuture();
                      })
                      .executes((context) -> {
                        try {
                          PlayerSetting.data.get(context.getSource().getPlayer().getName()
                              .getString()).nowUsingParticle = PlayerSetting.data
                                  .get(context.getSource().getPlayer().getName().getString()).particle
                                      .indexOf(StringArgumentType.getString(context, "name"));
                          ConfigsLoader.saveConfig();
                          return 0;
                        } catch (Exception e) {
                          return 1;
                        }
                      }))))
          .then(literal("prefix")
              .requires((player) -> {
                try {
                  return PlayerSetting.data.containsKey(player.getPlayer().getName().getString());
                } catch (CommandSyntaxException e) {
                  e.printStackTrace();
                }
                return false;
              })
              .then(literal("useByID")
                  .then(argument("id", IntegerArgumentType.integer())
                      .executes((context) -> {
                        try {
                          if (PlayerSetting.data.containsKey(context.getSource().getPlayer().getName().getString())) {
                            PlayerSetting.data.get(context.getSource().getPlayer().getName()
                                .getString()).nowUsingPrefix = IntegerArgumentType.getInteger(context, "id");
                            Nick.applyPreFix(context.getSource(), context.getSource().getPlayer().getName().getString(),
                                PlayerSetting.data.get(context.getSource().getPlayer().getName().getString())
                                    .getPrefix());
                          } else
                            context.getSource().sendFeedback(new LiteralText("Player Profile Not Found!"), false);
                          return 0;
                        } catch (NumberFormatException e) {
                          e.printStackTrace();
                          return 1;
                        }
                      })))
              .then(literal("use")
                  .then(argument("name", StringArgumentType.greedyString())
                      .suggests((context, builder) -> {
                        String name = context.getSource().getPlayer().getName().getString();
                        if (PlayerSetting.data.containsKey(name))
                          for (String prefix : PlayerSetting.data.get(name).prefix)
                            builder.suggest(prefix.replace("§", "$"));
                        return builder.buildFuture();
                      })
                      .executes(context -> {
                        String name = StringArgumentType.getString(context, "name");
                        for (int i = 0; i < PlayerSetting.data
                            .get(context.getSource().getPlayer().getName().getString()).prefix.size(); i++) {
                          if (PlayerSetting.data.get(context.getSource().getPlayer().getName().getString()).prefix
                              .get(i).replace("§", "$").equals(name)) {
                            PlayerSetting.data
                                .get(context.getSource().getPlayer().getName().getString()).nowUsingPrefix = i;
                            Nick.applyPreFix(context.getSource(), context.getSource().getPlayer().getName().getString(),
                                PlayerSetting.data.get(context.getSource().getPlayer().getName().getString())
                                    .getPrefix());
                            context.getSource().sendFeedback(
                                new LiteralText("Set Prefix To: " + name.replace("$", "§")),
                                false);
                            ConfigsLoader.saveConfig();
                            return 0;
                          }
                        }
                        context.getSource().sendFeedback(new LiteralText("Prefix Not Found: " + name.replace("$", "§")),
                            false);
                        return 0;
                      })))
              .then(literal("clear").executes(context -> {
                PlayerSetting.data.get(context.getSource().getPlayer().getName().getString()).nowUsingPrefix = -1;
                Nick.clearPrefix(context.getSource(), context.getSource().getPlayer().getName().getString());
                context.getSource().sendFeedback(new LiteralText("Cleared Prefix!"), false);
                return 0;
              })))
          .then(literal("suffix").requires((player) -> {
            try {
              return PlayerSetting.data.containsKey(player.getPlayer().getName().getString());
            } catch (CommandSyntaxException e) {
              e.printStackTrace();
            }
            return false;
          })
              .then(literal("useByID")
                  .then(argument("id", IntegerArgumentType.integer())
                      .executes((context) -> {
                        try {
                          if (PlayerSetting.data.containsKey(context.getSource().getPlayer().getName().getString())) {
                            PlayerSetting.data.get(context.getSource().getPlayer().getName()
                                .getString()).nowUsingSuffix = IntegerArgumentType.getInteger(context, "id");
                            Nick.applySuffix(context.getSource(), context.getSource().getPlayer().getName().getString(),
                                PlayerSetting.data.get(context.getSource().getPlayer().getName().getString())
                                    .getSuffix());
                          } else
                            context.getSource().sendFeedback(new LiteralText("Player Profile Not Found!"), false);
                          return 0;
                        } catch (NumberFormatException e) {
                          e.printStackTrace();
                          return 1;
                        }
                      })))
              .then(literal("use")
                  .then(argument("name", StringArgumentType.greedyString())
                      .suggests((context, builder) -> {
                        String name = context.getSource().getPlayer().getName().getString();
                        if (PlayerSetting.data.containsKey(name))
                          for (String suffix : PlayerSetting.data.get(name).suffix)
                            builder.suggest(suffix.replace("§", "$"));
                        return builder.buildFuture();
                      })
                      .executes(context -> {
                        String name = StringArgumentType.getString(context, "name");
                        for (int i = 0; i < PlayerSetting.data
                            .get(context.getSource().getPlayer().getName().getString()).suffix.size(); i++) {
                          if (PlayerSetting.data.get(context.getSource().getPlayer().getName().getString()).suffix
                              .get(i)
                              .replace("§", "$").equals(name)) {
                            PlayerSetting.data
                                .get(context.getSource().getPlayer().getName().getString()).nowUsingSuffix = i;
                            Nick.applySuffix(context.getSource(), context.getSource().getPlayer().getName().getString(),
                                PlayerSetting.data.get(context.getSource().getPlayer().getName().getString())
                                    .getSuffix());
                            context.getSource().sendFeedback(
                                new LiteralText("Set Suffix To: " + name.replace("$", "§")),
                                false);

                            ConfigsLoader.saveConfig();
                            return 0;
                          }
                        }
                        context.getSource().sendFeedback(new LiteralText("Suffix Not Found: " + name.replace("$", "§")),
                            false);
                        return 0;
                      })))
              .then(literal("clear").executes(context -> {
                PlayerSetting.data.get(context.getSource().getPlayer().getName().getString()).nowUsingSuffix = -1;
                Nick.clearSuffix(context.getSource(), context.getSource().getPlayer().getName().getString());
                context.getSource().sendFeedback(new LiteralText("Cleared Suffix!"), false);
                return 0;
              }))));
    });
  }
}
