package net.nperkins.stablemaster.commands;

import net.nperkins.stablemaster.StableMaster;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Nick on 20/12/2014.
 */
public class DelRider implements CommandExecutor {

    private StableMaster plugin;

    public DelRider(StableMaster p) {
        this.plugin = p;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            plugin.delRiderQueue.put((Player) sender, ((Player) sender));
            sender.sendMessage("Punch yo horse");
        } else {
            sender.sendMessage("This cannot be run by console.");
            return false;
        }
        return true;
    }
}
