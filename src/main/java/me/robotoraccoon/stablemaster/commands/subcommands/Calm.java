package me.robotoraccoon.stablemaster.commands.subcommands;

import me.robotoraccoon.stablemaster.LangString;
import me.robotoraccoon.stablemaster.StableMaster;
import me.robotoraccoon.stablemaster.commands.CommandInfo;
import me.robotoraccoon.stablemaster.commands.SubCommand;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.*;

import java.util.Collection;

/**
 * Calms all animals near the player by removing their target entity
 * @author RobotoRaccoon
 */
public class Calm extends SubCommand {

    /**
     * Default constructor
     */
    public Calm() {
        super("calm");
    }

    /**
     * {@inheritDoc}
     */
    public void handle(CommandInfo commandInfo) {
        final Player player = (Player) commandInfo.getSender();

        ConfigurationSection config = StableMaster.getPlugin().getConfig().getConfigurationSection("command.calm");
        int radius = (int) config.getDouble("radius", 16);
        Location loc = player.getLocation();

        // Check every entity in the area if it is animal owned by the player
        Collection<Entity> nearby = player.getWorld().getNearbyEntities(loc, radius, radius, radius);
        int count = 0;
        for (Entity entity : nearby) {
            if (!(entity instanceof Tameable))
                continue;

            Tameable tameable = (Tameable) entity;
            if (tameable.isTamed() && (tameable.getOwner() == player || canBypass(player))) {
                // If the animal is owned by the player and has a target, remove the target
                Animals animal = (Animals) entity;
                if (animal.getTarget() != null) {
                    if (animal instanceof Wolf) {
                        ((Wolf) animal).setAngry(false);
                    }
                    animal.setTarget(null);
                    count++;
                }
            }
        }

        if (count < 1) {
            new LangString("command.calm.no-animals").send(player);
        } else {
            new LangString("command.calm.calmed", count, radius).send(player);
        }
    }
}
