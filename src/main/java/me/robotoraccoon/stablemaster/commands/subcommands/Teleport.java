package me.robotoraccoon.stablemaster.commands.subcommands;

import me.robotoraccoon.stablemaster.LangString;
import me.robotoraccoon.stablemaster.StableMaster;
import me.robotoraccoon.stablemaster.commands.CommandInfo;
import me.robotoraccoon.stablemaster.commands.CoreCommand;
import me.robotoraccoon.stablemaster.commands.SubCommand;
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
public class Teleport extends SubCommand {

    /** Internal queue */
    private ConcurrentHashMap<Player, Object> teleportQueue = new ConcurrentHashMap<>();

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

        if (teleportQueue.containsKey(player) && teleportQueue.get(player) instanceof Animals) {

            CoreCommand.removeQueuedCommand(player);
            Animals animal = (Animals) teleportQueue.get(player);
            removeFromQueue(player);

            // Horses duplicate with cross world teleports...
            if (animal.getLocation().getWorld() != (player).getLocation().getWorld()) {
                new LangString("command.teleport.cross-world").send(player);
                return;
            }

            new TeleportEval(animal, player).runTask(StableMaster.getPlugin());

        } else {

            CoreCommand.setQueuedCommand(player, this);
            teleportQueue.put(player, true);
            new LangString("punch-animal").send(player);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void handleInteract(Stable stable, Player player, Tameable animal) {
        final Animals a = (Animals) animal;
        // Storing location
        new LangString("command.teleport.location-saved").send(player);
        StableMaster.getTeleportChunk().add(a.getLocation().getChunk());
        CoreCommand.setQueuedCommand(player, this);
        teleportQueue.put(player, a);
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

        /** Animal used */
        private Animals animal;
        /** Player running the teleport */
        private Player player;

        /**
         * Constructor
         * @param animal Animal
         * @param player Player
         */
        public TeleportEval(Animals animal, Player player) {
            this.animal = animal;
            this.player = player;
        }

        /**
         * Run the eval
         */
        public void run() {
            if (chunkIsLoaded()) {
                StableMaster.getTeleportChunk().remove(animal.getLocation().getChunk());
                new LangString("command.teleport.teleporting").send(player);
                animal.teleport(player, PlayerTeleportEvent.TeleportCause.PLUGIN);
            } else {
                new LangString("command.teleport.failed").send(player);
            }
        }

        /**
         * Check if a chunk is currently loaded
         */
        private boolean chunkIsLoaded() {
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
