package me.robotoraccoon.stablemaster.commands.subcommands;

import me.robotoraccoon.stablemaster.LangString;
import me.robotoraccoon.stablemaster.StableMaster;
import me.robotoraccoon.stablemaster.StableUtil;
import me.robotoraccoon.stablemaster.commands.CommandInfo;
import me.robotoraccoon.stablemaster.commands.CoreCommand;
import me.robotoraccoon.stablemaster.commands.SubCommand;
import me.robotoraccoon.stablemaster.data.Stable;
import me.robotoraccoon.stablemaster.data.StabledHorse;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;

import java.util.concurrent.ConcurrentHashMap;

public class DelRider extends SubCommand {

    private ConcurrentHashMap<Player, OfflinePlayer> delRiderQueue = new ConcurrentHashMap<>();

    public DelRider() {
        setTameablesAllowed(false);
        setMinArgs(1);
        setName("delrider");
    }

    public void handle(CommandInfo commandInfo) {
        final Player player = (Player) commandInfo.getSender();
        final String riderName = commandInfo.getArg(0);

        OfflinePlayer rider = StableMaster.getPlugin().getServer().getOfflinePlayer(riderName);
        if (rider != null && rider.hasPlayedBefore()) {
            CoreCommand.setQueuedCommand(player, this);
            delRiderQueue.put(player, rider);
            new LangString("punch-animal").send(player);
        } else {
            new LangString("error.player-not-found").send(player);
        }
    }

    public void handleInteract(Stable stable, Player player, Tameable animal) {
        final AbstractHorse horse = (AbstractHorse) animal;
        StabledHorse stabledHorse = stable.getHorse(horse);
        OfflinePlayer rider = delRiderQueue.get(player);
        removeFromQueue(player);

        if (!stabledHorse.isRider(rider)) {
            new LangString("command.delrider.not-rider", rider.getName()).send(player);
        } else {
            stabledHorse.delRider(rider);
            new LangString("command.delrider.removed", rider.getName(), StableUtil.getAnimal(horse.getType())).send(player);
        }
    }

    @Override
    public void removeFromQueue(Player player) {
        delRiderQueue.remove(player);
    }
}
