package net.nperkins.stablemaster.commands.subcommands;

import net.nperkins.stablemaster.StableMaster;
import net.nperkins.stablemaster.commands.CommandInfo;
import net.nperkins.stablemaster.commands.SubCommand;
import net.nperkins.stablemaster.data.Stable;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;

public class Reload extends SubCommand {

    public Reload() {
        setConsoleAllowed(true);
        setName("reload");
    }

    public void handle(CommandInfo commandInfo) {
        CommandSender sender = commandInfo.getSender();
        StableMaster.loadConfigData();
        StableMaster.langMessage(sender, "command.reload.reloaded");
    }

    public void handleInteract(Stable stable, Player player, Tameable animal) {
        // Nothing
    }
}
