package net.nperkins.stablemaster.commandlibs;

import org.bukkit.command.CommandSender;

public class CommandInfo {

    private final CommandSender sender;

    private final String[] args;

    public CommandInfo(CommandSender sender, String[] args) {
        this.sender = sender;
        this.args = args;
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
