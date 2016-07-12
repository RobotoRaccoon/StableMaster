package net.nperkins.stablemaster.commands.subcommands;

import com.google.common.base.Joiner;
import net.nperkins.stablemaster.StableMaster;
import net.nperkins.stablemaster.commands.CommandInfo;
import net.nperkins.stablemaster.commands.SubCommand;
import net.nperkins.stablemaster.data.Stable;
import net.nperkins.stablemaster.data.StabledHorse;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

public class Info extends SubCommand {

    public Info() {
        setPermission("stablemaster.info");
    }

    public void handle(CommandInfo commandInfo) {
        final CommandSender sender = commandInfo.getSender();

        StableMaster.getPlugin().getServer().getScheduler().runTaskAsynchronously(StableMaster.getPlugin(), new Runnable() {
                    public void run() {
                        if (!StableMaster.infoQueue.contains(sender)) {
                            StableMaster.infoQueue.add((Player) sender);
                        }
                        StableMaster.langMessage(sender, "punch-horse");
                    }
                }
        );
    }

    public void handleInteract(Stable stable, Player player, Horse horse) {
        StabledHorse stabledHorse = stable.getHorse(horse);
        ArrayList<String> riderNames = new ArrayList<String>();
        Iterator it = stabledHorse.getRiders().iterator();
        while (it.hasNext()) {
            OfflinePlayer rider = StableMaster.getPlugin().getServer().getOfflinePlayer(UUID.fromString((String) it.next()));
            if (rider.getName() == null) {
                //todo: some sort of lookup
                riderNames.add(rider.getUniqueId().toString());
            } else {
                riderNames.add(rider.getName());
            }
        }

        String permitted = stabledHorse.getRiders().isEmpty() ? "None" : Joiner.on(", ").join(riderNames);
        String variant = (horse.getVariant() == Horse.Variant.HORSE) ?
                horse.getColor() + ", " + horse.getStyle() :
                horse.getVariant().toString();

        // Print the info
        StableMaster.langMessage(player, "command.info.header");
        StableMaster.rawMessage(player, String.format(StableMaster.getLang("command.info.owner"), horse.getOwner().getName()));
        StableMaster.rawMessage(player, String.format(StableMaster.getLang("command.info.permitted-riders"), permitted));
        StableMaster.rawMessage(player, String.format(StableMaster.getLang("command.info.jump-strength"), horse.getJumpStrength()));
        StableMaster.rawMessage(player, String.format(StableMaster.getLang("command.info.variant"), variant));
    }

    public String getDescription() {
        return StableMaster.getLang("command.info.description");
    }

    public String getUsage() {
        return StableMaster.getLang("command.info.usage");
    }
}
