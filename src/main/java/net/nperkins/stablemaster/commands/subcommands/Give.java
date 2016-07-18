package net.nperkins.stablemaster.commands.subcommands;

import net.nperkins.stablemaster.StableMaster;
import net.nperkins.stablemaster.commands.CommandInfo;
import net.nperkins.stablemaster.commands.SubCommand;
import net.nperkins.stablemaster.data.Stable;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

import java.util.concurrent.ConcurrentHashMap;

public class Give extends SubCommand {

    private ConcurrentHashMap<Player, OfflinePlayer> giveQueue = new ConcurrentHashMap<>();

    public Give() {
        setMinArgs(1);
        setPermission("stablemaster.give");
    }

    public void handle(CommandInfo commandInfo) {
        final Player player = (Player) commandInfo.getSender();
        final String ownerName = commandInfo.getArg(0);

        OfflinePlayer rider = StableMaster.getPlugin().getServer().getOfflinePlayer(ownerName);
        if (rider != null && rider.hasPlayedBefore()) {
            StableMaster.commandQueue.put(player, this);
            giveQueue.put(player, rider);
            StableMaster.langMessage(player, "punch-horse");
        } else {
            StableMaster.langMessage(player, "error.player-not-found");
        }
    }

    public void handleInteract(Stable stable, Player player, Horse horse) {
        OfflinePlayer newOwner = giveQueue.get(player);
        giveQueue.remove(player);

        if (player != horse.getOwner() && !player.hasPermission("stablemaster.bypass.give")) {
            StableMaster.langMessage(player, "error.not-owner");
            return;
        }

        Stable newStable = StableMaster.getStable(newOwner);

        stable.removeHorse(horse);
        newStable.addHorse(horse);
        horse.setOwner(newOwner);

        StableMaster.rawMessage(player, String.format(
                StableMaster.getLang("command.give.given"), newOwner.getName()));
    }

    public String getDescription() {
        return StableMaster.getLang("command.give.description");
    }

    public String getUsage() {
        return StableMaster.getLang("command.give.usage");
    }
}
