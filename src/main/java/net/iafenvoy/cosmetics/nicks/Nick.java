package net.iafenvoy.cosmetics.nicks;

import net.minecraft.server.command.ServerCommandSource;

public class Nick {
  public static void applyPreFix(ServerCommandSource source, String playername, String prefix) {
    source.getMinecraftServer().getCommandManager().execute(source.getMinecraftServer().getCommandSource(),
        "team add " + playername);
    source.getMinecraftServer().getCommandManager().execute(source.getMinecraftServer().getCommandSource(),
        "team join " + playername + " " + playername);
    source.getMinecraftServer().getCommandManager().execute(source.getMinecraftServer().getCommandSource(),
        "team modify " + playername + " prefix \"" + prefix + "\"");
  }

  public static void applySuffix(ServerCommandSource source, String playername, String suffix) {
    source.getMinecraftServer().getCommandManager().execute(source.getMinecraftServer().getCommandSource(),
        "team add " + playername);
    source.getMinecraftServer().getCommandManager().execute(source.getMinecraftServer().getCommandSource(),
        "team join " + playername + " " + playername);
    source.getMinecraftServer().getCommandManager().execute(source.getMinecraftServer().getCommandSource(),
        "team modify " + playername + " suffix \"" + suffix + "\"");
  }

  public static void clearPrefix(ServerCommandSource source, String playername) {
    source.getMinecraftServer().getCommandManager().execute(source.getMinecraftServer().getCommandSource(),
        "team option " + playername + " prefix \"\"");
  }

  public static void clearSuffix(ServerCommandSource source, String playername) {
    source.getMinecraftServer().getCommandManager().execute(source.getMinecraftServer().getCommandSource(),
        "team option " + playername + " suffix \"\"");
  }
}
