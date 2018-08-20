package me.robotoraccoon.stablemaster.listeners;

import me.robotoraccoon.stablemaster.LangString;
import me.robotoraccoon.stablemaster.StableUtil;
import me.robotoraccoon.stablemaster.data.Stable;
import me.robotoraccoon.stablemaster.data.StabledHorse;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

/**
 * Listener to handle a player trying to ride a horse
 * @author RobotoRaccoon
 */
public class PlayerInteractEntityListener implements Listener {

    /**
     * Event where a player right-clicks an animal
     * @param event Event
     */
    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {

        final Entity entity = event.getRightClicked();
        if (entity == null)
            return; // Entity must exist.

        if (!(entity instanceof AbstractHorse))
            return; // Entity must be a horse.

        final Player player = event.getPlayer();
        final AbstractHorse horse = (AbstractHorse) entity;

        if (!horse.isTamed())
            return; // Horse must be tamed to deny riders.

        // Fix if the horse is tamed but has no owner.
        if (horse.getOwner() == null)
            horse.setOwner(player);

        // Get horse details
        final Stable stable = StableUtil.getStable((OfflinePlayer) horse.getOwner());
        // Check in case it's a pre-owned horse not known about
        if (!stable.hasHorse(horse)) {
            stable.addHorse(horse);
        }

        // Cancel event if player is not allowed to ride the horse
        if (player != horse.getOwner() && !player.hasPermission("stablemaster.bypass.ride")) {
            StabledHorse stabledHorse = stable.getHorse(horse);
            if (!stabledHorse.isRider(player)) {
                new LangString("error.not-rider").send(player);
                event.setCancelled(true);
            }
        }
    }
}
