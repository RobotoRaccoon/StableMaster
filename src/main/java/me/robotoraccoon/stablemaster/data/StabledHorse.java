package me.robotoraccoon.stablemaster.data;

import me.robotoraccoon.stablemaster.StableMaster;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.AbstractHorse;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class StabledHorse {

    private String uniqueID;
    private List<String> riders;

    public StabledHorse() {
        uniqueID = null;
        riders = new ArrayList<>();
    }

    public StabledHorse(AbstractHorse h) {
        uniqueID = h.getUniqueId().toString();
        riders = new ArrayList<>();
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public List<String> getRiders() {
        return riders;
    }

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

    public void setRiders(List<String> riders) {
        this.riders = riders;
    }

    public void addRider(OfflinePlayer p) {
        if (!riders.contains(p.getUniqueId().toString()))
            riders.add(p.getUniqueId().toString());
    }

    public void delRider(OfflinePlayer p) {
        if (riders.contains(p.getUniqueId().toString()))
            riders.remove(p.getUniqueId().toString());
    }

    public boolean isRider(OfflinePlayer p) {
        return riders.contains(p.getUniqueId().toString());
    }

}
