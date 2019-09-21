package me.robotoraccoon.stablemaster.commands.subcommands;

import me.robotoraccoon.stablemaster.LangString;
import me.robotoraccoon.stablemaster.StableUtil;
import me.robotoraccoon.stablemaster.commands.CommandInfo;
import me.robotoraccoon.stablemaster.commands.CoreCommand;
import me.robotoraccoon.stablemaster.commands.InteractCommand;
import me.robotoraccoon.stablemaster.data.Stable;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;

/**
 * Unname sub-command, remove the display name of an animal
 * @author RobotoRaccoon
 */
public class Unname extends InteractCommand {

    /**
     * Default constructor
     */
    public Unname() {
        super("unname");
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

        a.setCustomName(null);
        a.setCustomNameVisible(false);
        new LangString("command.unname.unnamed", StableUtil.getAnimal(a.getType())).send(player);
    }
}
