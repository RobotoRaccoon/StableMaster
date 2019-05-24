package me.robotoraccoon.stablemaster.commands.subcommands;

import me.robotoraccoon.stablemaster.LangString;
import me.robotoraccoon.stablemaster.StableMaster;
import me.robotoraccoon.stablemaster.commands.CommandInfo;
import me.robotoraccoon.stablemaster.commands.CoreCommand;
import me.robotoraccoon.stablemaster.commands.InteractCommand;
import me.robotoraccoon.stablemaster.data.AnimalSet;
import me.robotoraccoon.stablemaster.data.Stable;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Teleport sub-command, transport an animal to another location
 * @author RobotoRaccoon
 */
public class Teleport extends InteractCommand {

    /** Internal queue */
    private ConcurrentHashMap<Player, AnimalSet> teleportQueue = new ConcurrentHashMap<>();

    /**
     * Default constructor
     */
    public Teleport() {
        super("teleport");
    }

    /**
     * {@inheritDoc}
     */
    public void handle(CommandInfo commandInfo) {
        final Player player = (Player) commandInfo.getSender();

        AnimalSet set = teleportQueue.get(player);
        if (set != null && !set.isEmpty()) {

            CoreCommand.removeQueuedCommand(player);
            removeFromQueue(player);
            new TeleportEval(set, player).runTask(StableMaster.getPlugin());

        } else {

            CoreCommand.setQueuedCommand(player, this);
            teleportQueue.put(player, new AnimalSet());
            new LangString("punch-animal").send(player);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void handleInteract(Stable stable, Player player, Tameable animal) {
        final Animals a = (Animals) animal;

        // Storing location
        AnimalSet set = teleportQueue.get(player);
        boolean empty = set.isEmpty();
        boolean added = set.add(a);
        CoreCommand.setQueuedCommand(player, this);

        Chunk chunk = a.getLocation().getChunk();
        if (!chunk.isForceLoaded()) {
            StableMaster.getTeleportChunk().add(chunk);
            chunk.setForceLoaded(true);
        }

        if (empty) {
            new LangString("command.teleport.location-saved").send(player);
        } else if (added) {
            new LangString("command.teleport.location-saved-multiple").send(player);
        } else {
            new LangString("command.teleport.location-already-saved").send(player);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeFromQueue(Player player) {
        teleportQueue.remove(player);
    }

    /**
     * TeleportEval private inner-class runnable, to run asynchronously
     * @author RobotoRaccoon
     */
    private class TeleportEval extends BukkitRunnable {

        /** Animal set used */
        private AnimalSet set;
        /** Player running the teleport */
        private Player player;

        /**
         * Constructor
         * @param set Animal set
         * @param player Player
         */
        public TeleportEval(AnimalSet set, Player player) {
            this.set = set;
            this.player = player;
        }

        /**
         * Run the eval to teleport all animals in the set
         */
        public void run() {
            boolean success = true;

            while (!set.isEmpty()) {
                success &= teleport(set.pop());
            }

            if (success) {
                new LangString("command.teleport.teleporting").send(player);
            } else {
                new LangString("command.teleport.failed").send(player);
            }
        }

        /**
         * Teleport the given animal
         * @param animal Animal to teleport
         * @return True if the teleportation was successful
         */
        private boolean teleport(Animals animal) {
            if (!chunkIsLoaded(animal)) {
                return false;
            }

            Chunk chunk = animal.getLocation().getChunk();
            if (StableMaster.getTeleportChunk().remove(chunk)) {
                chunk.setForceLoaded(false);
            }
            return animal.teleport(player, PlayerTeleportEvent.TeleportCause.PLUGIN);
        }

        /**
         * Check if a chunk is currently loaded
         */
        private boolean chunkIsLoaded(Animals animal) {
            Location l = animal.getLocation();
            for (Chunk c : l.getWorld().getLoadedChunks()) {
                if (c.equals(l.getChunk())) {
                    return true;
                }
            }
            return false;
        }
    }
}
