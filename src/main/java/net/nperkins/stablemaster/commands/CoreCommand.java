package net.nperkins.stablemaster.commands;

import net.nperkins.stablemaster.StableMaster;
import net.nperkins.stablemaster.commands.subcommands.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

public class CoreCommand implements CommandExecutor {

    public static final ConcurrentHashMap<String, SubCommand> subCommands = new ConcurrentHashMap<>();

    public CoreCommand() {
        subCommands.put("addrider", new AddRider());
        subCommands.put("delrider", new DelRider());
        subCommands.put("give",     new Give());
        subCommands.put("help",     new Help());
        subCommands.put("info",     new Info());
        subCommands.put("rename",   new Rename());
        subCommands.put("teleport", new Teleport());
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
            StableMaster.langMessage(sender, "error.no-command");
            return true;
        }

        // Sender is console and command does not allow console access.
        if (!(sender instanceof Player) && !subCommand.isConsoleAllowed()) {
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
        final String[] finalArgs = args;
        StableMaster.getPlugin().getServer().getScheduler().runTaskAsynchronously(StableMaster.getPlugin(), new Runnable() {
                    public void run() {
                        final CommandInfo commandinfo = new CommandInfo(sender, finalArgs);
                        subCommand.handle(commandinfo);
                    }
                }
        );
        return true;
    }
}
