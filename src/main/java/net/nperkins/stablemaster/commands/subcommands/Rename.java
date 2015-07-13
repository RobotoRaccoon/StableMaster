package net.nperkins.stablemaster.commands.subcommands;

import net.nperkins.stablemaster.StableMaster;
import net.nperkins.stablemaster.commandlibs.CommandInfo;
import net.nperkins.stablemaster.commandlibs.SubHandler;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;


public class Rename implements SubHandler {

    private StableMaster plugin;

    public Rename(StableMaster plugin) {
        this.plugin = plugin;
    }

    public void handle(CommandInfo commandInfo) {
        final CommandSender sender = commandInfo.getSender();
        final String name = commandInfo.getArg(0);
        if (sender.hasPermission("stablemaster.rename")) {
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
                        public void run() {
                            if (name != null) {
                                plugin.renameQueue.put((Player) sender, ChatColor.translateAlternateColorCodes('&', name));
                                sender.sendMessage(StableMaster.playerMessage("Punch your horse."));
                            } else {
                                sender.sendMessage(StableMaster.playerMessage("No name supplied."));
                            }
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
        return "rename <name>";
    };

}
