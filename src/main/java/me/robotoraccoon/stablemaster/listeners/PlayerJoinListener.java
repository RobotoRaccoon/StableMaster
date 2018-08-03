package me.robotoraccoon.stablemaster.listeners;


import me.robotoraccoon.stablemaster.StableUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Load the player's stable
        StableUtil.loadStable(event.getPlayer());
    }
}
