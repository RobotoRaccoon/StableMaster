package net.nperkins.stablemaster.commandlibs;

import java.util.List;


public interface SubHandler {

    public void handle(CommandInfo commandInfo);

    public List<String> handleComplete(CommandInfo commandInfo);

    public String handleHelp();
}
