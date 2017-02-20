package net.nperkins.stablemaster.listeners;

import net.nperkins.stablemaster.StableMaster;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTameEvent;

public class EntityTameListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onEntityTame(EntityTameEvent event) {
        // Return if the damaged entity is not a tameable entity.
        if (!(event.getEntity() instanceof Tameable))
            return;

        Player player = (Player) event.getOwner();
        Animals animal = (Animals) event.getEntity();
        String name = animal.getType().name().toLowerCase();

        // Don't cancel if the player has the appropriate permission
        if (player.hasPermission("stablemaster.tame." + name))
            return;

        event.setCancelled(true);
        StableMaster.langMessage(player, "error.cannot-tame");
    }

}
