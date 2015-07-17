package net.nperkins.stablemaster.commands.subcommands;

import net.nperkins.stablemaster.StableMaster;
import net.nperkins.stablemaster.commands.CommandInfo;
import net.nperkins.stablemaster.commands.SubCommand;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddRider extends SubCommand {

    public AddRider(StableMaster plugin) {
        this.plugin = plugin;
        setMinArgs(1);
        setPermission("stablemaster.addrider");
    }

    public void handle(CommandInfo commandInfo) {
        final CommandSender sender = commandInfo.getSender();
        final String riderName = commandInfo.getArg(0);

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
                    public void run() {
                        OfflinePlayer rider = plugin.getServer().getOfflinePlayer(riderName);
                        if (rider != null && rider.hasPlayedBefore()) {
                            plugin.addRiderQueue.put((Player) sender, rider);
                            sender.sendMessage(StableMaster.playerMessage(plugin, "Punch your horse."));
                        } else {
                            sender.sendMessage(StableMaster.playerMessage(plugin, "We couldn't find that player."));
                        }
                    }
                }
        );
    }

    public String getUsage() {
        return "addrider <player>";
    };

}
