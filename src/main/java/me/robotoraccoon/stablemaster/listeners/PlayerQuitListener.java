package me.robotoraccoon.stablemaster.listeners;

import me.robotoraccoon.stablemaster.StableUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Listener to save stables upon player logout
 * @author RobotoRaccoon
 */
public class PlayerQuitListener implements Listener {

    /**
     * Event where a player logs off
     * @param event Event
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Unload the players stable
        StableUtil.unloadStable(event.getPlayer());
    }
}