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
                        if (StableMaster.teleportQueue.containsKey((Player) sender) &&
                                StableMaster.teleportQueue.get((Player) sender) instanceof Horse) {

                            Horse horse = (Horse) StableMaster.teleportQueue.get((Player) sender);

                            // Horses duplicate with cross world teleports...
                            if (horse.getLocation().getWorld() != ((Player) sender).getLocation().getWorld()) {
                                StableMaster.langMessage(sender, "command.teleport.cross-world");
                                StableMaster.teleportQueue.remove((Player) sender);
                                return;
                            }

                            new TeleportEval(horse, sender).runTask(StableMaster.getPlugin());
                            StableMaster.teleportQueue.remove((Player) sender);

                        } else {

                            StableMaster.teleportQueue.put((Player) sender, true);
                            StableMaster.langMessage(sender, "punch-horse");
                        }
                    }
                }
        );
    }

    public String getDescription() {
        return StableMaster.getLang("command.teleport.description");
    }

    public String getUsage() {
        return StableMaster.getLang("command.teleport.usage");
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
            StableMaster.langMessage(sender, "command.teleport.teleporting");
            horse.teleport(((Player) sender), PlayerTeleportEvent.TeleportCause.PLUGIN);
            StableMaster.teleportQueue.remove(sender);
        } else {
            StableMaster.langMessage(sender, "command.teleport.failed");
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