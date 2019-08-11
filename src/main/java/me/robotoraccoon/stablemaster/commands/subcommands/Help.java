package me.robotoraccoon.stablemaster.commands.subcommands;

import me.robotoraccoon.stablemaster.LangString;
import me.robotoraccoon.stablemaster.commands.CommandInfo;
import me.robotoraccoon.stablemaster.commands.CoreCommand;
import me.robotoraccoon.stablemaster.commands.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Help sub-command, display all commands to the executor
 * @author RobotoRaccoon
 */
public class Help extends SubCommand {

    /** Number of items to display on a single page */
    private static final int PER_PAGE = 6;

    /**
     * Default constructor
     */
    public Help() {
        super("help");
        setConsoleAllowed(true);
    }

    /**
     * {@inheritDoc}
     */
    public void handle(CommandInfo commandInfo) {
        CommandSender sender = commandInfo.getSender();

        // Collate commands this sender can use
        List<SubCommand> commands = new ArrayList<>();
        for (Map.Entry<String, SubCommand> entry : CoreCommand.getSubCommands()) {
            String name = entry.getKey();
            SubCommand cmd = entry.getValue();

            // Ignore the help command, and any aliases of the command
            if (cmd == this || !name.equalsIgnoreCase(cmd.getName())) {
                continue;
            }

            if (sender.hasPermission(cmd.getPermission())) {
                commands.add(cmd);
            }
        }

        // Calculate a valid page number, default to 1 otherwise
        int page = 1;
        int maxPage = (int) Math.ceil(commands.size() * 1.0 / PER_PAGE);
        if (commandInfo.getArgs().length > 0) {
            try {
                // Check if first argument is an integer, and if it falls within the range [0,maxPage]
                int arg = Integer.valueOf(commandInfo.getArg(0));
                if (arg > 0 && arg <= maxPage) {
                    page = arg;
                }
            } catch (NumberFormatException e) {
                // First argument is not an integer, ignore it.
            }
        }

        printHelp(sender, commands, page, maxPage);
    }

    /**
     * Display the help for commands the sender can use
     * @param sender Command executor
     * @param commands List of commands the sender can use
     * @param page What page to view
     * @param maxPage The maximum page viewable
     */
    private void printHelp(CommandSender sender, List<SubCommand> commands, int page, int maxPage) {
        new LangString("command.help.header").send(sender);
        new LangString("command.help.about").send(sender);

        // Calculate the first and last index to print
        int first = (page - 1) * PER_PAGE;
        int last = Math.min(commands.size(), page * PER_PAGE);

        for (int i = first; i < last; i++) {
            SubCommand cmd = commands.get(i);
            new LangString("command.help.format", cmd.getUsage(), cmd.getDescription()).send(sender);
        }

        if (maxPage > 1) {
            new LangString("command.help.more-pages", page, maxPage, getUsage()).send(sender);
        } else {
            new LangString("command.help.footer").send(sender);
        }
    }
}
