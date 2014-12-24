package net.nperkins.stablemaster.listeners;


import net.nperkins.stablemaster.StableMaster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    final StableMaster plugin;

    public PlayerJoinListener(StableMaster plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        final Player player = event.getPlayer();
        // Load the player's stable
        plugin.loadStable(player);
    }
}
