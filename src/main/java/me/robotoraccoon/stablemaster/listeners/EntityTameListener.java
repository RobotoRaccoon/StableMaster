package me.robotoraccoon.stablemaster.listeners;

import me.robotoraccoon.stablemaster.LangString;
import me.robotoraccoon.stablemaster.StableUtil;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTameEvent;

/**
 * Listener to permit a player to tame an animal (or not)
 * @author RobotoRaccoon
 */
public class EntityTameListener implements Listener {

    /**
     * Event where a player tames an animal
     * @param event Event
     */
    @EventHandler(ignoreCancelled = true)
    public void onEntityTame(EntityTameEvent event) {
        // Return if the entity is not a tameable entity.
        if (!(event.getEntity() instanceof Tameable)) {
            return;
        }

        Player player = (Player) event.getOwner();
        Animals animal = (Animals) event.getEntity();
        String name = animal.getType().name().toLowerCase();

        // Cancel if the player does not have the appropriate permission
        if (!player.hasPermission("stablemaster.tame." + name)) {
            event.setCancelled(true);
            new LangString("error.cannot-tame", StableUtil.getAnimal(animal.getType())).send(player);
        }
    }
}
