package net.iafenvoy.cosmetics.particles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.item.ItemStack;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;

public class MultiParticleType {
  private static HashMap<String, MultiParticleType> data = new HashMap<>();
  public String name;
  public List<Particle> particles = new ArrayList<>();
  public int timeDelta = -1;
  public int flag = 0;

  public MultiParticleType(JsonObject json) {
    name = json.get("name").getAsString();
    for (JsonElement ele : json.get("particles").getAsJsonArray())
      particles.add(new Particle(ele.getAsJsonObject()));
    if (json.has("timeDelta"))
      timeDelta = json.get("timeDelta").getAsInt();
    data.put(name, this);
  }

  public static MultiParticleType getByName(String name) {
    return data.get(name);
  }

  public class Particle {
    public String type;
    public String particleType;
    public String block;
    public String item;
    public float r, g, b, size;
    public Vec3d pos, delta;
    public double speed;
    public int count;
    public boolean force;

    public Particle(JsonObject json) {
      type = json.get("type").getAsString();
      if (type.equals("dust")) {
        particleType = "DUST";
        r = (float) json.get("color").getAsJsonArray().get(0).getAsFloat();
        g = (float) json.get("color").getAsJsonArray().get(1).getAsFloat();
        b = (float) json.get("color").getAsJsonArray().get(2).getAsFloat();
        size = (float) json.get("color").getAsJsonArray().get(3).getAsFloat();
      } else if (type.equals("item")) {
        particleType = "ITEM";
        item = json.get("item").getAsString();
      } else if (type.equals("block")) {
        particleType = "BLOCK";
        block = json.get("block").getAsString();
      } else if (type.equals("falling_dust")) {
        particleType = "FALLING_DUST";
        block = json.get("block").getAsString();
      } else
        throw (new IllegalArgumentException("illegal argument " + type + " in type"));
      pos = new Vec3d(json.get("pos").getAsJsonArray().get(0).getAsDouble(),
          json.get("pos").getAsJsonArray().get(1).getAsDouble(),
          json.get("pos").getAsJsonArray().get(2).getAsDouble());
      delta = new Vec3d(json.get("delta").getAsJsonArray().get(0).getAsDouble(),
          json.get("delta").getAsJsonArray().get(1).getAsDouble(),
          json.get("delta").getAsJsonArray().get(2).getAsDouble());
      speed = json.get("speed").getAsDouble();
      count = json.get("count").getAsInt();
      force = json.get("force").getAsBoolean();
    }

    public void summon(ServerPlayerEntity player) {
      try {
        switch (particleType) {
          case "DUST": {
            player.getServerWorld().spawnParticles(new DustParticleEffect(r, g, b, size), pos.x, pos.y, pos.z, count,
                delta.x, delta.y, delta.z, speed);
            break;
          }
          case "ITEM": {
            player.getServerWorld().spawnParticles(
                new ItemStackParticleEffect(ParticleTypes.ITEM,
                    new ItemStack(Registry.ITEM.get(new Identifier(item.split(":")[0], item.split(":")[1])))),
                pos.x, pos.y, pos.z, count, delta.x, delta.y, delta.z, speed);
            break;
          }
          case "BLOCK": {
            player.getServerWorld().spawnParticles(
                new BlockStateParticleEffect(ParticleTypes.BLOCK,
                    Registry.BLOCK.get(new Identifier(block.split(":")[0], block.split(":")[1])).getDefaultState()),
                pos.x, pos.y, pos.z, count, delta.x, delta.y, delta.z, speed);
            break;
          }
          case "FALLING_DUST": {
            player.getServerWorld().spawnParticles(
                new BlockStateParticleEffect(ParticleTypes.FALLING_DUST,
                    Registry.BLOCK.get(new Identifier(block.split(":")[0], block.split(":")[1])).getDefaultState()),
                pos.x, pos.y, pos.z, count, delta.x, delta.y, delta.z, speed);
            break;
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
