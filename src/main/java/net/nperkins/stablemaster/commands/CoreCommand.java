package net.nperkins.stablemaster.commands;

import net.nperkins.stablemaster.LangString;
import net.nperkins.stablemaster.StableMaster;
import net.nperkins.stablemaster.commands.subcommands.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class CoreCommand implements CommandExecutor {

    public static final HashMap<String, SubCommand> subCommands = new LinkedHashMap<>();

    public static void addAllCommands() {
        subCommands.clear();
        addCommand(new AddRider());
        addCommand(new DelRider());
        addCommand(new Give());
        addCommand(new Help());
        addCommand(new Info());
        addCommand(new Release());
        addCommand(new Reload());
        addCommand(new Rename());
        addCommand(new Teleport());
    }

    private static void addCommand(SubCommand cmd) {
        subCommands.put(cmd.getName(), cmd);
        for(String alias : cmd.getAliases())
            subCommands.put(alias, cmd);
    }

    public boolean onCommand(final CommandSender sender, Command cmd, String label, String[] args) {
        String subCommandName;
        if (args.length > 0) {
            subCommandName = args[0].toLowerCase();
            args = Arrays.copyOfRange(args, 1, args.length); // Remove the first argument.
        } else {
            // No command given - use default
            subCommandName = "help";
        }

        final SubCommand subCommand = subCommands.get(subCommandName);

        // Improper command specified.
        if (subCommand == null) {
            new LangString("error.no-command").send(sender);
            return true;
        }

        // Attempt execution.
        subCommand.execute(label, new CommandInfo(sender, args));
        return true;
    }
}
