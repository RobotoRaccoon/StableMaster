package net.nperkins.stablemaster.listeners;

import com.google.common.base.Joiner;
import net.nperkins.stablemaster.StableMaster;
import net.nperkins.stablemaster.data.Stable;
import net.nperkins.stablemaster.data.StabledHorse;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;


public class EntityDamageByEntityListener implements Listener {

    final StableMaster plugin;

    public EntityDamageByEntityListener(StableMaster plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        // Check if a player fired this
        if (event.getDamager().getType() == EntityType.PLAYER) {
            final Player player = (Player) event.getDamager();
            final Entity entity = event.getEntity();
            // Is they punch a horse?
            if (event.getEntityType() == EntityType.HORSE) {
                final Horse horse = (Horse) entity;
                // Horse has to be tamed to be owned
                if (!horse.isTamed()) {
                    if (plugin.infoQueue.contains(player)) {
                        player.sendMessage(StableMaster.playerMessage("---- Horse Info ----"));
                        player.sendMessage(StableMaster.playerMessage("This horse is not tamed!"));
                        plugin.infoQueue.remove(player);
                        event.setCancelled(true);
                    }
                    return;
                }

                // Regardless, we want to cancel this event now
                event.setCancelled(true);

                // Get horse details
                final OfflinePlayer owner = (OfflinePlayer) horse.getOwner();
                final Stable stable = plugin.getStable(owner);

                // Check in case it's a pre-owned horse not known about
                if (!stable.hasHorse(horse)) {
                    stable.addHorse(horse);
                }

                final StabledHorse stabledHorse = stable.getHorse(horse);

                // Add riders
                if (plugin.addRiderQueue.containsKey(player)) {
                    OfflinePlayer rider = plugin.addRiderQueue.get(player);
                    if (player != horse.getOwner()) {
                        player.sendMessage(StableMaster.playerMessage("This is not your horse!"));
                        return;
                    }

                    if (stabledHorse.isRider(rider)) {
                        player.sendMessage(StableMaster.playerMessage(rider.getName() + "is already a rider!"));
                    } else {
                        stabledHorse.addRider(rider);
                        player.sendMessage(StableMaster.playerMessage(rider.getName() + " can now ride this horse"));
                    }
                    plugin.addRiderQueue.remove(player);
                    return;
                }

                // Remove riders
                if (plugin.delRiderQueue.containsKey(player)) {
                    OfflinePlayer rider = plugin.delRiderQueue.get(player);

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

                // Horse info
                if (plugin.infoQueue.contains(player)) {
                    ArrayList<String> riderNames = new ArrayList<String>();
                    Iterator it = stabledHorse.getRiders().iterator();
                    while (it.hasNext()) {
                        OfflinePlayer rider = plugin.getServer().getOfflinePlayer(UUID.fromString((String) it.next()));
                        if (rider.getName() == null) {
                            //todo: some sort of lookup
                            riderNames.add("Unknown player");
                        } else {
                            riderNames.add(rider.getName());
                        }
                    }
                    player.sendMessage(StableMaster.playerMessage("---- Horse Info ----"));
                    player.sendMessage(StableMaster.playerMessage(String.format("Owner: %s", owner.getName())));
                    //player.sendMessage(StableMaster.playerMessage(String.format("Jump Strength: %f", horse.getJumpStrength())));
                    if (stabledHorse.getRiders().isEmpty()) {
                        player.sendMessage(StableMaster.playerMessage("Permitted Riders: None"));
                    } else {
                        player.sendMessage(StableMaster.playerMessage(String.format("Permitted Riders: %s", Joiner.on(", ").join(riderNames))));
                    }
                    plugin.infoQueue.remove(player);
                    return;
                }
                // If we get here, the horse isn't involved in a command
                player.sendMessage(StableMaster.playerMessage("BAM! Protected by the Mighty xrobau"));
                return;
            }

        }
    }
}