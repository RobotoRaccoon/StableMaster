package net.nperkins.stablemaster.commands.subcommands;

import net.nperkins.stablemaster.StableMaster;
import net.nperkins.stablemaster.commands.CommandInfo;
import net.nperkins.stablemaster.commands.SubCommand;
import net.nperkins.stablemaster.data.Stable;
import net.nperkins.stablemaster.data.StabledHorse;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

import java.util.concurrent.ConcurrentHashMap;

public class DelRider extends SubCommand {

    private ConcurrentHashMap<Player, OfflinePlayer> delRiderQueue = new ConcurrentHashMap<>();

    public DelRider() {
        setMinArgs(1);
        setPermission("stablemaster.delrider");
    }

    public void handle(CommandInfo commandInfo) {
        final Player player = (Player) commandInfo.getSender();
        final String riderName = commandInfo.getArg(0);

        OfflinePlayer rider = StableMaster.getPlugin().getServer().getOfflinePlayer(riderName);
        if (rider != null && rider.hasPlayedBefore()) {
            StableMaster.commandQueue.put(player, this);
            delRiderQueue.put(player, rider);
            StableMaster.langMessage(player, "punch-horse");
        } else {
            StableMaster.langMessage(player, "error.player-not-found");
        }
    }

    public void handleInteract(Stable stable, Player player, Horse horse) {
        StabledHorse stabledHorse = stable.getHorse(horse);
        OfflinePlayer rider = delRiderQueue.get(player);
        delRiderQueue.remove(player);

        if (player != horse.getOwner() && !player.hasPermission("stablemaster.bypass.delrider")) {
            StableMaster.langMessage(player, "error.not-owner");
        }
        else if (!stabledHorse.isRider(rider)) {
            StableMaster.rawMessage(player, String.format(
                    StableMaster.getLang("command.del-rider.not-rider"), rider.getName()));
        }
        else {
            stabledHorse.delRider(rider);
            StableMaster.rawMessage(player, String.format(
                    StableMaster.getLang("command.del-rider.removed"), rider.getName()));
        }
    }

    public String getDescription() {
        return StableMaster.getLang("command.del-rider.description");
    }

    public String getUsage() {
        return StableMaster.getLang("command.del-rider.usage");
    }
}
