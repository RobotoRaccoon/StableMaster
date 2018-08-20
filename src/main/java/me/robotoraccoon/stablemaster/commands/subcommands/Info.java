package me.robotoraccoon.stablemaster.commands.subcommands;

import com.google.common.base.Joiner;
import me.robotoraccoon.stablemaster.LangString;
import me.robotoraccoon.stablemaster.StableMaster;
import me.robotoraccoon.stablemaster.StableUtil;
import me.robotoraccoon.stablemaster.commands.CommandInfo;
import me.robotoraccoon.stablemaster.commands.CoreCommand;
import me.robotoraccoon.stablemaster.commands.SubCommand;
import me.robotoraccoon.stablemaster.data.Stable;
import me.robotoraccoon.stablemaster.data.StabledHorse;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Info sub-command, show all information about an animal
 * @author RobotoRaccoon
 */
public class Info extends SubCommand {

    /**
     * Default constructor
     */
    public Info() {
        setOwnerRequired(false);
        setName("info");
    }

    /**
     * {@inheritDoc}
     */
    public void handle(CommandInfo commandInfo) {
        final Player player = (Player) commandInfo.getSender();
        CoreCommand.setQueuedCommand(player, this);
        new LangString("punch-animal").send(player);
    }

    /**
     * {@inheritDoc}
     */
    public void handleInteract(Stable stable, Player player, Tameable animal) {
        final Animals a = (Animals) animal;

        List<String> riderNames = new ArrayList<>();
        boolean isHorse = false;
        if (animal instanceof AbstractHorse) {
            isHorse = true;
            StabledHorse stabledHorse = stable.getHorse((AbstractHorse) animal);
            riderNames = stabledHorse.getRiderNames();
        }

        // Get permission level of user to compare with those in the config.
        ConfigurationSection config = StableMaster.getPlugin().getConfig().getConfigurationSection("command.info");
        Integer permissionLevel;
        if (player == animal.getOwner())
            permissionLevel = 1;
        else if (riderNames.contains(player.getName()))
            permissionLevel = 2;
        else
            permissionLevel = 3;

        // Get the min level for players with the bypass permission.
        if (canBypass(player) && config.getInt("bypass-as-level") < permissionLevel)
            permissionLevel = config.getInt("bypass-as-level");

        // Print the info
        new LangString("command.info.header").send(player);

        // Owner of the animal
        if (config.getInt("owner") >= permissionLevel) {
            new LangString("command.info.owner", animal.getOwner().getName()).send(player);
        }

        // Players allowed to ride the horse
        if (isHorse && config.getInt("permitted-riders") >= permissionLevel) {
            String permitted = riderNames.isEmpty() ? new LangString("command.info.no-riders").getMessage() : Joiner.on(", ").join(riderNames);
            new LangString("command.info.permitted-riders", permitted).send(player);
        }

        // Current and maximum health
        if (config.getInt("health") >= permissionLevel) {
            Double hearts = a.getHealth() / 2;
            Double maxHearts = a.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() / 2;
            new LangString("command.info.health", hearts, maxHearts).send(player);
        }

        // Jump strength
        if (isHorse && config.getInt("jump-strength") >= permissionLevel) {
            Double str = ((AbstractHorse) animal).getJumpStrength();
            Double height = -0.1817584952 * str * str * str + 3.689713992 * str * str + 2.128599134 * str - 0.343930367;
            new LangString("command.info.jump-strength", str, height).send(player);
        }

        // Max Speed
        if (config.getInt("max-speed") >= permissionLevel) {
            Double speed = a.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getValue();
            Double blocksPerSecond = speed * 43.1;
            new LangString("command.info.max-speed", speed, blocksPerSecond).send(player);
        }

        // What type of animal it is
        if (config.getInt("variant") >= permissionLevel) {
            new LangString("command.info.variant", getVariant(a)).send(player);
        }
    }

    /**
     * Get the variant string of an animal
     * @param animal Animal
     * @return The string from lang.yml
     */
    private String getVariant(Animals animal) {
        switch (animal.getType()) {
            case HORSE:
                Horse horse = (Horse) animal;
                String color = new LangString("variant.horse.color." + horse.getColor()).getMessage();
                String style = new LangString("variant.horse.style." + horse.getStyle()).getMessage();
                return color + ", " + style;

            case LLAMA:
                Llama llama = (Llama) animal;
                return new LangString("variant.llama.color." + llama.getColor()).getMessage();

            case OCELOT:
                Ocelot ocelot = (Ocelot) animal;
                return new LangString("variant.ocelot.type." + ocelot.getCatType()).getMessage();

            case PARROT:
                Parrot parrot = (Parrot) animal;
                return new LangString("variant.parrot.variant." + parrot.getVariant()).getMessage();

            default:
                return StableUtil.getAnimal(animal.getType());
        }
    }
}
