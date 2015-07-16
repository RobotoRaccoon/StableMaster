package net.nperkins.stablemaster.commands.subcommands;

import net.nperkins.stablemaster.StableMaster;
import net.nperkins.stablemaster.commands.CommandInfo;
import net.nperkins.stablemaster.commands.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
public class Rename extends SubCommand {

    public Rename(StableMaster plugin) {
        this.plugin = plugin;
        setMinArgs(1);
        setPermission("stablemaster.rename");
    }

    public void handle(CommandInfo commandInfo) {
        final CommandSender sender = commandInfo.getSender();
        final String name = commandInfo.getArg(0);

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
    }

    public String getUsage() {
        return "rename <name>";
    };

}
