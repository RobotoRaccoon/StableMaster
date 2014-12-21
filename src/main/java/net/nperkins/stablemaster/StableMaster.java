package net.nperkins.stablemaster;

import net.nperkins.stablemaster.data.Stable;
import net.nperkins.stablemaster.commands.*;
import net.nperkins.stablemaster.data.StabledHorse;
import net.nperkins.stablemaster.listeners.*;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class StableMaster extends JavaPlugin {

    private File pluginFolder;
    //private File configFile;
    protected File dataFolder;
    private HashMap<OfflinePlayer, Stable> stables = new HashMap<OfflinePlayer, Stable>();
    public ConcurrentHashMap<Player, OfflinePlayer> addRiderQueue = new ConcurrentHashMap<Player, OfflinePlayer>();
    public ConcurrentHashMap<Player, OfflinePlayer> delRiderQueue = new ConcurrentHashMap<Player, OfflinePlayer>();

    @Override
    public void onEnable() {
        pluginFolder = getDataFolder();
        //configFile = new File(pluginFolder, "config.yml");
        dataFolder = new File(pluginFolder + "/stables");

        createDataFolders();

        // Register listeners
        getServer().getPluginManager().registerEvents(new EntityDamageByEntityListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractEntityListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);

        // Register commands
        this.getCommand("smaddrider").setExecutor(new AddRider(this));
        this.getCommand("smdelrider").setExecutor(new DelRider(this));

        for(Player p : this.getServer().getOnlinePlayers()) {
            this.loadStable(p);
        }


    }

    @Override
    public void onDisable() {

        Iterator it = stables.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            this.saveStable((Stable)pairs.getValue());
            it.remove();
        }
    }

    public void loadStable(OfflinePlayer p) {
        File f = new File(dataFolder + File.separator + p.getUniqueId().toString() + ".yml");
        YamlConfiguration yamlFile = YamlConfiguration.loadConfiguration(f);
        Stable s = new Stable(p);
        if (yamlFile.contains("horses")) {
            Set<String> horses = yamlFile.getConfigurationSection("horses").getKeys(false);
            HashMap<String, StabledHorse> horseMap = new HashMap<String, StabledHorse>();
            for (String h : horses) {
                StabledHorse horse = new StabledHorse();
                horse.setUniqueID(h);
                horse.setRiders(yamlFile.getStringList("horses." + h + ".riders"));
                horseMap.put(h, horse);
            }
            s.setHorses(horseMap);
        }
        stables.put(p, s);
    }

    public void saveStable(Stable s) {
        File f = new File(dataFolder + File.separator + s.getOwner() + ".yml");
        YamlConfiguration yamlFile = YamlConfiguration.loadConfiguration(f);
        yamlFile.set("owner",s.getOwner());
        for (String horse : s.getHorses().keySet()) {
            StabledHorse sh = s.getHorse(horse);
            yamlFile.set("horses." + horse + ".riders",sh.getRiders());
        }
        //yamlFile.set("horses",s.getHorses());
        try {
            yamlFile.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void unloadStable(OfflinePlayer p) {
        Stable s = this.stables.get(p);
        this.saveStable(s);
        stables.remove(p);
    }


    public Stable getStable(OfflinePlayer p) {
        if (!stables.containsKey(p)) {
            this.loadStable(p);
        }
        return stables.get(p);
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
