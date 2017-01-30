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
import org.bukkit.projectiles.ProjectileSource;

public class EntityDamageByEntityListener implements Listener {

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        // Return if the damaged entity is not a tameable entity.
        if (!(event.getEntity() instanceof Tameable))
            return;

        // Handle if a player is punching the animal.
        if (event.getDamager() instanceof Player)
            playerDamageAnimal(event);

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
        // If the animal has no owner, no need to protect it
        final Tameable animal = (Tameable) event.getEntity();
        if (!animal.isTamed() || animal.getOwner() == null)
            return;

        if (!canMonsterHurt(true))
            event.setCancelled(true);
    }

    private void projectileDamageAnimal(EntityDamageByEntityEvent event) {
        // Ignore cancelled events
        if (event.isCancelled())
            return;

        // If the animal has no owner, no need to protect it
        final Tameable animal = (Tameable) event.getEntity();
        if (!animal.isTamed() || animal.getOwner() == null)
            return;

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
            if (!canMonsterHurt(false))
                event.setCancelled(true);
        }
    }

    private boolean canPlayerHurt(Tameable animal, Player harmer, Boolean isMelee) {
        ConfigurationSection config = StableMaster.getPlugin().getConfig().getConfigurationSection("protection");
        Boolean bypass = harmer.hasPermission("stablemaster.bypass.protection");

        String path = (harmer == animal.getOwner() || bypass) ? "owner-" : "player-";
        path += (isMelee) ? "melee" : "ranged";

        return !config.getBoolean(path);
    }

    private boolean canMonsterHurt(Boolean isMelee) {
        ConfigurationSection config = StableMaster.getPlugin().getConfig().getConfigurationSection("protection");
        String path = "monster-" + (isMelee ? "melee" : "ranged");
        return !config.getBoolean(path);
    }
}