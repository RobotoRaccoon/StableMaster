package net.nperkins.stablemaster.commands;

import net.nperkins.stablemaster.StableMaster;
import net.nperkins.stablemaster.commands.subcommands.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class CoreCommand implements CommandExecutor {

    public static final Map<String, SubCommand> subCommands = new LinkedHashMap<String, SubCommand>();

    public CoreCommand() {
        subCommands.put("addrider", new AddRider());
        subCommands.put("delrider", new DelRider());
        subCommands.put("give",     new Give());
        subCommands.put("help",     new Help());
        subCommands.put("info",     new Info());
        subCommands.put("rename",   new Rename());
        subCommands.put("teleport", new Teleport());
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = null;

        if (sender instanceof Player) {
            player = (Player) sender;
        }

        String subCommandName = null;
        if (args.length > 0) {
            subCommandName = args[0].toLowerCase();
            args = Arrays.copyOfRange(args, 1, args.length); // Remove the first argument.
        } else {
            // No command given - use default
            // todo: remove hardcoding
            subCommandName = "help";

        }

        SubCommand subCommand = subCommands.get(subCommandName);

        // Improper command specified.
        if (subCommand == null) {
            StableMaster.langMessage(sender, "error.no-command");
            return true;
        }

        // Sender is console and command does not allow console access.
        if (player == null && !subCommand.isConsoleAllowed()) {
            StableMaster.langMessage(sender, "error.no-console");
            return true;
        }

        // Player does not have permission to use the command.
        if (!sender.hasPermission(subCommand.getPermission())) {
            StableMaster.langMessage(sender, "error.no-permission");
            return true;
        }

        // Not enough arguments have been supplied.
        if (args.length < subCommand.getMinArgs()) {
            StableMaster.langMessage(sender, "error.arguments");
            StableMaster.rawMessage(sender, "/" + label + " " + subCommand.getUsage());
            return true;
        }

        // Run the command.
        final CommandInfo commandinfo = new CommandInfo(sender, args);
        subCommand.handle(commandinfo);
        return true;
    }
}
