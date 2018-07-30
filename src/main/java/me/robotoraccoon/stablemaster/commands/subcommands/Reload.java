package me.robotoraccoon.stablemaster.commands.subcommands;

import me.robotoraccoon.stablemaster.LangString;
import me.robotoraccoon.stablemaster.StableMaster;
import me.robotoraccoon.stablemaster.commands.CommandInfo;
import me.robotoraccoon.stablemaster.commands.SubCommand;
import me.robotoraccoon.stablemaster.data.Stable;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;

public class Reload extends SubCommand {

    public Reload() {
        setConsoleAllowed(true);
        setName("reload");
    }

    public void handle(CommandInfo commandInfo) {
        CommandSender sender = commandInfo.getSender();
        StableMaster.loadConfigData();
        new LangString("command.reload.reloaded").send(sender);
    }

    public void handleInteract(Stable stable, Player player, Tameable animal) {
        // Nothing
    }
}
