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
import org.bukkit.event.entity.EntityDamageByEntityEvent;


public class EntityDamageByEntityListener implements Listener {

    final StableMaster plugin;

    public EntityDamageByEntityListener(StableMaster plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        // Check if a player fired this
        if (event.getDamager().getType() == EntityType.PLAYER) {
            Player player = (Player) event.getDamager();
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

                Stable stable = plugin.getStable(player);
                // Check in case it's a pre-owned horse not known about
                if (!stable.hasHorse(horse)) {
                    stable.addHorse(horse);
                }

                // Add riders
                if (plugin.addRiderQueue.containsKey(player)) {
                    OfflinePlayer rider = plugin.addRiderQueue.get(player);
                    StabledHorse sh = stable.getHorse(horse);

                    if (player != horse.getOwner()) {
                        player.sendMessage(StableMaster.playerMessage("This is not your horse!"));
                        return;
                    }

                    if (sh.isRider(rider)) {
                        player.sendMessage(StableMaster.playerMessage(rider.getName() + "is already a rider!"));
                    } else {
                        sh.addRider(rider);
                        player.sendMessage(StableMaster.playerMessage(rider.getName() + " can now ride this horse"));
                    }
                    plugin.addRiderQueue.remove(player);
                    return;
                }

                // Remove riders
                if (plugin.delRiderQueue.containsKey(player)) {
                    OfflinePlayer rider = plugin.delRiderQueue.get(player);
                    StabledHorse stabledHorse = stable.getHorse(horse);

                    if (player != horse.getOwner()) {
                        player.sendMessage(StableMaster.playerMessage("This is not your horse"));
                        return;
                    }

                    if (!stabledHorse.isRider(rider)) {
                        player.sendMessage(StableMaster.playerMessage(rider.getName() + " is not currently a rider!"));
                    } else {
                        stabledHorse.delRider(rider);
                        player.sendMessage(StableMaster.playerMessage(rider.getName() + " can no longer ride this horse!"));
                    }
                    plugin.delRiderQueue.remove(player);
                    return;
                }

                // Rename horse
                if (plugin.renameQueue.containsKey(player)) {
                    String name = plugin.renameQueue.get(player);
                    StabledHorse stabledHorse = stable.getHorse(horse);

                    if (player != horse.getOwner()) {
                        player.sendMessage(StableMaster.playerMessage("This is not your horse"));
                        return;
                    }

                    horse.setCustomName(name);
                    horse.setCustomNameVisible(true);
                    player.sendMessage(StableMaster.playerMessage("Horse renamed to " + name));
                    plugin.renameQueue.remove(player);
                    return;
                }

                // If we get here, the horse isn't involved in a command
                player.sendMessage(StableMaster.playerMessage("BAM! Protected by the Mighty xrobau"));
                return;
            }
        }

    }
}