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

public class Teleport extends SubCommand {

    private ConcurrentHashMap<Player, Object> teleportQueue = new ConcurrentHashMap<>();

    public Teleport() {
        setName("teleport");
    }

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

    public void handleInteract(Stable stable, Player player, Tameable animal) {
        final Animals a = (Animals) animal;
        // Storing location
        new LangString("command.teleport.location-saved").send(player);
        StableMaster.getTeleportChunk().add(a.getLocation().getChunk());
        CoreCommand.setQueuedCommand(player, this);
        teleportQueue.put(player, a);
    }

    @Override
    public void removeFromQueue(Player player) {
        teleportQueue.remove(player);
    }
}

class TeleportEval extends BukkitRunnable {

    private Animals animal;
    private Player player;

    public TeleportEval(Animals animal, Player player) {
        this.animal = animal;
        this.player = player;
    }

    public void run() {
        if (chunkIsLoaded()) {
            StableMaster.getTeleportChunk().remove(animal.getLocation().getChunk());
            new LangString("command.teleport.teleporting").send(player);
            animal.teleport(player, PlayerTeleportEvent.TeleportCause.PLUGIN);
        } else {
            new LangString("command.teleport.failed").send(player);
        }
    }

    // Bukkit method chunk.isLoaded() appears to be currently broken, always returns true
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