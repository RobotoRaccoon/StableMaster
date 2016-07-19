package net.nperkins.stablemaster.commands.subcommands;

import com.google.common.base.Joiner;
import net.nperkins.stablemaster.StableMaster;
import net.nperkins.stablemaster.commands.CommandInfo;
import net.nperkins.stablemaster.commands.SubCommand;
import net.nperkins.stablemaster.data.Stable;
import net.nperkins.stablemaster.data.StabledHorse;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import java.util.List;

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
        final StabledHorse stabledHorse = stable.getHorse(horse);
        final List<String> riderNames = stabledHorse.getRiderNames();

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

        // Owner of the horse
        if (config.getInt("owner") >= permissionLevel) {
            StableMaster.rawMessage(player, String.format(StableMaster.getLang("command.info.owner"), horse.getOwner().getName()));
        }

        // Players allowed to ride the horse
        if (config.getInt("permitted-riders") >= permissionLevel) {
            String permitted = stabledHorse.getRiders().isEmpty() ? "None" : Joiner.on(", ").join(riderNames);
            StableMaster.rawMessage(player, String.format(StableMaster.getLang("command.info.permitted-riders"), permitted));
        }

        // Jump strength
        if (config.getInt("jump-strength") >= permissionLevel) {
            StableMaster.rawMessage(player, String.format(StableMaster.getLang("command.info.jump-strength"), horse.getJumpStrength()));
        }

        // Current and maximum health
        if (config.getInt("health") >= permissionLevel) {
            Double hearts = horse.getHealth() / 2;
            Double maxHearts = horse.getMaxHealth() / 2;
            StableMaster.rawMessage(player, String.format(StableMaster.getLang("command.info.health"), hearts, maxHearts));
        }

        // What type of horse it is
        if (config.getInt("variant") >= permissionLevel) {
            String variant = (horse.getVariant() == Horse.Variant.HORSE) ?
                    horse.getColor() + ", " + horse.getStyle() :
                    horse.getVariant().toString();
            StableMaster.rawMessage(player, String.format(StableMaster.getLang("command.info.variant"), variant));
        }
    }

    public String getDescription() {
        return StableMaster.getLang("command.info.description");
    }

    public String getUsage() {
        return StableMaster.getLang("command.info.usage");
    }
}
