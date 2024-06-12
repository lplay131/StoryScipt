package ru.lasticks.storyscript.variables;

import ru.lasticks.storyscript.StoryScript;

import java.io.*;
import java.nio.charset.StandardCharsets;
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
            StoryScript.LOGGER.error("File {} does not exist", file.getAbsolutePath());
            return;
        }

        try (InputStream input = new FileInputStream(file);
             Reader reader = new InputStreamReader(input, StandardCharsets.UTF_8)) {
            Properties prop = new Properties();
            prop.load(reader);
            for (String key : prop.stringPropertyNames()) {
                String value = prop.getProperty(key);
                variables.put(key, value);
                StoryScript.LOGGER.info("Loaded variable: {} = {}", key, value);
            }
        } catch (IOException ex) {
            StoryScript.LOGGER.error("Error loading variables from {}: {}", file.getAbsolutePath(), ex.getMessage());
        }
    }

    public void saveVariables() {
        try (OutputStream output = new FileOutputStream(file);
             Writer writer = new OutputStreamWriter(output, StandardCharsets.UTF_8)) {
            Properties prop = new Properties();
            for (Map.Entry<String, String> entry : variables.entrySet()) {
                prop.setProperty(entry.getKey(), entry.getValue());
            }
            prop.store(writer, null);
        } catch (IOException io) {
            StoryScript.LOGGER.error("Error with save variables: ", io);
        }
    }

    public String getVariable(String key) {
        String value = variables.get(key);
        if (value == null) {
            StoryScript.LOGGER.error("Variable {} is not set.", key);
            return "ERROR: Variable is not set. Please set a variable.";
        }
        return value;
    }

    public void setVariable(String key, String value) {
        variables.put(key, value);
        saveVariables();
    }
}
