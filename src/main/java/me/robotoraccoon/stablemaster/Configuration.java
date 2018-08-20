package me.robotoraccoon.stablemaster;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * Load and get configuration sections from .yml files within the plugin dir
 * @author RobotoRaccoon
 */
public class Configuration {

    /** config.yml */
    private static File configFile;
    /** Contents of config.yml */
    private static FileConfiguration config;
    /** lang.yml */
    private static File langFile;
    /** Contents of lang.yml */
    private static FileConfiguration lang;

    static {
        // Save default configFile if it doesn't exist
        configFile = new File(StableMaster.getPlugin().getDataFolder(), "config.yml");
        if (!configFile.exists())
            StableMaster.getPlugin().saveResource("config.yml", true);

        // Save default langFile if it doesn't exist
        langFile = new File(StableMaster.getPlugin().getDataFolder(), "lang.yml");
        if (!langFile.exists())
            StableMaster.getPlugin().saveResource("lang.yml", true);
    }

    /**
     * Reload the configuration files
     */
    public static void loadAllConfigs() {
        config = YamlConfiguration.loadConfiguration(configFile);
        lang = YamlConfiguration.loadConfiguration(langFile);
        StableMaster.getPlugin().reloadConfig();
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
