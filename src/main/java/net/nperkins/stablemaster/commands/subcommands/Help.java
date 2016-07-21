package net.nperkins.stablemaster.commands.subcommands;

import net.nperkins.stablemaster.StableMaster;
import net.nperkins.stablemaster.commands.CommandInfo;
import net.nperkins.stablemaster.commands.CoreCommand;
import net.nperkins.stablemaster.commands.SubCommand;
import net.nperkins.stablemaster.data.Stable;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

public class Help extends SubCommand {

    public Help() {
        setConsoleAllowed(true);
        setName("help");
    }

    public void handle(CommandInfo commandInfo) {
        CommandSender sender = commandInfo.getSender();

        StableMaster.langMessage(sender, "command.help.header");
        StableMaster.langMessage(sender, "command.help.about");

        for (SubCommand cmd : CoreCommand.subCommands.values()) {
            if (cmd == this)
                continue;

            if (sender.hasPermission(cmd.getPermission()))
                StableMaster.rawMessage(sender, String.format(StableMaster.getLang("command.help.format"),
                        cmd.getUsage(), cmd.getDescription()));
        }

        StableMaster.langMessage(sender, "command.help.footer");
    }

    public void handleInteract(Stable stable, Player player, Horse horse) {
        // Nothing
    }
}
