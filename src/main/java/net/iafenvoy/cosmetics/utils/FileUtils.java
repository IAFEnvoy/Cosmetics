package net.iafenvoy.cosmetics.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

  public static List<String> readByLines(String filepath) throws IOException {
    List<String> lines = new ArrayList<>();
    FileReader fr = new FileReader(filepath);
    BufferedReader bf = new BufferedReader(fr);
    String str;
    // 按行读取字符串
    while ((str = bf.readLine()) != null)
      lines.add(str.toLowerCase());
    bf.close();
    fr.close();
    return lines;
  }

  public static void saveByLines(String filepath, List<String> data) throws IOException {
    FileWriter fw = new FileWriter(filepath);
    BufferedWriter bw = new BufferedWriter(fw);
    for (String line : data) {
      bw.write(line);
      bw.newLine();
    }
    bw.close();
    fw.close();
  }
}
