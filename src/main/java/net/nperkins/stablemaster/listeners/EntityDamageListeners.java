package net.nperkins.stablemaster.listeners;

import net.nperkins.stablemaster.StableMaster;
import net.nperkins.stablemaster.commands.SubCommand;
import net.nperkins.stablemaster.data.Stable;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.projectiles.ProjectileSource;

public class EntityDamageListeners implements Listener {

    // Event for the "always protect" config settings
    @EventHandler (ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        // Return if the damaged entity is not a tameable entity.
        if (!(event.getEntity() instanceof Tameable))
            return;

        // If the animal has no owner, no need to protect it
        final Tameable animal = (Tameable) event.getEntity();
        if (!animal.isTamed() || animal.getOwner() == null)
            return;

        String cause = event.getCause().name().toLowerCase();
        ConfigurationSection config = getProtectionConfig();

        if (config.getBoolean(cause))
            event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        // Return if the damaged entity is not a tameable entity.
        if (!(event.getEntity() instanceof Tameable))
            return;

        // Handle if a player is punching the animal.
        if (event.getDamager() instanceof Player)
            playerDamageAnimal(event);

        // Ignore cancelled events, as each source below are not issuing commands.
        if (event.isCancelled())
            return;

        // If the animal has no owner, no need to protect it
        final Tameable animal = (Tameable) event.getEntity();
        if (!animal.isTamed() || animal.getOwner() == null)
            return;

        if (event.getDamager() instanceof Monster)
            monsterDamageAnimal(event);

        if (event.getDamager() instanceof Projectile)
            projectileDamageAnimal(event);
    }

    private void playerDamageAnimal(EntityDamageByEntityEvent event) {
        final Player player = (Player) event.getDamager();
        final Tameable animal = (Tameable) event.getEntity();

        // Animal has to be tamed to be owned. Owner is null when owned by non-players.
        if (!animal.isTamed() || animal.getOwner() == null) {
            if (StableMaster.commandQueue.containsKey(player)) {
                event.setCancelled(true);
                StableMaster.langFormat(player, "not-tamed", event.getEntityType());
                StableMaster.commandQueue.remove(player);
            }
            return;
        }

        // Check in case it's a pre-owned horse not known about
        final Stable stable = StableMaster.getStable((OfflinePlayer) animal.getOwner());
        if (animal instanceof AbstractHorse) {
            final AbstractHorse horse = (AbstractHorse) animal;
            if (!stable.hasHorse(horse)) {
                stable.addHorse(horse);
            }
        }

        // Either run a command, or handle as if a player is trying to hurt the animal.
        if (StableMaster.commandQueue.containsKey(player)) {
            // Handle appropriate command
            event.setCancelled(true);
            SubCommand cmd = StableMaster.commandQueue.get(player);
            StableMaster.commandQueue.remove(player);

            if (cmd.isOwnerRequired() && player != animal.getOwner() && !cmd.canBypass(player)) {
                StableMaster.langFormat(player, "error.not-owner", event.getEntityType());
                cmd.removeFromQueue(player);
                return;
            }

            // Run for horses, and tameables if the command allows it.
            if (animal instanceof AbstractHorse || cmd.isTameablesAllowed())
                cmd.handleInteract(stable, player, animal);
            else
                StableMaster.langMessage(player, "error.not-supported");
            return;
        }

        if (!canPlayerHurt(animal, player, true)) {
            // If we get here, the animal was protected and not involved in a command.
            event.setCancelled(true);
            StableMaster.langFormat(player, "error.protected", event.getEntityType());
        }
    }

    private void monsterDamageAnimal(EntityDamageByEntityEvent event) {
        if (getProtectionConfig().getBoolean("monster-melee"))
            event.setCancelled(true);
    }

    private void projectileDamageAnimal(EntityDamageByEntityEvent event) {
        final Tameable animal = (Tameable) event.getEntity();
        final ProjectileSource source = ((Projectile) event.getDamager()).getShooter();
        if (source instanceof Player) {
            // If a player harmed the animal
            final Player player = (Player) source;
            if (!canPlayerHurt(animal, player, false)) {
                event.setCancelled(true);
                StableMaster.langFormat(player, "error.protected", event.getEntityType());
            }
        }
        else if (source instanceof Monster) {
            // If a monster harmed the animal
            if (getProtectionConfig().getBoolean("monster-ranged"))
                event.setCancelled(true);
        }
        else {
            // If something else harmed the animal (eg, dispenser)
            if (getProtectionConfig().getBoolean("other-ranged"))
                event.setCancelled(true);
        }
    }

    private ConfigurationSection getProtectionConfig() {
        return StableMaster.getPlugin().getConfig().getConfigurationSection("protection");
    }

    private boolean canPlayerHurt(Tameable animal, Player harmer, Boolean isMelee) {
        Boolean bypass = harmer.hasPermission("stablemaster.bypass.protection");
        // If the complete-bypass setting is true, always allow player damage
        if (bypass && getProtectionConfig().getBoolean("complete-bypass"))
            return true;

        String path = (harmer == animal.getOwner() || bypass) ? "owner-" : "player-";
        path += (isMelee) ? "melee" : "ranged";
        return !getProtectionConfig().getBoolean(path);
    }
}