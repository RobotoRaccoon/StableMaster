package net.nperkins.stablemaster.commands.subcommands;

import net.nperkins.stablemaster.StableMaster;
import net.nperkins.stablemaster.commands.CommandInfo;
import net.nperkins.stablemaster.commands.SubCommand;
import net.nperkins.stablemaster.data.Stable;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;

import java.util.concurrent.ConcurrentHashMap;

public class Give extends SubCommand {

    private ConcurrentHashMap<Player, OfflinePlayer> giveQueue = new ConcurrentHashMap<>();

    public Give() {
        setMinArgs(1);
        setName("give");
    }

    public void handle(CommandInfo commandInfo) {
        final Player player = (Player) commandInfo.getSender();
        final String ownerName = commandInfo.getArg(0);

        OfflinePlayer newOwner = StableMaster.getPlugin().getServer().getOfflinePlayer(ownerName);
        if (newOwner != null && newOwner.hasPlayedBefore()) {
            StableMaster.commandQueue.put(player, this);
            giveQueue.put(player, newOwner);
            StableMaster.langMessage(player, "punch-animal");
        } else {
            StableMaster.langMessage(player, "error.player-not-found");
        }
    }

    public void handleInteract(Stable stable, Player player, AbstractHorse horse) {
        OfflinePlayer newOwner = giveQueue.get(player);
        removeFromQueue(player);
        Stable newStable = StableMaster.getStable(newOwner);

        stable.removeHorse(horse);
        newStable.addHorse(horse);
        horse.setOwner(newOwner);

        StableMaster.langFormat(player, "command.give.given", horse.getType(), newOwner.getName());
    }

    @Override
    public void removeFromQueue(Player player) {
        giveQueue.remove(player);
    }
}
