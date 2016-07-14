package net.nperkins.stablemaster.listeners;

import net.nperkins.stablemaster.StableMaster;
import net.nperkins.stablemaster.commands.CoreCommand;
import net.nperkins.stablemaster.data.Stable;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.projectiles.ProjectileSource;

public class EntityDamageByEntityListener implements Listener {

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        // Return if the damaged entity is not a horse.
        if (event.getEntityType() != EntityType.HORSE)
            return;

        // Handle if a player is punching the horse.
        if (event.getDamager() instanceof Player)
            playerDamageHorse(event);

        if (event.getDamager() instanceof Projectile)
            projectileDamageHorse(event);
    }

    private void playerDamageHorse(EntityDamageByEntityEvent event) {
        final Player player = (Player) event.getDamager();
        final Horse horse = (Horse) event.getEntity();

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

        // Get horse details
        final Stable stable = StableMaster.getStable((OfflinePlayer) horse.getOwner());
        // Check in case it's a pre-owned horse not known about
        if (!stable.hasHorse(horse)) {
            stable.addHorse(horse);
        }

        // Either run a command, or handle as if a player is trying to hurt the horse.
        if (StableMaster.commandQueue.containsKey(player)) {
            // Handle appropriate command
            StableMaster.commandQueue.get(player).handleInteract(stable, player, horse);
            StableMaster.commandQueue.remove(player);
            event.setCancelled(true);
        }
        else if (!canPlayerHurt(horse, player, true)) {
            // If we get here, the horse was protected and not involved in a command.
            event.setCancelled(true);
            StableMaster.langMessage(player, "error.protected");
        }
    }

    private void projectileDamageHorse(EntityDamageByEntityEvent event) {
        final ProjectileSource source = ((Projectile) event.getDamager()).getShooter();

        // Ignore cancelled events, and only player shooters
        if (event.isCancelled() || !(source instanceof Player))
            return;

        final Player player = (Player) source;
        final Horse horse = (Horse) event.getEntity();

        // If the horse has no owner, no need to protect it
        if (!horse.isTamed() || horse.getOwner() == null)
            return;

        if (!canPlayerHurt(horse, player, false)) {
            event.setCancelled(true);
            StableMaster.langMessage(player, "error.protected");
        }
    }

    private boolean canPlayerHurt(Horse horse, Player harmer, Boolean isMelee) {
        String path = "protection.";
        path += (harmer == horse.getOwner()) ? "owner-" : "player-";
        path += (isMelee) ? "melee" : "ranged";

        return !StableMaster.getPlugin().getConfig().getBoolean(path);
    }
}