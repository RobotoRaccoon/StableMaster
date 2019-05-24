package me.robotoraccoon.stablemaster;

import me.robotoraccoon.stablemaster.commands.CoreCommand;
import me.robotoraccoon.stablemaster.data.Stable;
import me.robotoraccoon.stablemaster.listeners.*;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Map;

/**
 * Main StableMaster class to initialise and destroy the plugin
 * @author RobotoRaccoon
 */
public class StableMaster extends JavaPlugin {

    /** This plugin */
    private static Plugin plugin;
    /** The root file-path */
    private static File pluginFolder;
    /** The file-path for stables */
    private static File stablesFolder;

    /**
     * Called once when the plugin is instantiated
     */
    @Override
    public void onEnable() {
        plugin = this;
        pluginFolder = getDataFolder();
        stablesFolder = new File(pluginFolder + File.separator + "stables");
        createDataFolders();

        // Load in config data
        StableUtil.loadConfigData();

        // Register listeners
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new EntityDamageListeners(), this);
        pm.registerEvents(new EntityDeathListener(), this);
        pm.registerEvents(new EntityTameListener(), this);
        pm.registerEvents(new PlayerInteractEntityListener(), this);
        pm.registerEvents(new PlayerJoinListener(), this);
        pm.registerEvents(new PlayerQuitListener(), this);

        // Register commands
        this.getCommand("stablemaster").setExecutor(new CoreCommand());

        // Load all stables for players already online
        for (Player p : this.getServer().getOnlinePlayers()) {
            StableUtil.loadStable(p);
        }
    }

    /**
     * Always called when shutting down the plugin
     */
    @Override
    public void onDisable() {
        for (Map.Entry<OfflinePlayer, Stable> entry : StableUtil.getStables().entrySet()) {
            StableUtil.saveStable(entry.getValue());
        }
    }

    /**
     * Make sure the folders exist
     */
    private void createDataFolders() {
        if (!pluginFolder.exists()) {
            pluginFolder.mkdir();
        }
        if (!stablesFolder.exists()) {
            stablesFolder.mkdir();
        }
    }

    /**
     * Get this plugin
     * @return this
     */
    public static Plugin getPlugin() {
        return plugin;
    }

    /**
     * Get the stables directory
     * @return Stables folder
     */
    public static File getStablesFolder() {
        return stablesFolder;
    }
}
