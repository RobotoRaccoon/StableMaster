package me.robotoraccoon.stablemaster;

import me.robotoraccoon.stablemaster.commands.CoreCommand;
import me.robotoraccoon.stablemaster.commands.SubCommand;
import me.robotoraccoon.stablemaster.data.Stable;
import me.robotoraccoon.stablemaster.listeners.*;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StableMaster extends JavaPlugin {

    private static Plugin plugin;
    private static File stablesFolder;
    private static File pluginFolder;

    private static List<Chunk> teleportChunk = new ArrayList<>();

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
        pm.registerEvents(new ChunkListener(), this);
        pm.registerEvents(new EntityDamageListeners(), this);
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

    @Override
    public void onDisable() {
        for (Map.Entry<OfflinePlayer, Stable> entry : StableUtil.getStables().entrySet()) {
            StableUtil.saveStable(entry.getValue());
        }
    }

    private void createDataFolders() {
        if (!pluginFolder.exists()) {
            pluginFolder.mkdir();
        }

        if (!stablesFolder.exists()) {
            stablesFolder.mkdir();
        }
    }

    public static Plugin getPlugin() {
        return plugin;
    }

    public static File getStablesFolder() {
        return stablesFolder;
    }

    public static List<Chunk> getTeleportChunk() {
        return teleportChunk;
    }

}
