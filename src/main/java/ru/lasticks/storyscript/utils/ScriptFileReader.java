package ru.lasticks.storyscript.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.nio.charset.StandardCharsets;

public class ScriptFileReader {
    public static List<String> readScriptsFromFile(String filePath) {
        List<String> scripts = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    scripts.add(line.trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scripts;
    }
}