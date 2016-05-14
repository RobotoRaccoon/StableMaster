package net.nperkins.stablemaster.commands.subcommands;

import net.nperkins.stablemaster.StableMaster;
import net.nperkins.stablemaster.commands.CommandInfo;
import net.nperkins.stablemaster.commands.SubCommand;
import org.bukkit.command.CommandSender;

public class Help  extends SubCommand {

    public Help() {
        setConsoleAllowed(true);
        setPermission("stablemaster.help");
    }

    public void handle(CommandInfo commandInfo) {

        CommandSender sender = commandInfo.getSender();
        StableMaster.rawMessage(sender, "-------------- StableMaster --------------");
        StableMaster.rawMessage(sender, "Once you tame a horse, you own it.");
        StableMaster.rawMessage(sender, "No claiming required!");
        StableMaster.rawMessage(sender, "/sm help - StableMaster help");
        StableMaster.rawMessage(sender, "/sm addrider <player> - Allow player to ride your horse");
        StableMaster.rawMessage(sender, "/sm delrider <player> - Stop player from riding your horse");
        StableMaster.rawMessage(sender, "/sm give <player> - Give horse to a new owner");
        StableMaster.rawMessage(sender, "/sm teleport - Teleports your horse");
        StableMaster.rawMessage(sender, "/sm rename <name> - Rename horse, colour codes allowed");
        StableMaster.rawMessage(sender, "/sm info - Get info on a horse");
        StableMaster.rawMessage(sender, "-----------------------------------------");
    }

    public String getUsage() {
        return "help";
    }
}
