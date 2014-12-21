package net.nperkins.stablemaster.commands;

import net.nperkins.stablemaster.StableMaster;
import org.bukkit.OfflinePlayer;
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

            if (args.length == 1) {
                final CommandSender s = sender;
                final String riderName = args[0];
                plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
                            @Override
                            public void run() {
                                OfflinePlayer rider = plugin.getServer().getOfflinePlayer(riderName);;
                                plugin.delRiderQueue.put((Player) s, rider);
                                plugin.sendMessage((Player) s,"Punch your horse");
                            }
                        }
                );


            } else {
                plugin.sendMessage((Player) sender,"No player provided");
                return false;

            }
        } else {
            plugin.sendMessage((Player) sender,"This cannot be run by console.");
            return false;

        }
        return true;
    }
}
