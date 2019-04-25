package me.robotoraccoon.stablemaster.commands.subcommands;

import me.robotoraccoon.stablemaster.LangString;
import me.robotoraccoon.stablemaster.StableMaster;
import me.robotoraccoon.stablemaster.StableUtil;
import me.robotoraccoon.stablemaster.commands.CommandInfo;
import me.robotoraccoon.stablemaster.commands.CoreCommand;
import me.robotoraccoon.stablemaster.commands.InteractCommand;
import me.robotoraccoon.stablemaster.data.Stable;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.*;
import org.bukkit.inventory.AbstractHorseInventory;

/**
 * Release sub-command, let this animal go back into the wild
 * @author RobotoRaccoon
 */
public class Release extends InteractCommand {

    /**
     * Default constructor
     */
    public Release() {
        super("release");
    }

    /**
     * {@inheritDoc}
     */
    public void handle(CommandInfo commandInfo) {
        final Player player = (Player) commandInfo.getSender();
        CoreCommand.setQueuedCommand(player, this);
        new LangString("punch-animal").send(player);
    }

    /**
     * {@inheritDoc}
     */
    public void handleInteract(Stable stable, Player player, Tameable animal) {
        final Animals a = (Animals) animal;
        final ConfigurationSection config = StableMaster.getPlugin().getConfig().getConfigurationSection("command.release");

        // If animal is a horse, drop its saddle and remove it from the stable.
        if (animal instanceof AbstractHorse) {
            final AbstractHorse horse = (AbstractHorse) animal;
            final AbstractHorseInventory inv = horse.getInventory();

            // Saddle must drop, else it cannot be tamed later
            if (inv.getSaddle() != null) {
                horse.getWorld().dropItemNaturally(horse.getLocation(), inv.getSaddle());
                inv.setSaddle(null);
            }

            stable.removeHorse(horse);
        } else if (animal instanceof Sittable) {
            // Set released animals to non-sitting position
            ((Sittable) animal).setSitting(false);
        }

        if (config.getBoolean("clear-custom-name")) {
            a.setCustomNameVisible(false);
            a.setCustomName("");
        }

        // Finally remove ownership of the animal
        animal.setTamed(false);
        animal.setOwner(null);

        new LangString("command.release.released", StableUtil.getAnimal(a.getType())).send(player);
    }
}
