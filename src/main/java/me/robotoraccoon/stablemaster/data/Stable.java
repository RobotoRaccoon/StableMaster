package me.robotoraccoon.stablemaster.data;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.AbstractHorse;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

/**
 * A players class to contain all their stabled horses
 * @author RobotoRaccoon
 */
public class Stable {

    /** UUID of who owns the stable */
    private UUID owner;
    /** The map of horses within the stable */
    private HashMap<String, StabledHorse> horses;

    /**
     * Constructor, create a stable for the given player
     * @param player Who to make the stable for
     */
    public Stable(OfflinePlayer player) {
        this.owner = player.getUniqueId();
        this.horses = new HashMap<>();
    }

    /**
     * Get UUID of the owner
     * @return Owner's UUID
     */
    public UUID getOwner() {
        return owner;
    }

    /**
     * Get UUIDs of all horses in the stable
     * @return Horses in the stable
     */
    public Set<String> getHorses() {
        return horses.keySet();
    }

    /**
     * Add a horse to the stable
     * @param horse Horse to add
     */
    public void addHorse(StabledHorse horse) {
        horses.put(horse.getUUID(), horse);
    }

    /**
     * Add a horse to the stable
     * @param horse Horse to add
     */
    public void addHorse(AbstractHorse horse) {
        horses.put(horse.getUniqueId().toString(), new StabledHorse(horse));
    }

    /**
     * Remove a horse from the stable
     * @param horse Horse to remove
     */
    public void removeHorse(AbstractHorse horse) {
        horses.remove(horse.getUniqueId().toString());
    }

    /**
     * Does this stable contain this horse
     * @param horse Horse to test
     * @return True if the stable has the horse
     */
    public boolean hasHorse(AbstractHorse horse) {
        return horses.containsKey(horse.getUniqueId().toString());
    }

    /**
     * Get the StabledHorse from AbstractHorse
     * @param horse Horse to get
     * @return StabledHorse cast of horse
     */
    public StabledHorse getHorse(AbstractHorse horse) {
        return horses.get(horse.getUniqueId().toString());
    }

    /**
     * Get a horse from it's UUID
     * @param horseID UUID of the horse
     * @return StabledHorse of the horse, or null if not found
     */
    public StabledHorse getHorse(String horseID) {
        return horses.get(horseID);
    }

    /**
     * Get the size of the stable
     * @return Number of horses in the stable
     */
    public int getSize() {
        return horses.size();
    }
}
