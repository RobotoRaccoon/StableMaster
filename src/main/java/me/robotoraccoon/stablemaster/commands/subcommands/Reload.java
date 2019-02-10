package me.robotoraccoon.stablemaster.commands.subcommands;

import me.robotoraccoon.stablemaster.LangString;
import me.robotoraccoon.stablemaster.StableUtil;
import me.robotoraccoon.stablemaster.commands.CommandInfo;
import me.robotoraccoon.stablemaster.commands.SubCommand;
import org.bukkit.command.CommandSender;

/**
 * Reload sub-command, reload the configuration files
 * @author RobotoRaccoon
 */
public class Reload extends SubCommand {

    /**
     * Default constructor
     */
    public Reload() {
        super("reload");
        setConsoleAllowed(true);
    }

    /**
     * {@inheritDoc}
     */
    public void handle(CommandInfo commandInfo) {
        CommandSender sender = commandInfo.getSender();
        StableUtil.loadConfigData();
        new LangString("command.reload.reloaded").send(sender);
    }
}
