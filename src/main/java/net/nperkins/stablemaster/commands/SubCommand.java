package net.nperkins.stablemaster.commands;

import net.nperkins.stablemaster.StableMaster;
import net.nperkins.stablemaster.commands.CommandInfo;
import net.nperkins.stablemaster.data.Stable;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

public abstract class SubCommand {

    private boolean consoleAllowed = false;
    private int minArgs = 0;
    private String permission = "";

    public abstract void handle(CommandInfo commandInfo);
    public abstract void handleInteract(Stable stable, Player player, Horse horse);
    public abstract String getDescription();
    public abstract String getUsage();

    public boolean isConsoleAllowed() {
        return consoleAllowed;
    }

    protected void setConsoleAllowed(boolean consoleAllowed) {
        this.consoleAllowed = consoleAllowed;
    }

    public int getMinArgs() {
        return minArgs;
    }

    protected void setMinArgs(int minArgs) {
        this.minArgs = minArgs;
    }

    public String getPermission() {
        return permission;
    }

    protected void setPermission(String permission) {
        this.permission = permission;
    }
}