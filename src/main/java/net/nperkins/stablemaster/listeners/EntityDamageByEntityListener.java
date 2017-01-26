package net.nperkins.stablemaster.listeners;

import net.nperkins.stablemaster.StableMaster;
import net.nperkins.stablemaster.commands.CoreCommand;
import net.nperkins.stablemaster.data.Stable;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.projectiles.ProjectileSource;

public class EntityDamageByEntityListener implements Listener {

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        // Return if the damaged entity is not a horse.
        if (!(event.getEntity() instanceof AbstractHorse))
            return;

        // Handle if a player is punching the horse.
        if (event.getDamager() instanceof Player)
            playerDamageHorse(event);

        if (event.getDamager() instanceof Projectile)
            projectileDamageHorse(event);
    }

    private void playerDamageHorse(EntityDamageByEntityEvent event) {
        final Player player = (Player) event.getDamager();
        final AbstractHorse horse = (AbstractHorse) event.getEntity();

        // Horse has to be tamed to be owned. Owner is null when owned by non-players.
        if (!horse.isTamed() || horse.getOwner() == null) {
            if (StableMaster.commandQueue.containsKey(player)) {
                event.setCancelled(true);
                StableMaster.rawMessage(player, String.format(StableMaster.getLang("not-tamed"), event.getEntityType()));
                StableMaster.commandQueue.remove(player);
            }
            return;
        }

        // Get horse details
        final Stable stable = StableMaster.getStable((OfflinePlayer) horse.getOwner());
        // Check in case it's a pre-owned horse not known about
        if (!stable.hasHorse(horse)) {
            stable.addHorse(horse);
        }

        // Either run a command, or handle as if a player is trying to hurt the horse.
        if (StableMaster.commandQueue.containsKey(player)) {
            // Handle appropriate command
            event.setCancelled(true);
            StableMaster.commandQueue.get(player).handleInteract(stable, player, horse);
            StableMaster.commandQueue.remove(player);
        }
        else if (!canPlayerHurt(horse, player, true)) {
            // If we get here, the horse was protected and not involved in a command.
            event.setCancelled(true);
            StableMaster.rawMessage(player, String.format(StableMaster.getLang("error.protected"), horse.getType()));
        }
    }

    private void projectileDamageHorse(EntityDamageByEntityEvent event) {
        final ProjectileSource source = ((Projectile) event.getDamager()).getShooter();

        // Ignore cancelled events, and only player shooters
        if (event.isCancelled() || !(source instanceof Player))
            return;

        final Player player = (Player) source;
        final AbstractHorse horse = (AbstractHorse) event.getEntity();

        // If the horse has no owner, no need to protect it
        if (!horse.isTamed() || horse.getOwner() == null)
            return;

        if (!canPlayerHurt(horse, player, false)) {
            event.setCancelled(true);
            StableMaster.rawMessage(player, String.format(StableMaster.getLang("error.protected"), horse.getType()));
        }
    }

    private boolean canPlayerHurt(AbstractHorse horse, Player harmer, Boolean isMelee) {
        ConfigurationSection config = StableMaster.getPlugin().getConfig().getConfigurationSection("protection");
        Boolean bypass = harmer.hasPermission("stablemaster.bypass.protection");

        String path = (harmer == horse.getOwner() || bypass) ? "owner-" : "player-";
        path += (isMelee) ? "melee" : "ranged";

        return !config.getBoolean(path);
    }
}