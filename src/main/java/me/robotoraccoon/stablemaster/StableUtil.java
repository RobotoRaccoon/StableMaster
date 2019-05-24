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

/**
 * External utilities class for StableMaster
 * @author RobotoRaccoon
 */
public class StableUtil {

    /** Map of stables for every online player */
    private static HashMap<OfflinePlayer, Stable> stables = new HashMap<>();

    /**
     * Get the name of an animal
     * @param type What the entity is
     * @return The translated animal name
     */
    public static String getAnimal(EntityType type) {
        return new LangString("animal." + type.toString()).getMessage();
    }

    /**
     * Load the stable for the given player
     * @param player Who the load the stable for
     */
    public static void loadStable(OfflinePlayer player) {
        File stableFile = new File(StableMaster.getStablesFolder() + File.separator + player.getUniqueId().toString() + ".yml");
        YamlConfiguration yamlFile = YamlConfiguration.loadConfiguration(stableFile);
        Stable stable = new Stable(player);

        // If the player has horses, load them too
        if (yamlFile.contains("horses")) {
            Set<String> horses = yamlFile.getConfigurationSection("horses").getKeys(false);
            for (String uuid : horses) {
                StabledHorse horse = new StabledHorse(uuid);
                horse.setRiders(yamlFile.getStringList("horses." + uuid + ".riders"));
                stable.addHorse(horse);
            }
        }
        stables.put(player, stable);
    }

    /**
     * Save the stable back to file
     * @param stable Which stable to save
     */
    public static void saveStable(Stable stable) {
        File stableFile = new File(StableMaster.getStablesFolder() + File.separator + stable.getOwner().toString() + ".yml");
        // If the stable has no horses, delete the file and return
        if (stable.getSize() == 0) {
            if (stableFile.exists()) {
                stableFile.delete();
            }
            return;
        }

        YamlConfiguration yamlFile = YamlConfiguration.loadConfiguration(stableFile);
        yamlFile.set("owner", stable.getOwner().toString());
        // Clear out horses current stable yml
        if (yamlFile.contains("horses")) {
            Set<String> horses = yamlFile.getConfigurationSection("horses").getKeys(false);
            for (String uuid : horses) {
                if (stable.getHorses().contains(uuid)) {
                    yamlFile.set("horses." + uuid + ".riders", null);
                } else {
                    yamlFile.set("horses." + uuid, null);
                }
            }
        }
        // Save new stable data
        for (String uuid : stable.getHorses()) {
            StabledHorse sh = stable.getHorse(uuid);
            yamlFile.set("horses." + uuid + ".riders", sh.getRiders());
        }
        try {
            yamlFile.save(stableFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Unload the stable for the given player
     * @param player Player to unload for
     */
    public static void unloadStable(OfflinePlayer player) {
        Stable s = stables.get(player);
        saveStable(s);
        stables.remove(player);
    }

    /**
     * Get the stable for the given player
     * @param player Player to load for
     * @return The stable
     */
    public static Stable getStable(OfflinePlayer player) {
        if (!stables.containsKey(player)) {
            loadStable(player);
        }
        return stables.get(player);
    }

    /**
     * Get an offline player for a given username (Not UUID)
     * @param name The name to lookup
     * @return Offline player, null if not found
     */
    public static OfflinePlayer getOfflinePlayer(String name) {
        OfflinePlayer player = StableMaster.getPlugin().getServer().getOfflinePlayer(name);
        if (player != null) {
            if (!player.hasPlayedBefore() && !player.isOnline()) {
                player = null;
            }
        }
        return player;
    }

    /**
     * Reloads the configuration files and sub-commands
     */
    public static void loadConfigData() {
        Configuration.loadAllConfigs();
        CoreCommand.addAllCommands();
    }

    /**
     * Get the stables HashMap
     * @return Every loaded stable
     */
    public static HashMap<OfflinePlayer, Stable> getStables() {
        return stables;
    }
}
