package net.nperkins.stablemaster.commands;

import net.nperkins.stablemaster.LangString;
import net.nperkins.stablemaster.StableMaster;
import net.nperkins.stablemaster.data.Stable;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;

import java.util.ArrayList;
import java.util.List;

public abstract class SubCommand {

    private boolean consoleAllowed = false;
    private boolean ownerRequired = true;
    private boolean tameablesAllowed = true;
    private int minArgs = 0;
    private String name = "";
    private String permission = "";

    public abstract void handle(CommandInfo commandInfo);
    public abstract void handleInteract(Stable stable, Player player, Tameable animal);

    public boolean canBypass(CommandSender player) {
        return player.hasPermission("stablemaster.bypass.command." + getName());
    }

    public void removeFromQueue(Player player) {
        // Do nothing by default
    }

    public String getDescription() {
        return new LangString("command." + getName() + ".description").getMessage();
    }

    public String getUsage() {
        return new LangString("command." + getName() + ".usage").getMessage();
    }

    public boolean isConsoleAllowed() {
        return consoleAllowed;
    }

    protected void setConsoleAllowed(boolean consoleAllowed) {
        this.consoleAllowed = consoleAllowed;
    }

    public boolean isOwnerRequired() {
        return ownerRequired;
    }

    protected void setOwnerRequired(boolean ownerRequired) {
        this.ownerRequired = ownerRequired;
    }

    public boolean isTameablesAllowed() {
        return tameablesAllowed;
    }

    protected void setTameablesAllowed(boolean tameablesAllowed) {
        this.tameablesAllowed = tameablesAllowed;
    }

    public int getMinArgs() {
        return minArgs;
    }

    protected void setMinArgs(int minArgs) {
        this.minArgs = minArgs;
    }

    public String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
        setPermission("stablemaster." + name);
    }

    public String getPermission() {
        return permission;
    }

    protected void setPermission(String permission) {
        this.permission = permission;
    }

    public List<String> getAliases() {
        ConfigurationSection config = StableMaster.getPlugin().getConfig().getConfigurationSection("aliases");
        if (config.isList(getName()))
            return config.getStringList(getName());
        else
            return new ArrayList<>();
    }
}
