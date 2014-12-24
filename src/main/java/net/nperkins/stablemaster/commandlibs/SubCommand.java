package net.nperkins.stablemaster.commandlibs;

public class SubCommand {

    private boolean consoleAllowed = false;
    private SubHandler handler = null;
    private int minArgs = 0;

    public SubCommand(SubHandler handler) {
        this.handler = handler;
    }

    public SubCommand(SubHandler handler, int minArgs) {
        this.handler = handler;
        this.minArgs = minArgs;
    }

    public SubCommand(SubHandler handler, int minArgs, boolean consoleAllowed) {
        this.handler = handler;
        this.minArgs = minArgs;
        this.consoleAllowed = consoleAllowed;
    }

    public boolean isConsoleAllowed() {
        return consoleAllowed;
    }

    public void setConsoleAllowed(boolean consoleAllowed) {
        this.consoleAllowed = consoleAllowed;
    }

    public SubHandler getHandler() {
        return handler;
    }

    public void setHandler(SubHandler handler) {
        this.handler = handler;
    }

    public int getMinArgs() {
        return minArgs;
    }

    public void setMinArgs(int minArgs) {
        this.minArgs = minArgs;
    }
}
