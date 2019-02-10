package me.robotoraccoon.stablemaster.commands.subcommands;

import me.robotoraccoon.stablemaster.LangString;
import me.robotoraccoon.stablemaster.StableUtil;
import me.robotoraccoon.stablemaster.commands.CommandInfo;
import me.robotoraccoon.stablemaster.commands.CoreCommand;
import me.robotoraccoon.stablemaster.commands.InteractCommand;
import me.robotoraccoon.stablemaster.data.Stable;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Give sub-command, transfer ownership of a horse
 * @author RobotoRaccoon
 */
public class Give extends InteractCommand {

    /** Internal queue */
    private ConcurrentHashMap<Player, OfflinePlayer> giveQueue = new ConcurrentHashMap<>();

    /**
     * Default constructor
     */
    public Give() {
        super("give");
        setMinArgs(1);
    }

    /**
     * {@inheritDoc}
     */
    public void handle(CommandInfo commandInfo) {
        final Player player = (Player) commandInfo.getSender();
        final String ownerName = commandInfo.getArg(0);

        OfflinePlayer newOwner = StableUtil.getOfflinePlayer(ownerName);
        if (newOwner != null) {
            CoreCommand.setQueuedCommand(player, this);
            giveQueue.put(player, newOwner);
            new LangString("punch-animal").send(player);
        } else {
            new LangString("error.player-not-found").send(player);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void handleInteract(Stable stable, Player player, Tameable animal) {
        OfflinePlayer newOwner = giveQueue.get(player);
        removeFromQueue(player);

        if (animal instanceof AbstractHorse) {
            AbstractHorse horse = (AbstractHorse) animal;
            Stable newStable = StableUtil.getStable(newOwner);
            stable.removeHorse(horse);
            newStable.addHorse(horse);
        }

        animal.setOwner(newOwner);
        new LangString("command.give.given", StableUtil.getAnimal(((Animals) animal).getType()), newOwner.getName()).send(player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeFromQueue(Player player) {
        giveQueue.remove(player);
    }
}
