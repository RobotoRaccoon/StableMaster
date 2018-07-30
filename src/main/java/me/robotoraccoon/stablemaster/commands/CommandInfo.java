package me.robotoraccoon.stablemaster.commands;

import org.bukkit.command.CommandSender;

public class CommandInfo {

    private String label;
    private final CommandSender sender;
    private final String[] args;

    public CommandInfo(String label, CommandSender sender, String[] args) {
        this.label = label;
        this.sender = sender;
        this.args = args;
    }

    public String getLabel() {
        return label;
    }

    public CommandSender getSender() {
        return sender;
    }

    public String[] getArgs() {
        return args;
    }

    public String getArg(int n) {
        return args[n];
    }
}
