package net.nperkins.stablemaster.commands.subcommands;

import net.nperkins.stablemaster.StableMaster;
import net.nperkins.stablemaster.commandlibs.CommandInfo;
import net.nperkins.stablemaster.commandlibs.SubHandler;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class Teleport implements SubHandler {

    private StableMaster plugin;

    public Teleport(StableMaster plugin) {
        this.plugin = plugin;
    }

    public void handle(CommandInfo commandInfo) {
        final CommandSender sender = commandInfo.getSender();

        if (sender.hasPermission("stablemaster.teleport")) {
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
                        public void run() {
                            if (plugin.TeleportQueue.containsKey((Player) sender) &&
                                    plugin.TeleportQueue.get((Player) sender) instanceof Horse) {

                                Horse horse = (Horse) plugin.TeleportQueue.get((Player) sender);

                                // Horses duplicate with cross world teleports...
                                if (horse.getLocation().getWorld() != ((Player) sender).getLocation().getWorld()) {
                                    sender.sendMessage(StableMaster.playerMessage("You cannot teleport horses across worlds."));
                                    plugin.TeleportQueue.remove((Player) sender);
                                    return;
                                }

                                new TeleportEval(horse, sender).runTask(plugin);
                                plugin.TeleportQueue.remove((Player) sender);

                            } else {

                                plugin.TeleportQueue.put((Player) sender, true);
                                sender.sendMessage(StableMaster.playerMessage("Punch your horse."));
                            }
                        }
                    }
            );
        } else {
            sender.sendMessage(StableMaster.playerMessage("You don't have permission to do this."));
        }
    }

    public List<String> handleComplete(CommandInfo commandInfo) {
        return null;
    }

    public String handleHelp() {
        return "teleport";
    }

}
class TeleportEval extends BukkitRunnable {

    Horse horse;
    CommandSender sender;

    public TeleportEval(Horse horse, CommandSender sender){
        this.horse = horse;
        this.sender = sender;
    }

    public void run() {
        if (chunkIsLoaded()) {
            horse.teleport(((Player) sender), PlayerTeleportEvent.TeleportCause.PLUGIN);
            sender.sendMessage(StableMaster.playerMessage("Teleporting..."));
        } else {
            sender.sendMessage(StableMaster.playerMessage("Teleport failed, get a friend to stand near your horse next time."));
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