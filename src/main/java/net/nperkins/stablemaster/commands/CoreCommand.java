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

    public final Map<String, SubCommand> subCommands = new LinkedHashMap<String, SubCommand>();

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
            args = Arrays.copyOfRange(args, 1, args.length);
        } else {
            // No command given - use default
            // todo: remove hardcoding
            subCommandName = "help";

        }

        SubCommand subCommand = subCommands.get(subCommandName);

        if (subCommand == null) {
            sender.sendMessage(StableMaster.langMessage("error.no-command"));
            return true;
        }

        if (player == null && !subCommand.isConsoleAllowed()) {
            sender.sendMessage(StableMaster.langMessage("error.no-console"));
            return true;
        }

        if (!sender.hasPermission(subCommand.getPermission())) {
            sender.sendMessage(StableMaster.langMessage("error.no-permission"));
            return true;
        }

        if (args.length < subCommand.getMinArgs()) {
            sender.sendMessage(StableMaster.langMessage("error.arguments"));
            sender.sendMessage(StableMaster.playerMessage("/" + label + " " + subCommand.getUsage()));
            return true;
        }

        final CommandInfo commandinfo = new CommandInfo(sender, args);
        subCommand.handle(commandinfo);
        return true;
    }
}
