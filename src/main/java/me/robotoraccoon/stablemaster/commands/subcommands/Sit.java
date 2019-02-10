package me.robotoraccoon.stablemaster.commands.subcommands;

import me.robotoraccoon.stablemaster.LangString;
import me.robotoraccoon.stablemaster.commands.CommandInfo;
import me.robotoraccoon.stablemaster.commands.CoreCommand;
import me.robotoraccoon.stablemaster.commands.InteractCommand;
import me.robotoraccoon.stablemaster.data.Stable;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;

/**
 * Sit sub-command, sit on top of the chosen animal
 * @author RobotoRaccoon
 */
public class Sit extends InteractCommand {

    /**
     * Default constructor
     */
    public Sit() {
        super("sit");
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
        a.addPassenger(player);
    }
}
