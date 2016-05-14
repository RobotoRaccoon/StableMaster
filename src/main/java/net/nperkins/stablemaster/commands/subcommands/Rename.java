package net.nperkins.stablemaster.commands.subcommands;

import net.nperkins.stablemaster.StableMaster;
import net.nperkins.stablemaster.commands.CommandInfo;
import net.nperkins.stablemaster.commands.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
public class Rename extends SubCommand {

    public Rename() {
        setMinArgs(1);
        setPermission("stablemaster.rename");
    }

    public void handle(CommandInfo commandInfo) {
        final CommandSender sender = commandInfo.getSender();
        final String name = commandInfo.getArg(0);

        StableMaster.getPlugin().getServer().getScheduler().runTaskAsynchronously(StableMaster.getPlugin(), new Runnable() {
                    public void run() {
                        if (name != null) {
                            StableMaster.renameQueue.put((Player) sender, ChatColor.translateAlternateColorCodes('&', name));
                            StableMaster.rawMessage(sender, "Punch your horse.");
                        } else {
                            StableMaster.rawMessage(sender, "No name supplied.");
                        }
                    }
                }
        );
    }

    public String getUsage() {
        return "rename <name>";
    };

}
