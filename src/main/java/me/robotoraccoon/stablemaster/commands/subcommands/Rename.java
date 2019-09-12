package me.robotoraccoon.stablemaster.commands.subcommands;

import me.robotoraccoon.stablemaster.LangString;
import me.robotoraccoon.stablemaster.StableMaster;
import me.robotoraccoon.stablemaster.StableUtil;
import me.robotoraccoon.stablemaster.commands.CommandInfo;
import me.robotoraccoon.stablemaster.commands.CoreCommand;
import me.robotoraccoon.stablemaster.commands.InteractCommand;
import me.robotoraccoon.stablemaster.data.Stable;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Rename sub-command, change the display name of an animal
 * @author RobotoRaccoon
 */
public class Rename extends InteractCommand {

    /** Internal queue */
    private ConcurrentHashMap<Player, String> renameQueue = new ConcurrentHashMap<>();

    /**
     * Default constructor
     */
    public Rename() {
        super("rename");
        setMinArgs(1);
    }

    /**
     * {@inheritDoc}
     */
    public void handle(CommandInfo commandInfo) {

        final Player player = (Player) commandInfo.getSender();
        String name = StringUtils.join(commandInfo.getArgs(), " ");

        if (player.hasPermission("stablemaster.rename.colors")) {
            name = ChatColor.translateAlternateColorCodes('&', name);
        }

        renameQueue.put(player, name);
        CoreCommand.setQueuedCommand(player, this);
        new LangString("punch-animal").send(player);
    }

    /**
     * {@inheritDoc}
     */
    public void handleInteract(Stable stable, Player player, Tameable animal) {
        final Animals a = (Animals) animal;
        final ConfigurationSection config = StableMaster.getPlugin().getConfig().getConfigurationSection("command.rename");
        String name = renameQueue.get(player);
        removeFromQueue(player);

        a.setCustomName(name);
        a.setCustomNameVisible(config.getBoolean("name-always-visible"));
        new LangString("command.rename.renamed", StableUtil.getAnimal(a.getType()), name).send(player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeFromQueue(Player player) {
        renameQueue.remove(player);
    }
}
