package net.nperkins.stablemaster.commands.subcommands;

import net.nperkins.stablemaster.StableMaster;
import net.nperkins.stablemaster.commands.CommandInfo;
import net.nperkins.stablemaster.commands.SubCommand;
import net.nperkins.stablemaster.data.Stable;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Horse;
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

    public void handleInteract(Stable stable, Player player, Horse horse) {
        String name = StableMaster.renameQueue.get(player);

        if (player != horse.getOwner() && !player.hasPermission("stablemaster.bypass")) {
            StableMaster.langMessage(player, "error.not-owner");
            return;
        }

        horse.setCustomName(name);
        horse.setCustomNameVisible(true);
        StableMaster.rawMessage(player, String.format(
                StableMaster.getLang("command.rename.renamed"), name));
    }

    public String getDescription() {
        return StableMaster.getLang("command.rename.description");
    }

    public String getUsage() {
        return StableMaster.getLang("command.rename.usage");
    }
}
