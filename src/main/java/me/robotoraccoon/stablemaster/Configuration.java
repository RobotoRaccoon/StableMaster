package me.robotoraccoon.stablemaster;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Configuration {

    private static File configFile;
    private static FileConfiguration config;
    private static File langFile;
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

    public static void loadAllConfigs() {
        config = YamlConfiguration.loadConfiguration(configFile);
        lang = YamlConfiguration.loadConfiguration(langFile);
        StableMaster.getPlugin().reloadConfig();
    }

    public static FileConfiguration getConfig() {
        return config;
    }

    public static FileConfiguration getLang() {
        return lang;
    }
}
