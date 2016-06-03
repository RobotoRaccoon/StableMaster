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
                StableMaster.langMessage(player, "command.info.header");
                StableMaster.langMessage(player, "not-tamed");
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
            StableMaster.addRiderQueue.remove(player);

            if (player != horse.getOwner() && !player.hasPermission("stablemaster.bypass")) {
                StableMaster.langMessage(player, "error.not-owner");
            }
            else if (stabledHorse.isRider(rider)) {
                StableMaster.rawMessage(player, String.format(
                        StableMaster.getLang("command.add-rider.is-rider"), rider.getName()));
            }
            else {
                stabledHorse.addRider(rider);
                StableMaster.rawMessage(player, String.format(
                        StableMaster.getLang("command.add-rider.added"), rider.getName()));
            }
            return;
        }

        // Remove riders
        if (StableMaster.delRiderQueue.containsKey(player)) {
            OfflinePlayer rider = StableMaster.delRiderQueue.get(player);
            StableMaster.delRiderQueue.remove(player);

            if (player != horse.getOwner() && !player.hasPermission("stablemaster.bypass")) {
                StableMaster.langMessage(player, "error.not-owner");
            }
            else if (!stabledHorse.isRider(rider)) {
                StableMaster.rawMessage(player, String.format(
                        StableMaster.getLang("command.del-rider.not-rider"), rider.getName()));
            }
            else {
                stabledHorse.delRider(rider);
                StableMaster.rawMessage(player, String.format(
                        StableMaster.getLang("command.del-rider.removed"), rider.getName()));
            }
            return;
        }

        // Give horse
        if (StableMaster.giveQueue.containsKey(player)) {
            OfflinePlayer newOwner = StableMaster.giveQueue.get(player);
            StableMaster.giveQueue.remove(player);

            if (player != horse.getOwner() && !player.hasPermission("stablemaster.bypass")) {
                StableMaster.langMessage(player, "error.not-owner");
                return;
            }

            final Stable newStable = StableMaster.getStable(newOwner);

            stable.removeHorse(horse);
            newStable.addHorse(horse);
            horse.setOwner(newOwner);

            StableMaster.rawMessage(player, String.format(
                    StableMaster.getLang("command.give.given"), newOwner.getName()));
            return;
        }

        // Rename horse
        if (StableMaster.renameQueue.containsKey(player)) {
            String name = StableMaster.renameQueue.get(player);
            StableMaster.renameQueue.remove(player);

            if (player != horse.getOwner() && !player.hasPermission("stablemaster.bypass")) {
                StableMaster.langMessage(player, "error.not-owner");
                return;
            }

            horse.setCustomName(name);
            horse.setCustomNameVisible(true);
            StableMaster.rawMessage(player, String.format(
                    StableMaster.getLang("command.rename.renamed"), name));
            return;
        }

        // Teleport horse
        if (StableMaster.teleportQueue.containsKey(player)) {

            if (player != horse.getOwner() && !player.hasPermission("stablemaster.bypass")) {
                StableMaster.langMessage(player, "error.not-owner");
                StableMaster.teleportQueue.remove(player);
                return;
            }

            // Storing location
            StableMaster.langMessage(player, "command.teleport.location-saved");
            StableMaster.horseChunk.add(horse.getLocation().getChunk());
            StableMaster.teleportQueue.put(player, horse);
            return;
        }

        // Horse info
        if (StableMaster.infoQueue.contains(player)) {
            StableMaster.infoQueue.remove(player);

            ArrayList<String> riderNames = new ArrayList<String>();
            Iterator it = stabledHorse.getRiders().iterator();
            while (it.hasNext()) {
                OfflinePlayer rider = StableMaster.getPlugin().getServer().getOfflinePlayer(UUID.fromString((String) it.next()));
                if (rider.getName() == null) {
                    //todo: some sort of lookup
                    riderNames.add(rider.getUniqueId().toString());
                } else {
                    riderNames.add(rider.getName());
                }
            }

            String permitted = stabledHorse.getRiders().isEmpty() ? "None" : Joiner.on(", ").join(riderNames);
            String variant = (horse.getVariant() == Horse.Variant.HORSE) ?
                    horse.getColor() + ", " + horse.getStyle() :
                    horse.getVariant().toString();

            // Print the info
            StableMaster.langMessage(player, "command.info.header");
            StableMaster.rawMessage(player, String.format(StableMaster.getLang("command.info.owner"), owner.getName()));
            StableMaster.rawMessage(player, String.format(StableMaster.getLang("command.info.permitted-riders"), permitted));
            StableMaster.rawMessage(player, String.format(StableMaster.getLang("command.info.jump-strength"), horse.getJumpStrength()));
            StableMaster.rawMessage(player, String.format(StableMaster.getLang("command.info.variant"), variant));
            return;
        }

        // If we get here, the horse isn't involved in a command
        StableMaster.langMessage(player, "error.protected");
    }
}