package net.nperkins.stablemaster.commands.subcommands;

import net.nperkins.stablemaster.StableMaster;
import net.nperkins.stablemaster.commands.CommandInfo;
import net.nperkins.stablemaster.commands.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Info extends SubCommand {

    public Info() {
        setPermission("stablemaster.info");
    }

    public void handle(CommandInfo commandInfo) {
        final CommandSender sender = commandInfo.getSender();

        StableMaster.getPlugin().getServer().getScheduler().runTaskAsynchronously(StableMaster.getPlugin(), new Runnable() {
                    public void run() {
                        if (!StableMaster.infoQueue.contains(sender)) {
                            StableMaster.infoQueue.add((Player) sender);
                        }
                        StableMaster.langMessage(sender, "punch-horse");
                    }
                }
        );
    }

    public String getDescription() {
        return StableMaster.getLang("command.info.description");
    }

    public String getUsage() {
        return StableMaster.getLang("command.info.usage");
    }
}
