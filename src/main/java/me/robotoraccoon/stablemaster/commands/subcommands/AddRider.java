package me.robotoraccoon.stablemaster.commands.subcommands;

import me.robotoraccoon.stablemaster.LangString;
import me.robotoraccoon.stablemaster.StableUtil;
import me.robotoraccoon.stablemaster.commands.CommandInfo;
import me.robotoraccoon.stablemaster.commands.CoreCommand;
import me.robotoraccoon.stablemaster.commands.InteractCommand;
import me.robotoraccoon.stablemaster.data.Stable;
import me.robotoraccoon.stablemaster.data.StabledHorse;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;

import java.util.concurrent.ConcurrentHashMap;

/**
 * AddRider sub-command, allow another player to ride a horse
 * @author RobotoRaccoon
 */
public class AddRider extends InteractCommand {

    /** Internal queue */
    private ConcurrentHashMap<Player, OfflinePlayer> addRiderQueue = new ConcurrentHashMap<>();

    /**
     * Default constructor
     */
    public AddRider() {
        super("addrider");
        setTameablesAllowed(false);
        setMinArgs(1);
    }

    /**
     * {@inheritDoc}
     */
    public void handle(CommandInfo commandInfo) {
        final Player player = (Player) commandInfo.getSender();
        final String riderName = commandInfo.getArg(0);

        OfflinePlayer rider = StableUtil.getOfflinePlayer(riderName);
        if (rider != null) {
            CoreCommand.setQueuedCommand(player, this);
            addRiderQueue.put(player, rider);
            new LangString("punch-animal").send(player);
        } else {
            new LangString("error.player-not-found").send(player);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void handleInteract(Stable stable, Player player, Tameable animal) {
        final AbstractHorse horse = (AbstractHorse) animal;
        StabledHorse stabledHorse = stable.getHorse(horse);
        OfflinePlayer rider = addRiderQueue.get(player);
        removeFromQueue(player);

        if (stabledHorse.isRider(rider)) {
            new LangString("command.addrider.is-rider", rider.getName()).send(player);
        } else {
            stabledHorse.addRider(rider);
            new LangString("command.addrider.added", rider.getName(), StableUtil.getAnimal(horse.getType())).send(player);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeFromQueue(Player player) {
        addRiderQueue.remove(player);
    }
}
