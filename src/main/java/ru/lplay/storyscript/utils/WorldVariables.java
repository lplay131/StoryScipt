package ru.lplay.storyscript.utils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class WorldVariables {
    private final File file;
    private final Map<String, String> variables = new HashMap<>();

    public WorldVariables(File file) {
        this.file = file;
        loadVariables();
    }

    private void loadVariables() {
        if (!file.exists()) {
            return;
        }

        try (InputStream input = new FileInputStream(file)) {
            Properties prop = new Properties();
            prop.load(input);
            for (String key : prop.stringPropertyNames()) {
                variables.put(key, prop.getProperty(key));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void saveVariables() {
        try (OutputStream output = new FileOutputStream(file)) {
            Properties prop = new Properties();
            for (Map.Entry<String, String> entry : variables.entrySet()) {
                prop.setProperty(entry.getKey(), entry.getValue());
            }
            prop.store(output, null);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public String getVariable(String key) {
        return variables.getOrDefault(key, "");
    }

    public void setVariable(String key, String value) {
        variables.put(key, value);
        saveVariables();
    }
}
