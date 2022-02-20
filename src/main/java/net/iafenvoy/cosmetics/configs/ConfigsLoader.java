package net.iafenvoy.cosmetics.configs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConfigsLoader {
  public static String file_path = "./CosmeticsConfig.json";
  public static Logger logger = LogManager.getLogger();

  public static void loadConfig() {
    try {
      PlayerSetting.data.clear();
      logger.info("Start loading cosmetics configs");
      FileReader fr = new FileReader(file_path);
      BufferedReader bf = new BufferedReader(fr);
      String data = "";
      String line;
      while ((line = bf.readLine()) != null)
        data += line;
      bf.close();
      JsonArray json = new JsonParser().parse(data).getAsJsonArray();
      for (JsonElement jsonElement : json) {
        String name = jsonElement.getAsJsonObject().get("name").getAsString();
        List<String> particles = new ArrayList<>();
        for (JsonElement particle : jsonElement.getAsJsonObject().get("particle").getAsJsonArray())
          particles.add(particle.getAsString());
        int nowUsingParticle = jsonElement.getAsJsonObject().get("nowUsingParticle").getAsInt();
        List<String> nicks = new ArrayList<>();
        for (JsonElement nick : jsonElement.getAsJsonObject().get("nick").getAsJsonArray())
          nicks.add(nick.getAsString());
        int nowUsingNick = jsonElement.getAsJsonObject().get("nowUsingNick").getAsInt();
        new PlayerSetting(name, particles, nicks, nowUsingParticle, nowUsingNick);
        logger.info("Succeeded to load config");
      }
    } catch (FileNotFoundException e) {
      logger.warn("Config file not found, generating one.", e);
    } catch (Exception e) {
      logger.error("Failed to load config", e);
    }
  }

  public static void saveConfig() {
    logger.info("Start saving cosmetics configs");
    try {
      JsonArray json = new JsonArray();
      for (PlayerSetting setting : PlayerSetting.data.values()) {
        JsonArray particles = new JsonArray();
        for (String particle : setting.particle)
          particles.add(particle);
        JsonArray nicks = new JsonArray();
        for (String nick : setting.nick)
          nicks.add(nick);
        json.add(new JsonParser().parse(
            "{\"name\":\"" + setting.getName() + "\",\"particle\":\"" + particles.toString()
                + "\",\"nowUsingParticle\":\"" + setting.nowUsingParticle + "\",\"nick\":\"" + nicks.toString()
                + "\",\"nowUsingNick\":\"" + setting.nowUsingNick + "\"}"));
      }
      BufferedWriter bw = new BufferedWriter(new FileWriter(file_path));
      bw.write(json.toString());
      bw.close();
    } catch (Exception e) {
      logger.error("Failed to save config", e);
    }
    logger.info("Succeeded to save config");

  }
}
