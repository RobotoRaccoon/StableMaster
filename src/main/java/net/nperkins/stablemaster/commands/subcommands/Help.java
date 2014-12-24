package net.nperkins.stablemaster.commands.subcommands;

import net.nperkins.stablemaster.StableMaster;
import net.nperkins.stablemaster.commandlibs.CommandInfo;
import net.nperkins.stablemaster.commandlibs.SubHandler;

import java.util.List;


public class Help implements SubHandler {

    private StableMaster plugin;

    public Help(StableMaster p) {
        this.plugin = p;
    }

    public void handle(CommandInfo commandInfo) {

        commandInfo.getSender().sendMessage(StableMaster.playerMessage("-------------- StableMaster --------------"));
        commandInfo.getSender().sendMessage(StableMaster.playerMessage("Once you tame a horse, you own it."));
        commandInfo.getSender().sendMessage(StableMaster.playerMessage("No claiming required!"));
        commandInfo.getSender().sendMessage(StableMaster.playerMessage("Trading/swapping horses coming soon!"));
        commandInfo.getSender().sendMessage(StableMaster.playerMessage("/sm help - StableMaster help"));
        commandInfo.getSender().sendMessage(StableMaster.playerMessage("/sm addrider <player> - Allow player"));
        commandInfo.getSender().sendMessage(StableMaster.playerMessage("  to ride your horse"));
        commandInfo.getSender().sendMessage(StableMaster.playerMessage("/sm delrider <player> - Stop player"));
        commandInfo.getSender().sendMessage(StableMaster.playerMessage("  from riding your horse"));
        commandInfo.getSender().sendMessage(StableMaster.playerMessage("-----------------------------------------"));
    }

    public List<String> handleComplete(CommandInfo commandInfo) {
        return null;
    }

    public String handleHelp() {
        return "help";
    }
}
