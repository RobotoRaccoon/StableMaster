package me.robotoraccoon.stablemaster.commands;

import me.robotoraccoon.stablemaster.LangString;
import me.robotoraccoon.stablemaster.commands.subcommands.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * CommandExecutor class for our /stablemaster and /sm commands
 * @author RobotoRaccoon
 */
public class CoreCommand implements CommandExecutor {

    /** HashMap of commands, sorted by order they were added */
    private static final HashMap<String, SubCommand> subCommands = new LinkedHashMap<>();
    /** Commands waiting to be executed */
    private static final ConcurrentHashMap<Player, SubCommand> commandQueue = new ConcurrentHashMap<>();

    /**
     * Add in all of the commands
     */
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

    /**
     * Add a particular command
     * @param cmd SubCommand to be added
     */
    private static void addCommand(SubCommand cmd) {
        subCommands.put(cmd.getName(), cmd);
        for (String alias : cmd.getAliases())
            subCommands.putIfAbsent(alias, cmd);
    }

    /**
     * Execute the command
     * @param sender Who sent the command
     * @param cmd Command
     * @param label Label
     * @param args Arguments
     * @return Always true, it wouldn't be called if it was false. Legacy Bukkit code
     */
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
        subCommand.execute(new CommandInfo(label, sender, args));
        return true;
    }

    /**
     * Get every SubCommand with its attached label
     * @return Set of all commands
     */
    public static Set<Map.Entry<String, SubCommand>> getSubCommands() {
        return subCommands.entrySet();
    }

    /**
     * Set the queued command for a player
     * @param player Player
     * @param cmd SubCommand
     */
    public static void setQueuedCommand(Player player, SubCommand cmd) {
        commandQueue.put(player, cmd);
    }

    /**
     * Remove player from the queue
     * @param player Player
     * @return The command that was on the queue, or null if none
     */
    public static SubCommand removeQueuedCommand(Player player) {
        return commandQueue.remove(player);
    }

    /**
     * Does the player have a command queued?
     * @param player Player
     * @return True if there is a waiting command
     */
    public static boolean hasQueuedCommand(Player player) {
        return commandQueue.containsKey(player);
    }
}
