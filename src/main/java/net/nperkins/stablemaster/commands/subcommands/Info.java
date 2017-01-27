package net.nperkins.stablemaster.commands.subcommands;

import com.google.common.base.Joiner;
import net.nperkins.stablemaster.StableMaster;
import net.nperkins.stablemaster.commands.CommandInfo;
import net.nperkins.stablemaster.commands.SubCommand;
import net.nperkins.stablemaster.data.Stable;
import net.nperkins.stablemaster.data.StabledHorse;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.*;

import java.util.List;

public class Info extends SubCommand {

    public Info() {
        setOwnerRequired(false);
        setName("info");
    }

    public void handle(CommandInfo commandInfo) {
        final Player player = (Player) commandInfo.getSender();

        StableMaster.commandQueue.put(player, this);
        StableMaster.langMessage(player, "punch-animal");
    }

    public void handleInteract(Stable stable, Player player, AbstractHorse horse) {
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
        if (canBypass(player) && config.getInt("bypass-as-level") < permissionLevel)
            permissionLevel = config.getInt("bypass-as-level");

        // Print the info
        StableMaster.langMessage(player, "command.info.header");

        // Owner of the horse
        if (config.getInt("owner") >= permissionLevel) {
            StableMaster.langFormat(player, "command.info.owner", horse.getOwner().getName());
        }

        // Players allowed to ride the horse
        if (config.getInt("permitted-riders") >= permissionLevel) {
            String permitted = riderNames.isEmpty() ? StableMaster.getLang("command.info.no-riders") : Joiner.on(", ").join(riderNames);
            StableMaster.langFormat(player, "command.info.permitted-riders", permitted);
        }

        // Current and maximum health
        if (config.getInt("health") >= permissionLevel) {
            Double hearts = horse.getHealth() / 2;
            Double maxHearts = horse.getMaxHealth() / 2;
            StableMaster.langFormat(player, "command.info.health", hearts, maxHearts);
        }

        // Jump strength
        if (config.getInt("jump-strength") >= permissionLevel) {
            Double str = horse.getJumpStrength();
            Double height = -0.1817584952 * str*str*str + 3.689713992 * str*str + 2.128599134 * str - 0.343930367;
            StableMaster.langFormat(player, "command.info.jump-strength", str, height);
        }

        // Max Speed
        if (config.getInt("max-speed") >= permissionLevel) {
            Double speed = horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getValue();
            Double blocksPerSecond = speed * 43.1;
            StableMaster.langFormat(player, "command.info.max-speed", speed, blocksPerSecond);
        }

        // What type of animal it is
        if (config.getInt("variant") >= permissionLevel) {
            String variant;
            if (horse.getType() == EntityType.HORSE) {
                Horse h = (Horse) horse;
                variant = h.getColor() + ", " + h.getStyle();
            } else {
                variant = horse.getType().toString();
            }
            StableMaster.langFormat(player, "command.info.variant", variant);
        }
    }
}
