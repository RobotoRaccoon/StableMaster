package net.nperkins.stablemaster.listeners;


import net.nperkins.stablemaster.StableMaster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Load the player's stable
        StableMaster.loadStable(event.getPlayer());
    }
}
