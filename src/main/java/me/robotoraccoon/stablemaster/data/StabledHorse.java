package me.robotoraccoon.stablemaster.data;

import me.robotoraccoon.stablemaster.StableMaster;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.AbstractHorse;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Store information about a stabled horse (or rideable mob) from the stable yml
 * @author RobotoRaccoon
 */
public class StabledHorse {

    /** UUID of the horse */
    private String uuid;
    /** Rider of this horse, by UUID */
    private List<String> riders;

    /**
     * Constructor, create from given UUID
     */
    public StabledHorse(String uuid) {
        this.uuid = uuid;
        this.riders = new ArrayList<>();
    }

    /**
     * Constructor, create from an existing horse
     * @param horse Horse to use
     */
    public StabledHorse(AbstractHorse horse) {
        this.uuid = horse.getUniqueId().toString();
        this.riders = new ArrayList<>();
    }

    /**
     * Get the UUID of the horse
     * @return UUID
     */
    public String getUUID() {
        return uuid;
    }

    /**
     * Get who the ride this horse
     * @return UUID of every rider
     */
    public List<String> getRiders() {
        return riders;
    }

    /**
     * Set the list of riders
     * @param riders List of UUIDs
     */
    public void setRiders(List<String> riders) {
        this.riders = riders;
    }

    /**
     * Get all riders by name
     * @return List of usernames
     */
    public List<String> getRiderNames() {
        ArrayList<String> riderNames = new ArrayList<>();
        for (String id : getRiders()) {
            OfflinePlayer rider = StableMaster.getPlugin().getServer().getOfflinePlayer(UUID.fromString(id));
            if (rider.getName() == null) {
                riderNames.add(rider.getUniqueId().toString());
            } else {
                riderNames.add(rider.getName());
            }
        }
        return riderNames;
    }

    /**
     * Add a player as a rider
     * @param player Player to add
     */
    public void addRider(OfflinePlayer player) {
        if (!isRider(player)) {
            riders.add(player.getUniqueId().toString());
        }
    }

    /**
     * Remove a player as a rider
     * @param player Player to remove
     */
    public void delRider(OfflinePlayer player) {
        riders.remove(player.getUniqueId().toString());
    }

    /**
     * Check if a player is a rider
     * @param player Player to check
     * @return True if player can ride this horse
     */
    public boolean isRider(OfflinePlayer player) {
        return riders.contains(player.getUniqueId().toString());
    }
}
