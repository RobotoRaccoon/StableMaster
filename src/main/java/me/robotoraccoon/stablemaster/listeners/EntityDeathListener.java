package me.robotoraccoon.stablemaster.listeners;

import me.robotoraccoon.stablemaster.StableUtil;
import me.robotoraccoon.stablemaster.data.Stable;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

/**
 * Listener to remove dead horses from stable files
 * @author RobotoRaccoon
 */
public class EntityDeathListener implements Listener {

    /**
     * Removes horse from the stable file when it dies
     * @param event Event
     */
    @EventHandler(ignoreCancelled = true)
    public void entityDeathEvent(EntityDeathEvent event) {
        // Only care about horses
        if (!(event.getEntity() instanceof AbstractHorse)) {
            return;
        }

        // Only care about ones with owners
        AbstractHorse horse = (AbstractHorse) event.getEntity();
        if (!horse.isTamed() || horse.getOwner() == null) {
            return;
        }

        // Remove it from the stable object
        OfflinePlayer owner = (OfflinePlayer) horse.getOwner();
        Stable stable = StableUtil.getStable(owner);
        stable.removeHorse(horse);
    }

}
