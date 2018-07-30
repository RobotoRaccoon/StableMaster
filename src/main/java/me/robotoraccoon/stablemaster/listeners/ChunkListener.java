package me.robotoraccoon.stablemaster.listeners;

import me.robotoraccoon.stablemaster.StableMaster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;

public class ChunkListener implements Listener {

    @EventHandler
    public void chunkUnloadEvent(ChunkUnloadEvent event) {
        if (StableMaster.teleportChunk.contains(event.getChunk()))
            event.setCancelled(true);
    }

}
