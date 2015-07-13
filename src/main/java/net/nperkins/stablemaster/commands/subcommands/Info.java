package net.nperkins.stablemaster.commands.subcommands;

import net.nperkins.stablemaster.StableMaster;
import net.nperkins.stablemaster.commandlibs.CommandInfo;
import net.nperkins.stablemaster.commandlibs.SubHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;


public class Info implements SubHandler {

    private StableMaster plugin;

    public Info(StableMaster plugin) {
        this.plugin = plugin;
    }

    public void handle(CommandInfo commandInfo) {
        final CommandSender sender = commandInfo.getSender();
        if (sender.hasPermission("stablemaster.info")) {
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
                        public void run() {
                            if (!plugin.infoQueue.contains(sender)) {
                                plugin.infoQueue.add((Player) sender);
                            }
                            sender.sendMessage(StableMaster.playerMessage("Punch the horse."));
                        }
                    }
            );
        } else {
            sender.sendMessage(StableMaster.playerMessage("You don't have permission to do this."));
        }
    }
    public List<String> handleComplete(CommandInfo commandInfo) {
        return null;
    }

    public String handleHelp() {
        return "info";
    };

}
