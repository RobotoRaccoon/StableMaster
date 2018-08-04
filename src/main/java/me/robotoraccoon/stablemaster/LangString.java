package me.robotoraccoon.stablemaster;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class LangString {
    private String message;

    public LangString() {
        setMessage("");
    }

    public LangString(String key) {
        setMessage(getLang(key.toLowerCase()));
    }

    public LangString(String key, Object... args) {
        this(key);
        format(args);
    }

    private String getLang(String key) {
        FileConfiguration config = StableUtil.getConfiguration().getLang();
        if (!config.contains(key)) {
            StableMaster.getPlugin().getLogger().warning(String.format("lang.yml path `%s` does not exist!", key));
            return key;
        }
        return config.getString(key);
    }

    public LangString append(String string) {
        setMessage(getMessage() + string);
        return this;
    }

    public LangString format(Object... args) {
        setMessage(String.format(getMessage(), args));
        return this;
    }

    public LangString prefix() {
        setMessage(getLang("prefix") + getMessage());
        return this;
    }

    public void send(CommandSender sender) {
        prefix();
        sender.sendMessage(toString());
    }

    public String getMessage() {
        return this.message;
    }

    private void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return ChatColor.translateAlternateColorCodes('&', getMessage());
    }
}
