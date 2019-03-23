package me.robotoraccoon.stablemaster.listeners;

import me.robotoraccoon.stablemaster.StableMaster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;

/**
 * Listener to not allow a chunk to be unloaded while a teleport event is pending for an animal within it
 * @author RobotoRaccoon
 */
public class ChunkListener implements Listener {

    /**
     * Cancel chunk unloads if there is a pending teleport
     * @param event Event
     */
    @EventHandler(ignoreCancelled = true)
    public void chunkUnloadEvent(ChunkUnloadEvent event) {
        if (StableMaster.getTeleportChunk().contains(event.getChunk()))
            event.setCancelled(true);
    }

}
