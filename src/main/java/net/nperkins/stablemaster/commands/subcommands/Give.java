package net.nperkins.stablemaster.commands.subcommands;

import net.nperkins.stablemaster.StableMaster;
import net.nperkins.stablemaster.commands.CommandInfo;
import net.nperkins.stablemaster.commands.SubCommand;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Give extends SubCommand {

    public Give() {
        setMinArgs(1);
        setPermission("stablemaster.give");
    }

    public void handle(CommandInfo commandInfo) {
        final CommandSender sender = commandInfo.getSender();
        final String ownerName = commandInfo.getArg(0);

        StableMaster.getPlugin().getServer().getScheduler().runTaskAsynchronously(StableMaster.getPlugin(), new Runnable() {
                    public void run() {
                        OfflinePlayer rider = StableMaster.getPlugin().getServer().getOfflinePlayer(ownerName);
                        if (rider != null && rider.hasPlayedBefore()) {
                            StableMaster.giveQueue.put((Player) sender, rider);
                            sender.sendMessage(StableMaster.playerMessage("Punch your horse."));
                        } else {
                            sender.sendMessage(StableMaster.playerMessage("We couldn't find that player."));
                        }
                    }
                }
        );
    }

    public String getUsage() {
        return "give <player>";
    }

}
