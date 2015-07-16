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
    private StableMaster plugin;

    public CoreCommand(StableMaster plugin) {
        this.plugin = plugin;

        subCommands.put("addrider", new AddRider(this.plugin));
        subCommands.put("delrider", new DelRider(this.plugin));
        subCommands.put("give",     new Give(this.plugin));
        subCommands.put("help",     new Help(this.plugin));
        subCommands.put("info",     new Info(this.plugin));
        subCommands.put("rename",   new Rename(this.plugin));
        subCommands.put("teleport", new Teleport(this.plugin));
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
            sender.sendMessage(StableMaster.playerMessage("Command not found"));
            return true;
        }

        if (player == null && !subCommand.isConsoleAllowed()) {
            sender.sendMessage(StableMaster.playerMessage("This command cannot be run from console"));
            return true;
        }

        if (args.length < subCommand.getMinArgs()) {
            sender.sendMessage(StableMaster.playerMessage("Incorrect number of arguments supplied"));
            sender.sendMessage(StableMaster.playerMessage("/" + label + " " + subCommand.getUsage()));
            return true;
        }

        final CommandInfo commandinfo = new CommandInfo(sender, args);
        subCommand.handle(commandinfo);
        return true;
    }
}
