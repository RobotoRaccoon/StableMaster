package net.nperkins.stablemaster.listeners;

import com.google.common.base.Joiner;
import net.nperkins.stablemaster.StableMaster;
import net.nperkins.stablemaster.commands.CoreCommand;
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
            if (StableMaster.commandQueue.get(player) == CoreCommand.subCommands.get("info")) {
                StableMaster.langMessage(player, "command.info.header");
                StableMaster.langMessage(player, "not-tamed");
                StableMaster.commandQueue.remove(player);
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
        final Stable stable = StableMaster.getStable((OfflinePlayer) horse.getOwner());

        // Check in case it's a pre-owned horse not known about
        if (!stable.hasHorse(horse)) {
            stable.addHorse(horse);
        }


        if (StableMaster.commandQueue.containsKey(player)) {
            // Handle appropriate command
            StableMaster.commandQueue.get(player).handleInteract(stable, player, horse);
            StableMaster.commandQueue.remove(player);
        }
        else {
            // If we get here, the horse isn't involved in a command
            StableMaster.langMessage(player, "error.protected");
        }
    }
}