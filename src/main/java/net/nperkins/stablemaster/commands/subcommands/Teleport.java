package net.nperkins.stablemaster.commands.subcommands;

import net.nperkins.stablemaster.StableMaster;
import net.nperkins.stablemaster.commands.CommandInfo;
import net.nperkins.stablemaster.commands.SubCommand;
import net.nperkins.stablemaster.data.Stable;
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

            StableMaster.commandQueue.remove(player);
            Animals animal = (Animals) teleportQueue.get(player);
            removeFromQueue(player);

            // Horses duplicate with cross world teleports...
            if (animal.getLocation().getWorld() != (player).getLocation().getWorld()) {
                StableMaster.langMessage(player, "command.teleport.cross-world");
                return;
            }

            new TeleportEval(animal, player).runTask(StableMaster.getPlugin());

        } else {

            StableMaster.commandQueue.put(player, this);
            teleportQueue.put(player, true);
            StableMaster.langMessage(player, "punch-animal");
        }
    }

    public void handleInteract(Stable stable, Player player, Tameable animal) {
        final Animals a = (Animals) animal;
        // Storing location
        StableMaster.langMessage(player, "command.teleport.location-saved");
        StableMaster.teleportChunk.add(a.getLocation().getChunk());
        StableMaster.commandQueue.put(player, this);
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

    public TeleportEval(Animals animal, Player player){
        this.animal = animal;
        this.player = player;
    }

    public void run() {
        if (chunkIsLoaded()) {
            StableMaster.teleportChunk.remove(animal.getLocation().getChunk());
            StableMaster.langMessage(player, "command.teleport.teleporting");
            animal.teleport(player, PlayerTeleportEvent.TeleportCause.PLUGIN);
        } else {
            StableMaster.langMessage(player, "command.teleport.failed");
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