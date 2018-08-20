package me.robotoraccoon.stablemaster.data;

import me.robotoraccoon.stablemaster.StableMaster;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.AbstractHorse;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * Store information about a stabled horse (or rideable mob) from the stable yml
 * @author RobotoRaccoon
 */
public class StabledHorse {

    /** UUID of the horse */
    private String uniqueID;
    /** Rider of this horse, by UUID */
    private List<String> riders;

    /**
     * Default constructor, create an empty StabledHorse
     */
    public StabledHorse() {
        uniqueID = null;
        riders = new ArrayList<>();
    }

    /**
     * Constructor, create from an existing horse
     * @param h Horse to use
     */
    public StabledHorse(AbstractHorse h) {
        uniqueID = h.getUniqueId().toString();
        riders = new ArrayList<>();
    }

    /**
     * Get the UUID of the horse
     * @return UUID
     */
    public String getUniqueID() {
        return uniqueID;
    }

    /**
     * Set the horses UUID
     * @param uniqueID UUID
     */
    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
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
        Iterator it = getRiders().iterator();
        while (it.hasNext()) {
            OfflinePlayer rider = StableMaster.getPlugin().getServer().getOfflinePlayer(UUID.fromString((String) it.next()));
            if (rider.getName() == null) {
                //todo: some sort of lookup
                riderNames.add(rider.getUniqueId().toString());
            } else {
                riderNames.add(rider.getName());
            }
        }
        return riderNames;
    }

    /**
     * Add a player as a rider
     * @param p Player to add
     */
    public void addRider(OfflinePlayer p) {
        if (!riders.contains(p.getUniqueId().toString()))
            riders.add(p.getUniqueId().toString());
    }

    /**
     * Remove a player as a rider
     * @param p Player to remove
     */
    public void delRider(OfflinePlayer p) {
        if (riders.contains(p.getUniqueId().toString()))
            riders.remove(p.getUniqueId().toString());
    }

    /**
     * Check if a player is a rider
     * @param p Player to check
     * @return True if player can ride this horse
     */
    public boolean isRider(OfflinePlayer p) {
        return riders.contains(p.getUniqueId().toString());
    }

}
