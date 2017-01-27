package net.nperkins.stablemaster.commands.subcommands;

import net.nperkins.stablemaster.StableMaster;
import net.nperkins.stablemaster.commands.CommandInfo;
import net.nperkins.stablemaster.commands.CoreCommand;
import net.nperkins.stablemaster.commands.SubCommand;
import net.nperkins.stablemaster.data.Stable;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;

import java.util.Map;

public class Help extends SubCommand {

    public Help() {
        setConsoleAllowed(true);
        setOwnerRequired(false);
        setName("help");
    }

    public void handle(CommandInfo commandInfo) {
        CommandSender sender = commandInfo.getSender();

        StableMaster.langMessage(sender, "command.help.header");
        StableMaster.langMessage(sender, "command.help.about");

        for (Map.Entry<String, SubCommand> entry : CoreCommand.subCommands.entrySet()) {
            String name = entry.getKey();
            SubCommand cmd = entry.getValue();

            if (cmd == this || !name.equalsIgnoreCase(cmd.getName()))
                continue;

            if (sender.hasPermission(cmd.getPermission()))
                StableMaster.langFormat(sender, "command.help.format", cmd.getUsage(), cmd.getDescription());
        }

        StableMaster.langMessage(sender, "command.help.footer");
    }

    public void handleInteract(Stable stable, Player player, AbstractHorse horse) {
        // Nothing
    }
}
