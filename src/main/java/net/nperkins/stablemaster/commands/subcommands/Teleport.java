package net.nperkins.stablemaster.commands.subcommands;

import net.nperkins.stablemaster.StableMaster;
import net.nperkins.stablemaster.commands.CommandInfo;
import net.nperkins.stablemaster.commands.SubCommand;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class Teleport extends SubCommand {

    public Teleport() {
        setPermission("stablemaster.teleport");
    }

    public void handle(CommandInfo commandInfo) {
        final CommandSender sender = commandInfo.getSender();

        StableMaster.getPlugin().getServer().getScheduler().runTaskAsynchronously(StableMaster.getPlugin(), new Runnable() {
                    public void run() {
                        if (StableMaster.TeleportQueue.containsKey((Player) sender) &&
                                StableMaster.TeleportQueue.get((Player) sender) instanceof Horse) {

                            Horse horse = (Horse) StableMaster.TeleportQueue.get((Player) sender);

                            // Horses duplicate with cross world teleports...
                            if (horse.getLocation().getWorld() != ((Player) sender).getLocation().getWorld()) {
                                StableMaster.rawMessage(sender, "You cannot teleport horses across worlds.");
                                StableMaster.TeleportQueue.remove((Player) sender);
                                return;
                            }

                            new TeleportEval(horse, sender).runTask(StableMaster.getPlugin());
                            StableMaster.TeleportQueue.remove((Player) sender);

                        } else {

                            StableMaster.TeleportQueue.put((Player) sender, true);
                            StableMaster.rawMessage(sender, "Punch your horse.");
                        }
                    }
                }
        );
    }

    public String getUsage() {
        return "teleport";
    }

}
class TeleportEval extends BukkitRunnable {

    private Horse horse;
    private CommandSender sender;

    public TeleportEval(Horse horse, CommandSender sender){
        this.horse = horse;
        this.sender = sender;
    }

    public void run() {
        if (chunkIsLoaded()) {
            StableMaster.horseChunk.remove(horse.getLocation().getChunk());
            StableMaster.rawMessage(sender, "Teleporting...");
            horse.teleport(((Player) sender), PlayerTeleportEvent.TeleportCause.PLUGIN);
            StableMaster.TeleportQueue.remove(sender);
        } else {
            StableMaster.rawMessage(sender, "Teleport failed, get a friend to stand near your horse next time.");
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