package me.robotoraccoon.stablemaster;

import me.robotoraccoon.stablemaster.commands.CoreCommand;
import me.robotoraccoon.stablemaster.data.Stable;
import me.robotoraccoon.stablemaster.data.StabledHorse;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

public class StableUtil {

    private static Configuration configuration;
    private static HashMap<OfflinePlayer, Stable> stables = new HashMap<>();

    public static String getAnimal(EntityType type) {
        return new LangString("animal." + type.toString().toLowerCase()).getMessage();
    }

    public static void loadStable(OfflinePlayer player) {
        File stableFile = new File(StableMaster.getStablesFolder() + File.separator + player.getUniqueId().toString() + ".yml");
        YamlConfiguration yamlFile = YamlConfiguration.loadConfiguration(stableFile);
        Stable stable = new Stable(player);

        if (yamlFile.contains("horses")) {
            Set<String> horses = yamlFile.getConfigurationSection("horses").getKeys(false);
            HashMap<String, StabledHorse> horseMap = new HashMap<>();
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

    public static void saveStable(Stable stable) {
        File stableFile = new File(StableMaster.getStablesFolder() + File.separator + stable.getOwner().toString() + ".yml");
        if (stable.getHorses().isEmpty()) {
            if (stableFile.exists()) {
                stableFile.delete();
            }
            return;
        }
        YamlConfiguration yamlFile = YamlConfiguration.loadConfiguration(stableFile);
        yamlFile.set("owner", stable.getOwner().toString());
        // Clear out horses current stable dat
        if (yamlFile.contains("horses")) {
            Set<String> horses = yamlFile.getConfigurationSection("horses").getKeys(false);
            for (String s : horses) {
                if (stable.getHorses().keySet().contains(s)) {
                    yamlFile.set("horses." + s + ".riders", null);
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

    public static void unloadStable(OfflinePlayer player) {
        Stable s = stables.get(player);
        saveStable(s);
        stables.remove(player);
    }

    public static Stable getStable(OfflinePlayer player) {
        if (!stables.containsKey(player)) {
            loadStable(player);
        }
        return stables.get(player);
    }

    public static void loadConfigData() {
        if (configuration == null) {
            configuration = new Configuration();
        }
        configuration.createAllFiles();
        CoreCommand.addAllCommands();
    }

    public static Configuration getConfiguration() {
        return configuration;
    }

    public static HashMap<OfflinePlayer, Stable> getStables() {
        return stables;
    }
}
