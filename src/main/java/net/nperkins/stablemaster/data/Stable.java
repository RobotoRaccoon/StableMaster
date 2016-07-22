package net.nperkins.stablemaster.data;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Horse;
import java.util.HashMap;
import java.util.UUID;

public class Stable {

    private UUID owner;
    private HashMap<String, StabledHorse> horses;

    public Stable() {
        setOwner(null);
        horses = new HashMap<>();
    }

    public Stable(OfflinePlayer player) {
        setOwner(player.getUniqueId());
        horses = new HashMap<>();
    }

    public UUID getOwner() {
        return owner;
    }

    protected void setOwner(UUID owner) {
        this.owner = owner;
    }

    public HashMap<String, StabledHorse> getHorses() {
        return horses;
    }

    public void setHorses(HashMap<String, StabledHorse> horses) {
        this.horses = horses;
    }

    public void addHorse(Horse horse) {
        horses.put(horse.getUniqueId().toString(), new StabledHorse(horse));
    }

    public void removeHorse(Horse horse) {
        horses.remove(horse.getUniqueId().toString());
    }

    public boolean hasHorse(Horse horse) {
        return horses.containsKey(horse.getUniqueId().toString());
    }

    public StabledHorse getHorse(Horse horse) {
        return horses.get(horse.getUniqueId().toString());
    }

    public StabledHorse getHorse(String horseID) {
        return horses.get(horseID);
    }

}
