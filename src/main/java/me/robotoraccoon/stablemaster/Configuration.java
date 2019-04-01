package me.robotoraccoon.stablemaster;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStreamReader;

/**
 * Load and get configuration sections from .yml files within the plugin dir
 * @author RobotoRaccoon
 */
public class Configuration {

    /** Name of the config file */
    private static final String CONFIG = "config.yml";
    /** Name of the lang file */
    private static final String LANG = "lang.yml";

    /** config.yml */
    private static File configFile;
    /** Contents of config.yml */
    private static YamlConfiguration config;
    /** lang.yml */
    private static File langFile;
    /** Contents of lang.yml */
    private static YamlConfiguration lang;

    static {
        // Save default configFile if it doesn't exist
        configFile = new File(StableMaster.getPlugin().getDataFolder(), CONFIG);
        if (!configFile.exists())
            StableMaster.getPlugin().saveResource(CONFIG, true);

        // Save default langFile if it doesn't exist
        langFile = new File(StableMaster.getPlugin().getDataFolder(), LANG);
        if (!langFile.exists())
            StableMaster.getPlugin().saveResource(LANG, true);
    }

    /**
     * Reload the configuration files
     */
    public static void loadAllConfigs() {
        config = YamlConfiguration.loadConfiguration(configFile);
        addMissingEntries(CONFIG, config);

        lang = YamlConfiguration.loadConfiguration(langFile);
        addMissingEntries(LANG, lang);

        StableMaster.getPlugin().reloadConfig();
    }

    /**
     * Check for missing entries in the existing config in the plugin directory and add them if they are missing
     * @param resource The name of the file
     * @param existing The existing configuration
     */
    private static void addMissingEntries(String resource, YamlConfiguration existing) {
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(
                new InputStreamReader(StableMaster.getPlugin().getResource(resource)));

        addMissingEntriesRecursion("", resource, existing, configuration);
    }

    /**
     * Recursive method for addMissingEntries
     * @param parentKey The full path of the parent key
     * @param resource The name of the file
     * @param existing The configuration section of the existing config
     * @param packaged The configuration section of the config packed with the plugin jar
     */
    private static void addMissingEntriesRecursion(String parentKey, String resource, ConfigurationSection existing, ConfigurationSection packaged) {
        // Loop over all the children of the current section (shallow search)
        for (String shortKey : packaged.getKeys(false)) {
            String key = parentKey.isEmpty() ? shortKey : parentKey + "." + shortKey;

            // If the key is a configuration section, we want to recurse back into this function at that section
            if (packaged.isConfigurationSection(shortKey)) {
                if (!existing.isConfigurationSection(shortKey)) {
                    existing.createSection(shortKey);
                }
                addMissingEntriesRecursion(key, resource, existing.getConfigurationSection(shortKey), packaged.getConfigurationSection(shortKey));
            }
            // Else, we want to check if the value is set in the existing config
            else if (!existing.contains(shortKey)) {
                // Key is a leaf node, and the existing config does not contain the value
                existing.set(shortKey, packaged.get(shortKey));
                // Check if we're to alert about missing paths. Default to true if even this config option is missing
                if (config.getBoolean("generic.alert-missing-paths", true)) {
                    StableMaster.getPlugin().getLogger().warning(String.format("%s path `%s` does not exist, using default value", resource, key));
                }
            }
        }
    }

    /**
     * Get the contents of config.yml
     * @return config.yml
     */
    public static FileConfiguration getConfig() {
        return config;
    }

    /**
     * Get the contents of lang.yml
     * @return lang.yml
     */
    public static FileConfiguration getLang() {
        return lang;
    }
}
