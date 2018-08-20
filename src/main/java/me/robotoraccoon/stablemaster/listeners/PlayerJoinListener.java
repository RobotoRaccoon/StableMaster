package me.robotoraccoon.stablemaster.listeners;

import me.robotoraccoon.stablemaster.StableUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Listener to load a players stable upon join
 * @author RobotoRaccoon
 */
public class PlayerJoinListener implements Listener {

    /**
     * Event where a player logs on
     * @param event Event
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Load the player's stable
        StableUtil.loadStable(event.getPlayer());
    }
}
