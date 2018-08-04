package me.robotoraccoon.stablemaster.commands;

import me.robotoraccoon.stablemaster.LangString;
import me.robotoraccoon.stablemaster.StableMaster;
import me.robotoraccoon.stablemaster.data.Stable;
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

    public void removeFromQueue(Player player) {
        // Do nothing by default
    }

    public final void execute(CommandInfo info) {
        CommandSender sender = info.getSender();

        // Sender is console and command does not allow console access.
        if (!(sender instanceof Player) && !isConsoleAllowed()) {
            new LangString("error.no-console").send(sender);
            return;
        }

        // Player does not have permission to use the command.
        if (!sender.hasPermission(getPermission())) {
            new LangString("error.no-permission").send(sender);
            return;
        }

        // Not enough arguments have been supplied.
        if (info.getArgs().length < getMinArgs()) {
            new LangString("error.arguments").send(sender);
            new LangString().append("/" + info.getLabel() + " " + getUsage()).send(sender);
            return;
        }

        // Run the command.
        StableMaster.getPlugin().getServer().getScheduler().runTaskAsynchronously(StableMaster.getPlugin(), new Runnable() {
                    public void run() {
                        handle(info);
                    }
                }
        );
    }

    public final boolean canBypass(CommandSender player) {
        return player.hasPermission("stablemaster.bypass.command." + getName());
    }

    public final String getDescription() {
        return new LangString("command." + getName() + ".description").getMessage();
    }

    public final String getUsage() {
        return new LangString("command." + getName() + ".usage").getMessage();
    }

    public final boolean isConsoleAllowed() {
        return consoleAllowed;
    }

    protected final void setConsoleAllowed(boolean consoleAllowed) {
        this.consoleAllowed = consoleAllowed;
    }

    public final boolean isOwnerRequired() {
        return ownerRequired;
    }

    protected final void setOwnerRequired(boolean ownerRequired) {
        this.ownerRequired = ownerRequired;
    }

    public final boolean isTameablesAllowed() {
        return tameablesAllowed;
    }

    protected final void setTameablesAllowed(boolean tameablesAllowed) {
        this.tameablesAllowed = tameablesAllowed;
    }

    public final int getMinArgs() {
        return minArgs;
    }

    protected final void setMinArgs(int minArgs) {
        this.minArgs = minArgs;
    }

    public final String getName() {
        return name;
    }

    protected final void setName(String name) {
        this.name = name;
        setPermission("stablemaster." + name);
    }

    public final String getPermission() {
        return permission;
    }

    protected final void setPermission(String permission) {
        this.permission = permission;
    }

    public final List<String> getAliases() {
        ConfigurationSection config = StableMaster.getPlugin().getConfig().getConfigurationSection("aliases");
        if (config.isList(getName()))
            return config.getStringList(getName());
        else
            return new ArrayList<>();
    }
}
