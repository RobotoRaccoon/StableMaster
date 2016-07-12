package net.nperkins.stablemaster.commands.subcommands;

import net.nperkins.stablemaster.StableMaster;
import net.nperkins.stablemaster.commands.CommandInfo;
import net.nperkins.stablemaster.commands.SubCommand;
import net.nperkins.stablemaster.data.Stable;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Horse;
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
                            StableMaster.langMessage(sender, "punch-horse");
                        } else {
                            StableMaster.langMessage(sender, "error.player-not-found");
                        }
                    }
                }
        );
    }

    public void handleInteract(Stable stable, Player player, Horse horse) {
        OfflinePlayer newOwner = StableMaster.giveQueue.get(player);

        if (player != horse.getOwner() && !player.hasPermission("stablemaster.bypass")) {
            StableMaster.langMessage(player, "error.not-owner");
            return;
        }

        Stable newStable = StableMaster.getStable(newOwner);

        stable.removeHorse(horse);
        newStable.addHorse(horse);
        horse.setOwner(newOwner);

        StableMaster.rawMessage(player, String.format(
                StableMaster.getLang("command.give.given"), newOwner.getName()));
    }

    public String getDescription() {
        return StableMaster.getLang("command.give.description");
    }

    public String getUsage() {
        return StableMaster.getLang("command.give.usage");
    }
}
