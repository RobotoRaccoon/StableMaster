package me.robotoraccoon.stablemaster.data;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.AbstractHorse;

import java.util.HashMap;
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
        setOwner(player.getUniqueId());
        horses = new HashMap<>();
    }

    /**
     * Get UUID of the owner
     * @return Owner's UUID
     */
    public UUID getOwner() {
        return owner;
    }

    /**
     * Set the owner
     * @param owner New owner's UUID
     */
    private void setOwner(UUID owner) {
        this.owner = owner;
    }

    /**
     * Get the stable map
     * @return Horses in the stable
     */
    public HashMap<String, StabledHorse> getHorses() {
        return horses;
    }

    /**
     * Set the stables map
     * @param horses Horses to overwrite existing ones with
     */
    public void setHorses(HashMap<String, StabledHorse> horses) {
        this.horses = horses;
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

}
