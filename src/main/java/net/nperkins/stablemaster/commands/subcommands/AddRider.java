package net.nperkins.stablemaster.commands.subcommands;

import net.nperkins.stablemaster.StableMaster;
import net.nperkins.stablemaster.commands.CommandInfo;
import net.nperkins.stablemaster.commands.SubCommand;
import net.nperkins.stablemaster.data.Stable;
import net.nperkins.stablemaster.data.StabledHorse;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

import java.util.concurrent.ConcurrentHashMap;

public class AddRider extends SubCommand {

    private ConcurrentHashMap<Player, OfflinePlayer> addRiderQueue = new ConcurrentHashMap<>();

    public AddRider() {
        setMinArgs(1);
        setName("addrider");
    }

    public void handle(CommandInfo commandInfo) {
        final Player player = (Player) commandInfo.getSender();
        final String riderName = commandInfo.getArg(0);

        OfflinePlayer rider = StableMaster.getPlugin().getServer().getOfflinePlayer(riderName);
        if (rider != null && rider.hasPlayedBefore()) {
            StableMaster.commandQueue.put(player, this);
            addRiderQueue.put(player, rider);
            StableMaster.langMessage(player, "punch-horse");
        } else {
            StableMaster.langMessage(player, "error.player-not-found");
        }
    }

    public void handleInteract(Stable stable, Player player, Horse horse) {
        StabledHorse stabledHorse = stable.getHorse(horse);
        OfflinePlayer rider = addRiderQueue.get(player);
        addRiderQueue.remove(player);

        if (player != horse.getOwner() && !canBypass(player)) {
            StableMaster.langMessage(player, "error.not-owner");
        }
        else if (stabledHorse.isRider(rider)) {
            StableMaster.rawMessage(player, String.format(
                    StableMaster.getLang("command.addrider.is-rider"), rider.getName()));
        }
        else {
            stabledHorse.addRider(rider);
            StableMaster.rawMessage(player, String.format(
                    StableMaster.getLang("command.addrider.added"), rider.getName()));
        }
    }
}
