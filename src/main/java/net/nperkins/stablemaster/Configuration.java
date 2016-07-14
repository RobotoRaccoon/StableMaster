package net.nperkins.stablemaster;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Configuration {

    private File configFile;
    private FileConfiguration config;

    private File langFile;
    private FileConfiguration lang;

    public Configuration() {

        configFile = new File(StableMaster.getPlugin().getDataFolder(), "config.yml");
        config = YamlConfiguration.loadConfiguration(configFile);

        langFile = new File(StableMaster.getPlugin().getDataFolder(), "lang.yml");
        lang = YamlConfiguration.loadConfiguration(langFile);
    }

    public void createAllFiles() {
        if (!configFile.exists())
            StableMaster.getPlugin().saveResource("config.yml", true);

        if (!langFile.exists())
            StableMaster.getPlugin().saveResource("lang.yml", true);

        loadConfigs();
    }

    public boolean loadConfigs() {
        try {
            config = YamlConfiguration.loadConfiguration(new File(StableMaster.getPlugin().getDataFolder(), "config.yml"));
            lang   = YamlConfiguration.loadConfiguration(new File(StableMaster.getPlugin().getDataFolder(), "lang.yml"));
            StableMaster.getPlugin().reloadConfig();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean saveConfigs() {
        try {
            config.save(configFile);
            lang.save(langFile);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public FileConfiguration getLang() {
        return lang;
    }

}
