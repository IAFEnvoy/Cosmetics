package net.iafenvoy.cosmetics.particles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;

public class MultiParticleType {
  private static HashMap<String, MultiParticleType> data = new HashMap<>();
  public String name;
  public List<Particle> particles = new ArrayList<>();

  public MultiParticleType(JsonObject json) {
    name = json.get("name").getAsString();
    for (JsonElement ele : json.get("particles").getAsJsonArray())
      particles.add(new Particle(ele.getAsJsonObject()));
    data.put(name, this);
  }

  public static MultiParticleType getByName(String name) {
    return data.get(name);
  }

  public class Particle {
    public String type;
    public ParticleType particleType;
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
        particleType = ParticleTypes.DUST;
        r = (float) json.get("color").getAsJsonObject().get("r").getAsDouble();
        g = (float) json.get("color").getAsJsonObject().get("g").getAsDouble();
        b = (float) json.get("color").getAsJsonObject().get("b").getAsDouble();
        size = (float) json.get("color").getAsJsonObject().get("size").getAsDouble();
      } else if (type.equals("item")) {
        particleType = ParticleTypes.ITEM;
        item = json.get("item").getAsString();
      } else if (type.equals("block")) {
        particleType = ParticleTypes.BLOCK;
        block = json.get("block").getAsString();
      } else if (type.equals("falling_dust")) {
        particleType = ParticleTypes.FALLING_DUST;
        block = json.get("block").getAsString();
      } else
        throw (new IllegalArgumentException("illegal argument " + type + " in type"));
      pos = new Vec3d(json.get("pos").getAsJsonObject().get("x").getAsDouble(),
          json.get("pos").getAsJsonObject().get("y").getAsDouble(),
          json.get("pos").getAsJsonObject().get("z").getAsDouble());
      delta = new Vec3d(json.get("delta").getAsJsonObject().get("x").getAsDouble(),
          json.get("delta").getAsJsonObject().get("y").getAsDouble(),
          json.get("delta").getAsJsonObject().get("z").getAsDouble());
      speed = json.get("speed").getAsDouble();
      count = json.get("count").getAsInt();
      force = json.get("force").getAsBoolean();
    }
  }
}
