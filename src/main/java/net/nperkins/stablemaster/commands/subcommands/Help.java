package net.nperkins.stablemaster.commands.subcommands;

import net.nperkins.stablemaster.StableMaster;
import net.nperkins.stablemaster.commands.CommandInfo;
import net.nperkins.stablemaster.commands.CoreCommand;
import net.nperkins.stablemaster.commands.SubCommand;
import org.bukkit.command.CommandSender;

public class Help extends SubCommand {

    public Help() {
        setConsoleAllowed(true);
        setPermission("stablemaster.help");
    }

    public void handle(CommandInfo commandInfo) {

        CommandSender sender = commandInfo.getSender();
        StableMaster.langMessage(sender, "command.help.header");
        StableMaster.langMessage(sender, "command.help.about");

        for (SubCommand cmd : CoreCommand.subCommands.values()) {
            if (sender.hasPermission(cmd.getPermission()) || sender.hasPermission("stablemaster.bypass"))
                StableMaster.rawMessage(sender, String.format(StableMaster.getLang("command.help.format"),
                        cmd.getUsage(), cmd.getDescription()));
        }

        StableMaster.langMessage(sender, "command.help.footer");
    }

    public String getDescription() {
        return StableMaster.getLang("command.help.description");
    }

    public String getUsage() {
        return StableMaster.getLang("command.help.usage");
    }
}
