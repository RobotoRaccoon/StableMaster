package net.nperkins.stablemaster.commands.subcommands;

import net.nperkins.stablemaster.StableMaster;
import net.nperkins.stablemaster.commands.CommandInfo;
import net.nperkins.stablemaster.commands.SubCommand;

public class Help  extends SubCommand {

    public Help() {
        setConsoleAllowed(true);
        setPermission("stablemaster.help");
    }

    public void handle(CommandInfo commandInfo) {

        commandInfo.getSender().sendMessage(StableMaster.playerMessage("-------------- StableMaster --------------"));
        commandInfo.getSender().sendMessage(StableMaster.playerMessage("Once you tame a horse, you own it."));
        commandInfo.getSender().sendMessage(StableMaster.playerMessage("No claiming required!"));
        commandInfo.getSender().sendMessage(StableMaster.playerMessage("/sm help - StableMaster help"));
        commandInfo.getSender().sendMessage(StableMaster.playerMessage("/sm addrider <player> - Allow player to ride your horse"));
        commandInfo.getSender().sendMessage(StableMaster.playerMessage("/sm delrider <player> - Stop player from riding your horse"));
        commandInfo.getSender().sendMessage(StableMaster.playerMessage("/sm give <player> - Give horse to a new owner"));
        commandInfo.getSender().sendMessage(StableMaster.playerMessage("/sm teleport - Teleports your horse"));
        commandInfo.getSender().sendMessage(StableMaster.playerMessage("/sm rename <name> - Rename horse, colour codes allowed"));
        commandInfo.getSender().sendMessage(StableMaster.playerMessage("/sm info - Get info on a horse"));
        commandInfo.getSender().sendMessage(StableMaster.playerMessage("-----------------------------------------"));
    }

    public String getUsage() {
        return "help";
    }
}
