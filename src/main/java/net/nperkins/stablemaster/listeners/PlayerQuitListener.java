package net.nperkins.stablemaster.listeners;

import net.nperkins.stablemaster.StableMaster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by Nick on 20/12/2014.
 */
public class PlayerQuitListener implements Listener {


    final StableMaster plugin;

    public PlayerQuitListener (StableMaster p) {
        this.plugin = p;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

        final Player p = event.getPlayer();

        // todo: load this player's horse information into memory
        this.plugin.getLogger().info("{StableMaster] Player quit: " + p.getUniqueId().toString());
        plugin.unloadStable(p);
    }
}