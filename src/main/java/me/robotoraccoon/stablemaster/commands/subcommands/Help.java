package me.robotoraccoon.stablemaster.commands.subcommands;

import me.robotoraccoon.stablemaster.LangString;
import me.robotoraccoon.stablemaster.commands.CommandInfo;
import me.robotoraccoon.stablemaster.commands.CoreCommand;
import me.robotoraccoon.stablemaster.commands.SubCommand;
import me.robotoraccoon.stablemaster.data.Stable;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;

import java.util.Map;

public class Help extends SubCommand {

    public Help() {
        setConsoleAllowed(true);
        setOwnerRequired(false);
        setName("help");
    }

    public void handle(CommandInfo commandInfo) {
        CommandSender sender = commandInfo.getSender();

        new LangString("command.help.header").send(sender);
        new LangString("command.help.about").send(sender);

        for (Map.Entry<String, SubCommand> entry : CoreCommand.getSubCommands()) {
            String name = entry.getKey();
            SubCommand cmd = entry.getValue();

            if (cmd == this || !name.equalsIgnoreCase(cmd.getName()))
                continue;

            if (sender.hasPermission(cmd.getPermission()))
                new LangString("command.help.format", cmd.getUsage(), cmd.getDescription()).send(sender);
        }

        new LangString("command.help.footer").send(sender);
    }

    public void handleInteract(Stable stable, Player player, Tameable animal) {
        // Nothing
    }
}
