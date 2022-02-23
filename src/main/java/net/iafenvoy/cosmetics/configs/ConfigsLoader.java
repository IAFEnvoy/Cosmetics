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
        List<String> prefixs = new ArrayList<>();
        for (JsonElement prefix : jsonElement.getAsJsonObject().get("prefix").getAsJsonArray())
          prefixs.add(prefix.getAsString());
        int nowUsingPrefix = jsonElement.getAsJsonObject().get("nowUsingPrefix").getAsInt();
        List<String> suffixs = new ArrayList<>();
        for (JsonElement suffix : jsonElement.getAsJsonObject().get("suffix").getAsJsonArray())
          suffixs.add(suffix.getAsString());
        int nowUsingSuffix = jsonElement.getAsJsonObject().get("nowUsingSuffix").getAsInt();
        new PlayerSetting(name, particles, prefixs, suffixs, nowUsingParticle, nowUsingPrefix, nowUsingSuffix);
      }
      logger.info("Succeeded to load config");
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
        JsonArray prefixs = new JsonArray();
        for (String prefix : setting.prefix)
          prefixs.add(prefix);
        JsonArray suffixs = new JsonArray();
        for (String suffix : setting.suffix)
          suffixs.add(suffix);
        System.out.println("{\"name\":\"" + setting.getName() + "\",\"particle\":" + particles.toString()
            + ",\"nowUsingParticle\":" + setting.nowUsingParticle + ",\"prefix\":" + prefixs.toString()
            + ",\"nowUsingPrefix\":" + setting.nowUsingPrefix + ",\"suffix\":" + suffixs.toString()
            + "\"nowUsingSuffix\":" + setting.nowUsingSuffix + "}");
        json.add(new JsonParser().parse(
            "{\"name\":\"" + setting.getName() + "\",\"particle\":" + particles.toString()
                + ",\"nowUsingParticle\":" + setting.nowUsingParticle + ",\"prefix\":" + prefixs.toString()
                + ",\"nowUsingPrefix\":" + setting.nowUsingPrefix + ",\"suffix\":" + suffixs.toString()
                + ",\"nowUsingSuffix\":" + setting.nowUsingSuffix + "}"));
      }
      BufferedWriter bw = new BufferedWriter(new FileWriter(file_path));
      bw.write(json.toString());
      bw.close();
      logger.info("Succeeded to save config");
    } catch (Exception e) {
      logger.error("Failed to save config", e);
    }

  }
}
