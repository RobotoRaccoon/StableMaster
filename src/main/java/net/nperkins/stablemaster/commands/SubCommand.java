package net.nperkins.stablemaster.commands;

import net.nperkins.stablemaster.StableMaster;
import net.nperkins.stablemaster.commands.CommandInfo;

public abstract class SubCommand {

    protected StableMaster plugin;
    protected boolean consoleAllowed = false;
    protected int minArgs = 0;
    protected String permission = "";

    public abstract void handle(CommandInfo commandInfo);
    public abstract String getUsage();

    public StableMaster getPlugin() {
        return plugin;
    }

    public boolean isConsoleAllowed() {
        return consoleAllowed;
    }

    public void setConsoleAllowed(boolean consoleAllowed) {
        this.consoleAllowed = consoleAllowed;
    }

    public int getMinArgs() {
        return minArgs;
    }

    public void setMinArgs(int minArgs) {
        this.minArgs = minArgs;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
