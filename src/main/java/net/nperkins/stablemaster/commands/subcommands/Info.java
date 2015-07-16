package net.nperkins.stablemaster.commands.subcommands;

import net.nperkins.stablemaster.StableMaster;
import net.nperkins.stablemaster.commands.CommandInfo;
import net.nperkins.stablemaster.commands.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Info extends SubCommand {

    public Info(StableMaster plugin) {
        this.plugin = plugin;
        setPermission("stablemaster.info");
    }

    public void handle(CommandInfo commandInfo) {
        final CommandSender sender = commandInfo.getSender();

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
                    public void run() {
                        if (!plugin.infoQueue.contains(sender)) {
                            plugin.infoQueue.add((Player) sender);
                        }
                        sender.sendMessage(StableMaster.playerMessage("Punch the horse."));
                    }
                }
        );
    }

    public String getUsage() {
        return "info";
    };

}
