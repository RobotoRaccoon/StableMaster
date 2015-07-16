package net.nperkins.stablemaster;

import net.nperkins.stablemaster.commands.CoreCommand;
import net.nperkins.stablemaster.data.Stable;
import net.nperkins.stablemaster.data.StabledHorse;
import net.nperkins.stablemaster.listeners.EntityDamageByEntityListener;
import net.nperkins.stablemaster.listeners.PlayerInteractEntityListener;
import net.nperkins.stablemaster.listeners.PlayerJoinListener;
import net.nperkins.stablemaster.listeners.PlayerQuitListener;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class StableMaster extends JavaPlugin {
    
    public static List<Chunk> horseChunk = new ArrayList<>();

    public ConcurrentHashMap<Player, OfflinePlayer> addRiderQueue = new ConcurrentHashMap<Player, OfflinePlayer>();
    public ConcurrentHashMap<Player, OfflinePlayer> delRiderQueue = new ConcurrentHashMap<Player, OfflinePlayer>();
    public ConcurrentHashMap<Player, OfflinePlayer> giveQueue = new ConcurrentHashMap<Player, OfflinePlayer>();
    public ConcurrentHashMap<Player, String> renameQueue = new ConcurrentHashMap<Player, String>();
    public ConcurrentHashMap<Player, Object> TeleportQueue = new ConcurrentHashMap<Player, Object>();
    public ArrayList<Player> infoQueue = new ArrayList<Player>();

    //private File configFile;
    protected File dataFolder;
    private File pluginFolder;
    private HashMap<OfflinePlayer, Stable> stables = new HashMap<OfflinePlayer, Stable>();


    public static String playerMessage(String msg) {
        // todo: remove hardcoding
        String prefix = "&3[&bSM&3] &f";
        return (ChatColor.translateAlternateColorCodes('&', prefix + msg));
    }

    @Override
    public void onEnable() {
        pluginFolder = getDataFolder();
        //configFile = new File(pluginFolder, "config.yml");
        dataFolder = new File(pluginFolder + File.separator + "stables");

        createDataFolders();

        // Register listeners
        getServer().getPluginManager().registerEvents(new EntityDamageByEntityListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractEntityListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);

        // Register commands
        this.getCommand("stablemaster").setExecutor(new CoreCommand(this));



        for (Player p : this.getServer().getOnlinePlayers()) {
            this.loadStable(p);
        }


    }

    @Override
    public void onDisable() {

        Iterator it = stables.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            this.saveStable((Stable) pairs.getValue());
            it.remove();
        }
    }

    public void loadStable(OfflinePlayer player) {
        File stableFile = new File(dataFolder + File.separator + player.getUniqueId().toString() + ".yml");
        YamlConfiguration yamlFile = YamlConfiguration.loadConfiguration(stableFile);
        Stable stable = new Stable(player);
        if (yamlFile.contains("horses")) {
            Set<String> horses = yamlFile.getConfigurationSection("horses").getKeys(false);
            HashMap<String, StabledHorse> horseMap = new HashMap<String, StabledHorse>();
            for (String s : horses) {
                StabledHorse horse = new StabledHorse();
                horse.setUniqueID(s);
                horse.setRiders(yamlFile.getStringList("horses." + s + ".riders"));
                horseMap.put(s, horse);
            }
            stable.setHorses(horseMap);
        }
        stables.put(player, stable);
    }

    public void saveStable(Stable stable) {
        File stableFile = new File(dataFolder + File.separator + stable.getOwner() + ".yml");
        if (stable.getHorses().isEmpty()) {
            if (stableFile.exists()) {
                stableFile.delete();
            }
            return;
        }
        YamlConfiguration yamlFile = YamlConfiguration.loadConfiguration(stableFile);
        yamlFile.set("owner", stable.getOwner());
        // Clear out horses current stable dat
        if (yamlFile.contains("horses")) {
            Set<String> horses = yamlFile.getConfigurationSection("horses").getKeys(false);
            for (String s : horses) {
                if (stable.getHorses().keySet().contains(s)) {
                    yamlFile.set("horses." + s + ".riders",null);
                } else {
                    yamlFile.set("horses." + s, null);
                }
            }
        }
        // Save new stable data
        for (String horse : stable.getHorses().keySet()) {
            StabledHorse sh = stable.getHorse(horse);
            yamlFile.set("horses." + horse + ".riders", sh.getRiders());
        }
        try {
            yamlFile.save(stableFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void unloadStable(OfflinePlayer player) {
        Stable s = this.stables.get(player);
        this.saveStable(s);
        stables.remove(player);
    }

    public Stable getStable(OfflinePlayer player) {
        if (!stables.containsKey(player)) {
            this.loadStable(player);
        }
        return stables.get(player);
    }

    private void createDataFolders() {
        if (!pluginFolder.exists()) {
            try {
                pluginFolder.mkdir();
            } catch (Exception e) {
                getLogger().info(e.getMessage());
            }
        }

        if (!dataFolder.exists()) {
            try {
                dataFolder.mkdir();
            } catch (Exception e) {
                getLogger().info(e.getMessage());
            }
        }
    }

}
