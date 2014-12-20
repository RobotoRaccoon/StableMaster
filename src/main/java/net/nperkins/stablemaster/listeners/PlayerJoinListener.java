package net.nperkins.stablemaster.listeners;


import net.nperkins.stablemaster.StableMaster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener{

    final StableMaster plugin;

    public PlayerJoinListener (StableMaster p) {
        this.plugin = p;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        final Player p = event.getPlayer();

        // todo: load this player's horse information into memory
        this.plugin.getLogger().info("{StableMaster] Player joined: " + p.getUniqueId().toString());
        plugin.loadStable(p);
    }
}
