package net.iafenvoy.cosmetics.configs;

import java.util.HashMap;
import java.util.List;

import net.iafenvoy.cosmetics.particles.MultiParticleType;

public class PlayerSetting {
  public static HashMap<String, PlayerSetting> data = new HashMap<>();
  public String name;
  public List<String> particle, prefix, suffix;
  public int nowUsingParticle, nowUsingPrefix, nowUsingSuffix;

  public PlayerSetting(String name, List<String> particle, List<String> prefix, List<String> suffix,
      int nowUsingParticle,
      int nowUsingPrefix, int nowUsingSuffix) {
    this.name = name;
    this.particle = particle;
    this.prefix = prefix;
    this.suffix = suffix;
    this.nowUsingParticle = nowUsingParticle;
    this.nowUsingPrefix = nowUsingPrefix;
    this.nowUsingSuffix = nowUsingSuffix;
    data.put(name, this);
  }

  public String getName() {
    return this.name;
  }

  public MultiParticleType getParticle() {
    return nowUsingParticle < 0 || nowUsingParticle >= this.particle.size() ? null
        : MultiParticleType.getByName(this.particle.get(nowUsingParticle));
  }

  public String getPrefix() {
    return nowUsingPrefix < 0 || nowUsingPrefix >= this.prefix.size() ? ""
        : this.prefix.get(nowUsingPrefix);
  }

  public String getSuffix() {
    return nowUsingSuffix < 0 || nowUsingSuffix >= this.suffix.size() ? ""
        : this.suffix.get(nowUsingSuffix);
  }

  public String toString() {
    return "PlayerSetting{name=" + name + ", particle=" + particle + ", prefix=" + prefix + ", suffix="
        + suffix + ", nowUsingParticle=" + nowUsingParticle + ", nowUsingPrefix=" + nowUsingPrefix
        + ", nowUsingSuffix=" + nowUsingSuffix + "}";
  }
}
