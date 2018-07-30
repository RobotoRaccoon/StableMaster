package me.robotoraccoon.stablemaster.listeners;

import me.robotoraccoon.stablemaster.StableMaster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Unload the players stable
        StableMaster.unloadStable(event.getPlayer());
    }
}