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
        if (horse.isTamed()) {
            if (player != horse.getOwner() && horse.getOwner() != null && !player.hasPermission("stablemaster.bypass")) {

                Stable stable = StableMaster.getStable((OfflinePlayer) horse.getOwner());
                if (!stable.hasHorse(horse)) {
                    stable.addHorse(horse);
                }

                StabledHorse stabledHorse = stable.getHorse(horse);
                if (!stabledHorse.isRider(player)) {
                    StableMaster.langMessage(player, "error.not-rider");
                    event.setCancelled(true);
                }

            }
        }
    }
}
