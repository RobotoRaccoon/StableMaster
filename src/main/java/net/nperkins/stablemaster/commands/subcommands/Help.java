package net.nperkins.stablemaster.commands.subcommands;

import net.nperkins.stablemaster.LangString;
import net.nperkins.stablemaster.StableMaster;
import net.nperkins.stablemaster.commands.CommandInfo;
import net.nperkins.stablemaster.commands.CoreCommand;
import net.nperkins.stablemaster.commands.SubCommand;
import net.nperkins.stablemaster.data.Stable;
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

        for (Map.Entry<String, SubCommand> entry : CoreCommand.subCommands.entrySet()) {
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
