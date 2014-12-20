package net.nperkins.stablemaster.commands;

import net.nperkins.stablemaster.StableMaster;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class AddRider implements CommandExecutor {

    private StableMaster plugin;

    public AddRider(StableMaster p) {
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
                                plugin.addRiderQueue.put((Player) s, rider);
                                s.sendMessage("Punch yo horse");
                            }
                        }
                );


            } else {
                sender.sendMessage("No player provided");
                return false;

            }
        } else {
            sender.sendMessage("This cannot be run by console.");
            return false;

        }
        return true;
    }
}
