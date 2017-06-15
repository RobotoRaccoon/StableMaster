package net.nperkins.stablemaster.commands.subcommands;

import net.nperkins.stablemaster.LangString;
import net.nperkins.stablemaster.StableMaster;
import net.nperkins.stablemaster.commands.CommandInfo;
import net.nperkins.stablemaster.commands.SubCommand;
import net.nperkins.stablemaster.data.Stable;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;

import static net.nperkins.stablemaster.StableMaster.getAnimal;

import java.util.concurrent.ConcurrentHashMap;

public class Rename extends SubCommand {

    private ConcurrentHashMap<Player, String> renameQueue = new ConcurrentHashMap<>();

    public Rename() {
        setMinArgs(1);
        setName("rename");
    }

    public void handle(CommandInfo commandInfo) {

        final Player player = (Player) commandInfo.getSender();
        String name = StringUtils.join(commandInfo.getArgs(), " ");

        if (player.hasPermission("stablemaster.rename.colors"))
            name = ChatColor.translateAlternateColorCodes('&', name);

        renameQueue.put(player, name);
        StableMaster.commandQueue.put(player, this);
        new LangString("punch-animal").send(player);
    }

    public void handleInteract(Stable stable, Player player, Tameable animal) {
        final Animals a = (Animals) animal;
        final ConfigurationSection config = StableMaster.getPlugin().getConfig().getConfigurationSection("command.rename");
        String name = renameQueue.get(player);
        removeFromQueue(player);

        a.setCustomName(name);
        a.setCustomNameVisible(config.getBoolean("name-always-visible"));
        new LangString("command.rename.renamed", getAnimal(a.getType()), name).send(player);
    }

    @Override
    public void removeFromQueue(Player player) {
        renameQueue.remove(player);
    }
}
