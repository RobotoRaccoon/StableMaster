package net.nperkins.stablemaster.listeners;

import net.nperkins.stablemaster.StableMaster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {


    final StableMaster plugin;

    public PlayerQuitListener(StableMaster plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

        final Player player = event.getPlayer();

        // Unload the players stable
        plugin.unloadStable(player);
    }
}