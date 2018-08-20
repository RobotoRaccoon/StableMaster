package me.robotoraccoon.stablemaster.commands;

import org.bukkit.command.CommandSender;

/**
 * Information pertaining to a run command
 * @author RobotoRaccoon
 */
public class CommandInfo {

    /** The base command used */
    private final String label;
    /** Who ran the command */
    private final CommandSender sender;
    /** Arguments supplied */
    private final String[] args;

    /**
     * Constructor
     * @param label Base command
     * @param sender Who sent the command
     * @param args Arguments sent in
     */
    public CommandInfo(String label, CommandSender sender, String[] args) {
        this.label = label;
        this.sender = sender;
        this.args = args;
    }

    /**
     * Get the label
     * @return Label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Get the sender
     * @return Sender
     */
    public CommandSender getSender() {
        return sender;
    }

    /**
     * Get the supplied arguments
     * @return Arguments
     */
    public String[] getArgs() {
        return args;
    }

    /**
     * Get a particular argument
     * @param index Index
     * @return Individual argument
     */
    public String getArg(int index) {
        return args[index];
    }
}
