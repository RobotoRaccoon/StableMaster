package net.nperkins.stablemaster.commands.subcommands;

import net.nperkins.stablemaster.StableMaster;
import net.nperkins.stablemaster.commandlibs.CommandInfo;
import net.nperkins.stablemaster.commandlibs.SubHandler;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;


public class DelRider implements SubHandler {

    private StableMaster plugin;

    public DelRider(StableMaster plugin) {
        this.plugin = plugin;
    }

    public void handle(CommandInfo commandInfo) {
        final CommandSender sender = commandInfo.getSender();
        final String riderName = commandInfo.getArg(0);
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
                    @Override
                    public void run() {
                        OfflinePlayer rider = plugin.getServer().getOfflinePlayer(riderName);
                        if (rider != null) {
                            plugin.delRiderQueue.put((Player) sender, rider);
                            sender.sendMessage(StableMaster.playerMessage("Punch your horse."));
                        } else {
                            sender.sendMessage(StableMaster.playerMessage("We couldn't find that player."));
                        }
                    }
                }
        );
    }

    public List<String> handleComplete(CommandInfo commandInfo) {
        return null;
    }

    public String handleHelp() {
        return "delrider <player>";
    }
}
