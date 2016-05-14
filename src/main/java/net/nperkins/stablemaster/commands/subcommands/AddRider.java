package net.nperkins.stablemaster.commands.subcommands;

import net.nperkins.stablemaster.StableMaster;
import net.nperkins.stablemaster.commands.CommandInfo;
import net.nperkins.stablemaster.commands.SubCommand;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddRider extends SubCommand {

    public AddRider() {
        setMinArgs(1);
        setPermission("stablemaster.addrider");
    }

    public void handle(CommandInfo commandInfo) {
        final CommandSender sender = commandInfo.getSender();
        final String riderName = commandInfo.getArg(0);

        StableMaster.getPlugin().getServer().getScheduler().runTaskAsynchronously(StableMaster.getPlugin(), new Runnable() {
                    public void run() {
                        OfflinePlayer rider = StableMaster.getPlugin().getServer().getOfflinePlayer(riderName);
                        if (rider != null && rider.hasPlayedBefore()) {
                            StableMaster.addRiderQueue.put((Player) sender, rider);
                            StableMaster.rawMessage(sender, "Punch your horse.");
                        } else {
                            StableMaster.rawMessage(sender, "We couldn't find that player.");
                        }
                    }
                }
        );
    }

    public String getUsage() {
        return "addrider <player>";
    };

}
