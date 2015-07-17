package net.nperkins.stablemaster;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Configuration {

    private StableMaster plugin;

    //private File configFile;
    //private FileConfiguration config;

    private File langFile;
    private FileConfiguration lang;

    public Configuration(StableMaster plugin) {
        this.plugin = plugin;

        //configFile = new File(plugin.getDataFolder(), "config.yml");
        //config = YamlConfiguration.loadConfiguration(configFile);

        langFile = new File(plugin.getDataFolder(), "lang.yml");
        lang = YamlConfiguration.loadConfiguration(langFile);
    }

    public void createAllFiles() {
        //if (!configFile.exists())
        //    plugin.saveResource("config.yml", true);

        if (!langFile.exists())
            plugin.saveResource("lang.yml", true);

        loadConfigs();
    }

    public boolean loadConfigs() {
        try {
            //config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "config.yml"));
            lang = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "lang.yml"));
            plugin.reloadConfig();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean saveConfigs() {
        try {
            //config.save(configFile);
            lang.save(langFile);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //public FileConfiguration getConfig() {
    //    return config;
    //}

    public FileConfiguration getLang() {
        return lang;
    }

}
