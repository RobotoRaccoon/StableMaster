package net.nperkins.stablemaster.commands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Nick on 20/12/2014.
 */
public class StableMasterCommand implements CommandExecutor {

    private net.nperkins.stablemaster.StableMaster plugin;

    public StableMasterCommand(net.nperkins.stablemaster.StableMaster p) {
        this.plugin = p;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        plugin.sendMessage((Player) sender,"---------------------- StableMaster ----------------------");
        plugin.sendMessage((Player) sender,"Once you tame a horse, you own it. No claiming required!");
        plugin.sendMessage((Player) sender,"/smaddrider <player> - Allow player to ride your horse");
        plugin.sendMessage((Player) sender,"/smdelrider <player> - Stop player from riding your horse");
        plugin.sendMessage((Player) sender,"----------------------------------------------------------");
        return true;
    }
}
