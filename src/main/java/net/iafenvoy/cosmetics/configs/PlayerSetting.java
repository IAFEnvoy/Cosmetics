package net.iafenvoy.cosmetics.configs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.iafenvoy.cosmetics.particles.MultiParticleType;

public class PlayerSetting {
  public static HashMap<String, PlayerSetting> data = new HashMap<>();
  public String name;
  public List<String> particle;
  public List<String> nick = new ArrayList<>();
  public int nowUsingParticle = -1, nowUsingNick = -1;

  public PlayerSetting(String name, List<String> particle, List<String> nick, int nowUsingParticle,
      int nowUsingNick) {
    this.name = name;
    this.particle = particle;
    this.nick = nick;
    this.nowUsingParticle = nowUsingParticle;
    this.nowUsingNick = nowUsingNick;
    data.put(name, this);
  }

  public String getName() {
    return this.name;
  }

  public MultiParticleType getParticle() {
    return nowUsingParticle == -1 ? null : MultiParticleType.getByName(this.particle.get(nowUsingParticle));
  }

  public String getNick() {
    return nowUsingNick == -1 ? "" : this.nick.get(nowUsingNick);
  }

  public String toString() {
    return "PlayerSetting{name=" + name + ", particle=" + particle + ", nick=" + nick + ", nowUsingParticle="
        + nowUsingParticle + ", nowUsingNick=" + nowUsingNick + "}";
  }
}
