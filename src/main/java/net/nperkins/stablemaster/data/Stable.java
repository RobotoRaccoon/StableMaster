package net.nperkins.stablemaster.data;


import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Horse;

import java.util.HashMap;


public class Stable {

    private String owner;
    private HashMap<String, StabledHorse> horses;

    public Stable() {
        this.owner = null;
        horses = new HashMap<String, StabledHorse>();
    }

    public Stable(OfflinePlayer pr) {
        this.owner = pr.getUniqueId().toString();
        horses = new HashMap<String, StabledHorse>();
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public HashMap<String, StabledHorse> getHorses() {
        return horses;
    }

    public void setHorses(HashMap<String, StabledHorse> horses) {
        this.horses = horses;
    }

    public void addHorse(Horse horse) {
        this.horses.put(horse.getUniqueId().toString(), new StabledHorse(horse));
    }

    public void removeHorse(Horse horse) { this.horses.remove(horse.getUniqueId().toString()); }

    public boolean hasHorse(Horse horse) {
        return this.horses.containsKey(horse.getUniqueId().toString());
    }

    public StabledHorse getHorse(Horse horse) {
        return this.horses.get(horse.getUniqueId().toString());
    }

    public StabledHorse getHorse(String horseID) {
        return this.horses.get(horseID);
    }

}
