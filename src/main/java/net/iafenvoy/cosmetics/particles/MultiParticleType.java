package net.iafenvoy.cosmetics.particles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.server.MinecraftServer;

public class MultiParticleType {
  private static Logger logger = LogManager.getLogger();
  private static HashMap<String, MultiParticleType> data = new HashMap<>();
  public final JsonObject json;
  public String name;
  public List<String> particlesCommand = new ArrayList<>();
  public int timeDelta = -1;
  public int flag = 0, listFlag = 0;

  public MultiParticleType(JsonObject json) {
    this.json = json;
    name = json.get("name").getAsString();
    for (JsonElement ele : json.get("particles").getAsJsonArray())
      particlesCommand.add(ele.getAsString());
    if (json.has("timeDelta"))
      timeDelta = json.get("timeDelta").getAsInt();
    data.put(name, this);
  }

  public static MultiParticleType getByName(String name) {
    return data.get(name);
  }

  public void summon(MinecraftServer server, String playerName) {
    flag++;
    if (flag >= timeDelta) {
      flag = 0;
      listFlag++;
      if (listFlag >= particlesCommand.size())
        listFlag = 0;
      String command = "execute as " + playerName + " at @s run " + particlesCommand.get(listFlag);
      server.getCommandManager().execute(server.getCommandSource(), command);
    }
  }

  public static void loadData() {
    logger.info("Loading MultiParticleType data...");
    File file = new File("./particles/");
    File[] tempList = file.listFiles();

    for (int i = 0; i < tempList.length; i++) {
      if (tempList[i].isFile()) {
        String fileName = tempList[i].getName();
        if (fileName.endsWith(".json")) {
          JsonObject json = loadJson(tempList[i]);
          if (json != null)
            try {
              new MultiParticleType(json);
            } catch (Exception e) {
              e.printStackTrace();
            }
        }
      }
      if (tempList[i].isDirectory()) {
        // 这里就不递归了
      }
    }
    logger.info("Loaded MultiParticleType data.");
  }

  public static void saveData() {
    logger.info("Saving MultiParticleType data...");
    File file = new File("./particles/");
    File[] tempList = file.listFiles();
    for (int i = 0; i < tempList.length; i++) {
      if (tempList[i].isFile()) {
        String fileName = tempList[i].getName();
        if (fileName.endsWith(".json")) {
          try {
            tempList[i].delete();
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
      if (tempList[i].isDirectory()) {
        // 这里就不递归了
      }
    }
    for (MultiParticleType particle : data.values()) {
      try {
        FileWriter fw = new FileWriter("./particles/" + particle.name + ".json", false);
        fw.write(particle.json.toString());
        fw.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    logger.info("Saved MultiParticleType data.");
  }

  private static JsonObject loadJson(File file) {
    try {
      FileReader fr = new FileReader(file);
      BufferedReader bf = new BufferedReader(fr);
      String data = "", line;
      while ((line = bf.readLine()) != null)
        data += line;
      bf.close();
      return new JsonParser().parse(data).getAsJsonObject();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static void remove(String name) {
    data.remove(name);
  }

  public static Collection<MultiParticleType> getAll() {
    return data.values();
  }
}
