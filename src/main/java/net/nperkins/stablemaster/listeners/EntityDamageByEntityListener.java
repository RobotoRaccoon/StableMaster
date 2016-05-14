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

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        // Return if the entity punching is not a player.
        if (event.getDamager().getType() != EntityType.PLAYER)
            return;

        final Player player = (Player) event.getDamager();
        final Entity entity = event.getEntity();

        // Return if the punched entity is not a horse.
        if (event.getEntityType() != EntityType.HORSE)
            return;

        final Horse horse = (Horse) entity;

        // Horse has to be tamed to be owned
        if (!horse.isTamed()) {
            if (StableMaster.infoQueue.contains(player)) {
                StableMaster.rawMessage(player, "---- Horse Info ----");
                StableMaster.rawMessage(player, "This horse is not tamed!");
                StableMaster.infoQueue.remove(player);
                event.setCancelled(true);
            }
            return;
        }

        // Added when horses could be ridden by skeletons. Horse is tamed, owner is null.
        if (horse.getOwner() == null)
            return;

        // Regardless, we want to cancel this event now
        event.setCancelled(true);

        // Get horse details
        final OfflinePlayer owner = (OfflinePlayer) horse.getOwner();
        final Stable stable = StableMaster.getStable(owner);

        // Check in case it's a pre-owned horse not known about
        if (!stable.hasHorse(horse)) {
            stable.addHorse(horse);
        }

        final StabledHorse stabledHorse = stable.getHorse(horse);

        // Add riders
        if (StableMaster.addRiderQueue.containsKey(player)) {
            OfflinePlayer rider = StableMaster.addRiderQueue.get(player);
            if (player != horse.getOwner() && !player.hasPermission("stablemaster.bypass")) {
                StableMaster.rawMessage(player, "This is not your horse!");
                return;
            }

            if (stabledHorse.isRider(rider)) {
                StableMaster.rawMessage(player, rider.getName() + "is already a rider!");
            } else {
                stabledHorse.addRider(rider);
                StableMaster.rawMessage(player, rider.getName() + " can now ride this horse");
            }
            StableMaster.addRiderQueue.remove(player);
            return;
        }

        // Remove riders
        if (StableMaster.delRiderQueue.containsKey(player)) {
            OfflinePlayer rider = StableMaster.delRiderQueue.get(player);

            if (player != horse.getOwner() && !player.hasPermission("stablemaster.bypass")) {
                StableMaster.rawMessage(player, "This is not your horse");
                return;
            }

            if (!stabledHorse.isRider(rider)) {
                StableMaster.rawMessage(player, rider.getName() + " is not currently a rider!");
            } else {
                stabledHorse.delRider(rider);
                StableMaster.rawMessage(player, rider.getName() + " can no longer ride this horse!");
            }
            StableMaster.delRiderQueue.remove(player);
            return;
        }

        // Give horse
        if (StableMaster.giveQueue.containsKey(player)) {
            OfflinePlayer newOwner = StableMaster.giveQueue.get(player);

            if (player != horse.getOwner() && !player.hasPermission("stablemaster.bypass")) {
                StableMaster.rawMessage(player, "This is not your horse");
                return;
            }

            final Stable newStable = StableMaster.getStable(newOwner);

            stable.removeHorse(horse);
            newStable.addHorse(horse);
            horse.setOwner(newOwner);

            StableMaster.rawMessage(player, "Horse given to " + newOwner.getName());
            StableMaster.giveQueue.remove(player);
            return;

        }

        // Rename horse
        if (StableMaster.renameQueue.containsKey(player)) {
            String name = StableMaster.renameQueue.get(player);
            if (player != horse.getOwner() && !player.hasPermission("stablemaster.bypass")) {
                StableMaster.rawMessage(player, "This is not your horse");
                return;
            }

            horse.setCustomName(name);
            horse.setCustomNameVisible(true);
            StableMaster.rawMessage(player, "Horse renamed to " + name);
            StableMaster.renameQueue.remove(player);
            return;
        }

        // Teleport horse
        if (StableMaster.TeleportQueue.containsKey(player)) {

            if (player != horse.getOwner() && !player.hasPermission("stablemaster.bypass")) {
                StableMaster.rawMessage(player, "This is not your horse");
                return;
            }

            // Storing location
            StableMaster.rawMessage(player, "Location stored. Run the command again at the destination");
            StableMaster.horseChunk.add(horse.getLocation().getChunk());
            StableMaster.TeleportQueue.put(player, horse);
            return;
        }

        // Horse info
        if (StableMaster.infoQueue.contains(player)) {
            ArrayList<String> riderNames = new ArrayList<String>();
            Iterator it = stabledHorse.getRiders().iterator();
            while (it.hasNext()) {
                OfflinePlayer rider = StableMaster.getPlugin().getServer().getOfflinePlayer(UUID.fromString((String) it.next()));
                if (rider.getName() == null) {
                    //todo: some sort of lookup
                    riderNames.add("Unknown player");
                } else {
                    riderNames.add(rider.getName());
                }
            }

            String permitted = stabledHorse.getRiders().isEmpty() ? "None" : Joiner.on(", ").join(riderNames);

            // Print the info
            StableMaster.rawMessage(player, "---- Horse Info ----");
            StableMaster.rawMessage(player, String.format("Owner: %s", owner.getName()));
            //player.sendMessage(StableMaster.rawMessage(String.format("Jump Strength: %f", horse.getJumpStrength())));
            StableMaster.rawMessage(player, String.format("Permitted Riders: %s", permitted));

            StableMaster.infoQueue.remove(player);
            return;
        }

        // If we get here, the horse isn't involved in a command
        StableMaster.rawMessage(player, "BAM! Protected by the Mighty xrobau");
    }
}