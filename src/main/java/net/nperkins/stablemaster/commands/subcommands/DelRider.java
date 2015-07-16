package net.nperkins.stablemaster.commands.subcommands;

import net.nperkins.stablemaster.StableMaster;
import net.nperkins.stablemaster.commandlibs.CommandInfo;
import net.nperkins.stablemaster.commandlibs.SubCommand;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DelRider extends SubCommand {

    public DelRider(StableMaster plugin) {
        this.plugin = plugin;
        setMinArgs(1);
    }

    public void handle(CommandInfo commandInfo) {
        final CommandSender sender = commandInfo.getSender();
        final String riderName = commandInfo.getArg(0);
        if (sender.hasPermission("stablemaster.delrider")) {
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
                        public void run() {
                            OfflinePlayer rider = plugin.getServer().getOfflinePlayer(riderName);
                            if (rider != null && rider.hasPlayedBefore()) {
                                plugin.delRiderQueue.put((Player) sender, rider);
                                sender.sendMessage(StableMaster.playerMessage("Punch your horse."));
                            } else {
                                sender.sendMessage(StableMaster.playerMessage("We couldn't find that player."));
                            }
                        }
                    }
            );
        } else {
            sender.sendMessage(StableMaster.playerMessage("You don't have permission to do this."));
        }
    }

    public String getUsage() {
        return "delrider <player>";
    }
}
