package net.iafenvoy.cosmetics.configs;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.iafenvoy.cosmetics.utils.FileUtils;

public class CosmeticsOPs {
  public static String ops_file_path = "./CosmeticsOPs.txt";
  public static Logger logger = LogManager.getLogger();
  public static List<String> ops = new ArrayList<>();

  public static void load() {
    ops.clear();
    try {
      List<String> lines = FileUtils.readByLines(ops_file_path);
      for (String line : lines) {
        if (line.startsWith("#"))
          continue;
        ops.add(line);
      }
    } catch (Exception e) {
      logger.error("Error loading ops file", e);
    }
  }

  public static void save() {
    try {
      FileUtils.saveByLines(ops_file_path, ops);
    } catch (Exception e) {
      logger.error("Error saving ops file", e);
    }
  }
}
