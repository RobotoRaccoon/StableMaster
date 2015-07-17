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

    public Teleport(StableMaster plugin) {
        this.plugin = plugin;
        setPermission("stablemaster.teleport");
    }

    public void handle(CommandInfo commandInfo) {
        final CommandSender sender = commandInfo.getSender();

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
                    public void run() {
                        if (plugin.TeleportQueue.containsKey((Player) sender) &&
                                plugin.TeleportQueue.get((Player) sender) instanceof Horse) {

                            Horse horse = (Horse) plugin.TeleportQueue.get((Player) sender);

                            // Horses duplicate with cross world teleports...
                            if (horse.getLocation().getWorld() != ((Player) sender).getLocation().getWorld()) {
                                sender.sendMessage(StableMaster.playerMessage(plugin, "You cannot teleport horses across worlds."));
                                plugin.TeleportQueue.remove((Player) sender);
                                return;
                            }

                            new TeleportEval(plugin, horse, sender).runTask(plugin);
                            plugin.TeleportQueue.remove((Player) sender);

                        } else {

                            plugin.TeleportQueue.put((Player) sender, true);
                            sender.sendMessage(StableMaster.playerMessage(plugin, "Punch your horse."));
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

    StableMaster plugin;
    Horse horse;
    CommandSender sender;

    public TeleportEval(StableMaster plugin, Horse horse, CommandSender sender){
        this.plugin = plugin;
        this.horse = horse;
        this.sender = sender;
    }

    public void run() {
        if (chunkIsLoaded()) {
            StableMaster.horseChunk.remove(horse.getLocation().getChunk());
            sender.sendMessage(StableMaster.playerMessage(plugin, "Teleporting..."));
            horse.teleport(((Player) sender), PlayerTeleportEvent.TeleportCause.PLUGIN);
            plugin.TeleportQueue.remove(sender);
        } else {
            sender.sendMessage(StableMaster.playerMessage(plugin, "Teleport failed, get a friend to stand near your horse next time."));
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