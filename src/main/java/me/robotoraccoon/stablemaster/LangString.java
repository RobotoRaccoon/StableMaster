package me.robotoraccoon.stablemaster;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Stores a string that can be read in from lang.yml, be added to, and sent to a CommandSender
 * @author RobotoRaccoon
 */
public class LangString {

    /** The stored message */
    private String message;

    /**
     * Default constructor, empty string
     */
    public LangString() {
        setMessage("");
    }

    /**
     * Constructor, load string from lang.yml
     * @param key The lang to load
     */
    public LangString(String key) {
        setMessage(getLang(key.toLowerCase()));
    }

    /**
     * Constructor, load string from lang.yml and format with args
     * @param key The lang to load
     * @param args The arguments to format with
     */
    public LangString(String key, Object... args) {
        this(key);
        format(args);
    }

    /**
     * Get the saved string from lang.yml
     * @param key Path to lookup
     * @return String saved at key, or key if the string is not found
     */
    private String getLang(String key) {
        FileConfiguration config = Configuration.getLang();
        if (!config.contains(key)) {
            StableMaster.getPlugin().getLogger().warning(String.format("lang.yml path `%s` does not exist!", key));
            return key;
        }
        return config.getString(key);
    }

    /**
     * Add on to the end of the current message
     * @param string What to append
     * @return this
     */
    public LangString append(String string) {
        setMessage(getMessage() + string);
        return this;
    }

    /**
     * Format the current message with the given arguments
     * @param args What to add into the string
     * @return this
     */
    public LangString format(Object... args) {
        setMessage(String.format(getMessage(), args));
        return this;
    }

    /**
     * Append the prefix to the message
     * @return this
     */
    public LangString prefix() {
        setMessage(getLang("prefix") + getMessage());
        return this;
    }

    /**
     * Send the message to the given player
     * @param sender Send the message with a prefix
     */
    public void send(CommandSender sender) {
        prefix();
        sender.sendMessage(toString());
    }

    /**
     * Get the message contents
     * @return The message contents
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Set the message contents
     * @param message What to overwrite message with
     */
    private void setMessage(String message) {
        this.message = message;
    }

    /**
     * Translate colour codes and return message
     * @return The translated message
     */
    @Override
    public String toString() {
        return ChatColor.translateAlternateColorCodes('&', getMessage());
    }
}
