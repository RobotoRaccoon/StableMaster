package net.nperkins.stablemaster.commands.subcommands;

import net.nperkins.stablemaster.StableMaster;
import net.nperkins.stablemaster.commands.CommandInfo;
import net.nperkins.stablemaster.commands.SubCommand;
import net.nperkins.stablemaster.data.Stable;
import net.nperkins.stablemaster.data.StabledHorse;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;

import java.util.concurrent.ConcurrentHashMap;

public class DelRider extends SubCommand {

    private ConcurrentHashMap<Player, OfflinePlayer> delRiderQueue = new ConcurrentHashMap<>();

    public DelRider() {
        setMinArgs(1);
        setName("delrider");
    }

    public void handle(CommandInfo commandInfo) {
        final Player player = (Player) commandInfo.getSender();
        final String riderName = commandInfo.getArg(0);

        OfflinePlayer rider = StableMaster.getPlugin().getServer().getOfflinePlayer(riderName);
        if (rider != null && rider.hasPlayedBefore()) {
            StableMaster.commandQueue.put(player, this);
            delRiderQueue.put(player, rider);
            StableMaster.langMessage(player, "punch-animal");
        } else {
            StableMaster.langMessage(player, "error.player-not-found");
        }
    }

    public void handleInteract(Stable stable, Player player, AbstractHorse horse) {
        StabledHorse stabledHorse = stable.getHorse(horse);
        OfflinePlayer rider = delRiderQueue.get(player);
        removeFromQueue(player);

        if (!stabledHorse.isRider(rider)) {
            StableMaster.langFormat(player, "command.delrider.not-rider", rider.getName());
        }
        else {
            stabledHorse.delRider(rider);
            StableMaster.langFormat(player, "command.delrider.removed", rider.getName(), horse.getType());
        }
    }

    @Override
    public void removeFromQueue(Player player) {
        delRiderQueue.remove(player);
    }
}
