package net.nperkins.stablemaster.listeners;

import net.nperkins.stablemaster.StableMaster;
import net.nperkins.stablemaster.data.Stable;
import net.nperkins.stablemaster.data.StabledHorse;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class PlayerInteractEntityListener implements Listener {

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {

        final Entity entity = event.getRightClicked();
        if (entity == null)
            return; // Entity must exist.

        if (entity.getType() != EntityType.HORSE)
            return; // Entity must be a horse.

        final Player player = event.getPlayer();
        final Horse horse = (Horse) entity;

        if (!horse.isTamed())
            return; // Horse must be tamed to deny riders.

        // Fix if the horse is tamed but has no owner.
        if (horse.getOwner() == null)
            horse.setOwner(player);

        // Get horse details
        final Stable stable = StableMaster.getStable((OfflinePlayer) horse.getOwner());
        // Check in case it's a pre-owned horse not known about
        if (!stable.hasHorse(horse)) {
            stable.addHorse(horse);
        }

        // Cancel event if player is not allowed to ride the horse
        if (player != horse.getOwner() && !player.hasPermission("stablemaster.bypass.ride")) {
            StabledHorse stabledHorse = stable.getHorse(horse);
            if (!stabledHorse.isRider(player)) {
                StableMaster.langMessage(player, "error.not-rider");
                event.setCancelled(true);
            }
        }
    }
}
