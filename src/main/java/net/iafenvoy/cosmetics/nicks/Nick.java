package net.iafenvoy.cosmetics.nicks;

import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;

public class Nick {
  public static void applyPreFix(ServerCommandSource source, String playername, String prefix) {
    Scoreboard scoreboard = source.getMinecraftServer().getScoreboard();
    if (scoreboard.getTeam(playername) != null) {
      scoreboard.getTeam(playername).setPrefix(new LiteralText(prefix));
    } else {
      Team team = scoreboard.addTeam(playername);
      team.setPrefix(new LiteralText(prefix));
      scoreboard.addPlayerToTeam(playername, team);
    }
  }

  public static void applySuffix(ServerCommandSource source, String playername, String suffix) {
    Scoreboard scoreboard = source.getMinecraftServer().getScoreboard();
    if (scoreboard.getTeam(playername) != null) {
      scoreboard.getTeam(playername).setSuffix(new LiteralText(suffix));
    } else {
      Team team = scoreboard.addTeam(playername);
      team.setSuffix(new LiteralText(suffix));
      scoreboard.addPlayerToTeam(playername, team);
    }
  }

  public static void clearPrefix(ServerCommandSource source, String playername) {
    Scoreboard scoreboard = source.getMinecraftServer().getScoreboard();
    if (scoreboard.getTeam(playername) != null) {
      scoreboard.getTeam(playername).setPrefix(new LiteralText(""));
    } else {
      Team team = scoreboard.addTeam(playername);
      team.setPrefix(new LiteralText(""));
      scoreboard.addPlayerToTeam(playername, team);
    }
  }

  public static void clearSuffix(ServerCommandSource source, String playername) {
    Scoreboard scoreboard = source.getMinecraftServer().getScoreboard();
    if (scoreboard.getTeam(playername) != null) {
      scoreboard.getTeam(playername).setSuffix(new LiteralText(""));
    } else {
      Team team = scoreboard.addTeam(playername);
      team.setSuffix(new LiteralText(""));
      scoreboard.addPlayerToTeam(playername, team);
    }
  }
}
