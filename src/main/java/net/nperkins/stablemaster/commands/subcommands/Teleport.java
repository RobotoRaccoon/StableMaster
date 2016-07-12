package net.nperkins.stablemaster.commands.subcommands;

import net.nperkins.stablemaster.StableMaster;
import net.nperkins.stablemaster.commands.CommandInfo;
import net.nperkins.stablemaster.commands.SubCommand;
import net.nperkins.stablemaster.data.Stable;
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
        final Player player = (Player) sender;

        StableMaster.getPlugin().getServer().getScheduler().runTaskAsynchronously(StableMaster.getPlugin(), new Runnable() {
                    public void run() {
                        if (StableMaster.teleportQueue.containsKey(player) &&
                                StableMaster.teleportQueue.get(player) instanceof Horse) {

                            Horse horse = (Horse) StableMaster.teleportQueue.get(player);

                            // Horses duplicate with cross world teleports...
                            if (horse.getLocation().getWorld() != (player).getLocation().getWorld()) {
                                StableMaster.langMessage(sender, "command.teleport.cross-world");
                                StableMaster.teleportQueue.remove(player);
                                return;
                            }

                            new TeleportEval(horse, sender).runTask(StableMaster.getPlugin());
                            StableMaster.teleportQueue.remove(player);

                        } else {

                            StableMaster.teleportQueue.put((Player) sender, true);
                            StableMaster.langMessage(sender, "punch-horse");
                        }
                    }
                }
        );
    }

    public void handleInteract(Stable stable, Player player, Horse horse) {
        if (player != horse.getOwner() && !player.hasPermission("stablemaster.bypass")) {
            StableMaster.langMessage(player, "error.not-owner");
            StableMaster.teleportQueue.remove(player);
            return;
        }

        // Storing location
        StableMaster.langMessage(player, "command.teleport.location-saved");
        StableMaster.horseChunk.add(horse.getLocation().getChunk());
        StableMaster.teleportQueue.put(player, horse);
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