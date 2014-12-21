package net.nperkins.stablemaster.listeners;

import net.nperkins.stablemaster.data.Stable;
import net.nperkins.stablemaster.StableMaster;
import net.nperkins.stablemaster.data.StabledHorse;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;


public class EntityDamageByEntityListener implements Listener {

    final StableMaster plugin;

    public EntityDamageByEntityListener(StableMaster p) {
        this.plugin = p;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        // Check if a player fired this
        if (event.getDamager().getType() == EntityType.PLAYER) {
            Player player = (Player)event.getDamager();
            Entity entity = event.getEntity();
            // Is they punch a horse?
            if (event.getEntityType() == EntityType.HORSE) {
                Horse horse = (Horse) entity;
                // Horse has to be tamed to be owned
                if (!horse.isTamed()) {
                    return;
                }
                // Regardless, we want to cancel this event now
                event.setCancelled(true);

                Stable s = plugin.getStable(player);
                // Check in case it's a pre-owned horse not known about
                if (!s.hasHorse(horse)) {
                    s.addHorse(horse);
                }

                // Add riders
                if (plugin.addRiderQueue.containsKey(player)) {
                    OfflinePlayer rider = plugin.addRiderQueue.get(player);
                    StabledHorse sh = s.getHorse(horse);

                    if (player != horse.getOwner()) {
                        player.sendMessage("This is not your horse");
                        return;
                    }

                    if (sh.isRider(rider)) {
                        player.sendMessage("Already a rider!");
                    } else {
                        sh.addRider(rider);
                        player.sendMessage("Rider added!");
                    }
                    plugin.addRiderQueue.remove(player);
                    return;
                }

                // Remove riders
                if (plugin.delRiderQueue.containsKey(player)) {
                    OfflinePlayer rider = plugin.delRiderQueue.get(player);
                    StabledHorse sh = s.getHorse(horse);

                    if (player != horse.getOwner()) {
                        player.sendMessage("This is not your horse");
                        return;
                    }

                    if (!sh.isRider(rider)) {
                        player.sendMessage("Not currently a rider!");
                    } else {
                        sh.delRider(rider);
                        player.sendMessage("Rider removed!");
                    }
                    plugin.delRiderQueue.remove(player);
                    return;
                }

                // If we get here, the horse isn't involved in a command
                player.sendMessage("This horse is protected by the power of Mighty xrobau");
                return;
            }
        }

    }
}
