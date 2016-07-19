package net.nperkins.stablemaster.data;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Horse;
import java.util.ArrayList;
import java.util.List;

public class StabledHorse {

    private String uniqueID;
    private List<String> riders;

    public StabledHorse() {
        uniqueID = null;
        riders = new ArrayList<>();
    }

    public StabledHorse(Horse h) {
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
