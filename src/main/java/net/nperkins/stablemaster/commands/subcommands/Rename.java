package net.nperkins.stablemaster.commands.subcommands;

import net.nperkins.stablemaster.StableMaster;
import net.nperkins.stablemaster.commands.CommandInfo;
import net.nperkins.stablemaster.commands.SubCommand;
import net.nperkins.stablemaster.data.Stable;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

import java.util.concurrent.ConcurrentHashMap;

public class Rename extends SubCommand {

    private static ConcurrentHashMap<Player, String> renameQueue = new ConcurrentHashMap<>();

    public Rename() {
        setMinArgs(1);
        setPermission("stablemaster.rename");
    }

    public void handle(CommandInfo commandInfo) {

        final Player player = (Player) commandInfo.getSender();
        final String name = StringUtils.join(commandInfo.getArgs(), " ");

        StableMaster.commandQueue.put(player, this);
        renameQueue.put(player, ChatColor.translateAlternateColorCodes('&', name));
        StableMaster.langMessage(player, "punch-horse");
    }

    public void handleInteract(Stable stable, Player player, Horse horse) {
        String name = renameQueue.get(player);
        renameQueue.remove(player);

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
