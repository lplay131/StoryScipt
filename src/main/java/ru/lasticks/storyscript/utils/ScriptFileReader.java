package ru.lasticks.storyscript.utils;

import ru.lasticks.storyscript.StoryScript;

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
                line = line.trim(); // Убираем начальные и конечные пробелы и символы перевода строки
                if (!line.isEmpty() && !line.startsWith("//")) { // Проверяем, не пустая ли строка и не комментарий ли это
                    scripts.add(line);
                }
            }
        } catch (IOException e) {
            StoryScript.LOGGER.error("The error with file reading: {0}", e);
        }
        return scripts;
    }
}
