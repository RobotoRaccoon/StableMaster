package net.nperkins.stablemaster.commands.subcommands;

import net.nperkins.stablemaster.StableMaster;
import net.nperkins.stablemaster.commands.CommandInfo;
import net.nperkins.stablemaster.commands.SubCommand;
import net.nperkins.stablemaster.data.Stable;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;

import java.util.concurrent.ConcurrentHashMap;

public class Rename extends SubCommand {

    private ConcurrentHashMap<Player, String> renameQueue = new ConcurrentHashMap<>();

    public Rename() {
        setMinArgs(1);
        setName("rename");
    }

    public void handle(CommandInfo commandInfo) {

        final Player player = (Player) commandInfo.getSender();
        String name = StringUtils.join(commandInfo.getArgs(), " ");

        if (player.hasPermission("stablemaster.rename.colors"))
            name = ChatColor.translateAlternateColorCodes('&', name);

        renameQueue.put(player, name);
        StableMaster.commandQueue.put(player, this);
        StableMaster.langMessage(player, "punch-animal");
    }

    public void handleInteract(Stable stable, Player player, AbstractHorse horse) {
        String name = renameQueue.get(player);
        removeFromQueue(player);

        horse.setCustomName(name);
        horse.setCustomNameVisible(true);
        StableMaster.langFormat(player, "command.rename.renamed", horse.getType(), name);
    }

    @Override
    public void removeFromQueue(Player player) {
        renameQueue.remove(player);
    }
}
