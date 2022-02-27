package net.iafenvoy.cosmetics.particles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.server.MinecraftServer;

public class MultiParticleType {
  private static HashMap<String, MultiParticleType> data = new HashMap<>();
  public String name;
  public List<String> particlesCommand = new ArrayList<>();
  public int timeDelta = -1;
  public int flag = 0;

  public MultiParticleType(JsonObject json) {
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
    String command = "execute as " + playerName + " at @s run " + particlesCommand.get(0);
    server.getCommandManager().execute(server.getCommandSource(), command);
  }

  public static void loadData() {
    File file = new File("./particles/");
    File[] tempList = file.listFiles();

    for (int i = 0; i < tempList.length; i++) {
      if (tempList[i].isFile()) {
        String fileName = tempList[i].getName();
        if (fileName.endsWith(".json")) {
          JsonObject json = loadJson(tempList[i]);
          if (json != null)
            new MultiParticleType(json);
        }
      }
      if (tempList[i].isDirectory()) {
        // 这里就不递归了
      }
    }
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
}
