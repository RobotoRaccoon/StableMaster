package net.nperkins.stablemaster.commands.subcommands;

import com.google.common.base.Joiner;
import net.nperkins.stablemaster.StableMaster;
import net.nperkins.stablemaster.commands.CommandInfo;
import net.nperkins.stablemaster.commands.SubCommand;
import net.nperkins.stablemaster.data.Stable;
import net.nperkins.stablemaster.data.StabledHorse;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
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
        final Player player = (Player) commandInfo.getSender();

        StableMaster.commandQueue.put(player, this);
        StableMaster.langMessage(player, "punch-horse");
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

        // Get permission level of user to compare with those in the config.
        ConfigurationSection config = StableMaster.getPlugin().getConfig().getConfigurationSection("command.info");
        Integer permissionLevel;
        if (player == horse.getOwner())
            permissionLevel = 1;
        else if (riderNames.contains(player.getName()))
            permissionLevel = 2;
        else
            permissionLevel = 3;

        // Get the min level for players with the bypass permission.
        if (player.hasPermission("stablemaster.bypass.info") && config.getInt("bypass-as-level") < permissionLevel)
            permissionLevel = config.getInt("bypass-as-level");

        // Print the info
        StableMaster.langMessage(player, "command.info.header");
        if (config.getInt("owner") >= permissionLevel)
            StableMaster.rawMessage(player, String.format(StableMaster.getLang("command.info.owner"), horse.getOwner().getName()));
        if (config.getInt("permitted-riders") >= permissionLevel)
            StableMaster.rawMessage(player, String.format(StableMaster.getLang("command.info.permitted-riders"), permitted));
        if (config.getInt("jump-strength") >= permissionLevel)
            StableMaster.rawMessage(player, String.format(StableMaster.getLang("command.info.jump-strength"), horse.getJumpStrength()));
        if (config.getInt("max-health") >= permissionLevel)
            StableMaster.rawMessage(player, String.format(StableMaster.getLang("command.info.max-health"), horse.getMaxHealth()));
        if (config.getInt("variant") >= permissionLevel)
            StableMaster.rawMessage(player, String.format(StableMaster.getLang("command.info.variant"), variant));
    }

    public String getDescription() {
        return StableMaster.getLang("command.info.description");
    }

    public String getUsage() {
        return StableMaster.getLang("command.info.usage");
    }
}
