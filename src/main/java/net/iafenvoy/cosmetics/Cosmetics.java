package net.iafenvoy.cosmetics;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.Environment;
import net.iafenvoy.cosmetics.commands.AdminsCommands;
import net.iafenvoy.cosmetics.commands.PlayersCommands;
import net.iafenvoy.cosmetics.configs.ConfigsLoader;
import net.iafenvoy.cosmetics.configs.CosmeticsOPs;
import net.iafenvoy.cosmetics.particles.MultiParticleType;
import net.fabricmc.api.EnvType;

@Environment(EnvType.SERVER)
public class Cosmetics implements DedicatedServerModInitializer {
  public static final String MOD_ID = "cosmetics";

  @Override
  public void onInitializeServer() {
    ConfigsLoader.loadConfig();
    CosmeticsOPs.load();

    AdminsCommands.registryCommands();
    PlayersCommands.registryCommands();

    MultiParticleType.loadData();
  }

}
