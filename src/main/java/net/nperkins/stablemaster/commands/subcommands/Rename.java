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
                        StableMaster.renameQueue.put((Player) sender, ChatColor.translateAlternateColorCodes('&', name));
                        StableMaster.langMessage(sender, "punch-horse");
                    }
                }
        );
    }

    public String getDescription() {
        return StableMaster.getLang("command.rename.description");
    }

    public String getUsage() {
        return StableMaster.getLang("command.rename.usage");
    }
}
