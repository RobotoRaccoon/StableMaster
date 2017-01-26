package net.nperkins.stablemaster.commands.subcommands;

import net.nperkins.stablemaster.StableMaster;
import net.nperkins.stablemaster.commands.CommandInfo;
import net.nperkins.stablemaster.commands.SubCommand;
import net.nperkins.stablemaster.data.Stable;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;
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

        if (teleportQueue.containsKey(player) && teleportQueue.get(player) instanceof AbstractHorse) {

            AbstractHorse horse = (AbstractHorse) teleportQueue.get(player);

            // Horses duplicate with cross world teleports...
            if (horse.getLocation().getWorld() != (player).getLocation().getWorld()) {
                StableMaster.langMessage(player, "command.teleport.cross-world");
                teleportQueue.remove(player);
                return;
            }

            new TeleportEval(horse, player).runTask(StableMaster.getPlugin());
            teleportQueue.remove(player);

        } else {

            StableMaster.commandQueue.put(player, this);
            teleportQueue.put(player, true);
            StableMaster.langMessage(player, "punch-animal");
        }
    }

    public void handleInteract(Stable stable, Player player, AbstractHorse horse) {
        if (player != horse.getOwner() && !canBypass(player)) {
            StableMaster.rawMessage(player, String.format(StableMaster.getLang("error.not-owner"), horse.getType()));
            teleportQueue.remove(player);
            return;
        }

        // Storing location
        StableMaster.langMessage(player, "command.teleport.location-saved");
        StableMaster.horseChunk.add(horse.getLocation().getChunk());
        StableMaster.commandQueue.put(player, this);
        teleportQueue.put(player, horse);
    }
}

class TeleportEval extends BukkitRunnable {

    private AbstractHorse horse;
    private Player player;

    public TeleportEval(AbstractHorse horse, Player player){
        this.horse = horse;
        this.player = player;
    }

    public void run() {
        if (chunkIsLoaded()) {
            StableMaster.horseChunk.remove(horse.getLocation().getChunk());
            StableMaster.langMessage(player, "command.teleport.teleporting");
            horse.teleport(player, PlayerTeleportEvent.TeleportCause.PLUGIN);
        } else {
            StableMaster.langMessage(player, "command.teleport.failed");
        }
    }

    // Bukkit method chunk.isLoaded() appears to be currently broken, always returns true
    private boolean chunkIsLoaded() {
        Location l = horse.getLocation();
        for (Chunk c : l.getWorld().getLoadedChunks()) {
            if (c.equals(l.getChunk())) {
                return true;
            }
        }
        return false;
    }
}