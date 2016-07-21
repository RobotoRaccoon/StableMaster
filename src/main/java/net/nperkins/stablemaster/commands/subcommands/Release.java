package net.nperkins.stablemaster.commands.subcommands;

import net.nperkins.stablemaster.StableMaster;
import net.nperkins.stablemaster.commands.CommandInfo;
import net.nperkins.stablemaster.commands.SubCommand;
import net.nperkins.stablemaster.data.Stable;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.HorseInventory;
import org.bukkit.inventory.ItemStack;

public class Release extends SubCommand {

    public Release() {
        setName("release");
    }

    public void handle(CommandInfo commandInfo) {
        final Player player = (Player) commandInfo.getSender();

        StableMaster.commandQueue.put(player, this);
        StableMaster.langMessage(player, "punch-horse");
    }

    public void handleInteract(Stable stable, Player player, Horse horse) {
        if (player != horse.getOwner() && !canBypass(player)) {
            StableMaster.langMessage(player, "error.not-owner");
            return;
        }

        final ConfigurationSection config = StableMaster.getPlugin().getConfig().getConfigurationSection("command.info");
        final HorseInventory inv = horse.getInventory();

        // Saddle must drop, else it cannot be tamed later
        if (inv.getSaddle() != null) {
            horse.getWorld().dropItemNaturally(horse.getLocation(), inv.getSaddle());
            inv.setSaddle(null);
        }

        if (config.getBoolean("clear-custom-name")) {
            horse.setCustomNameVisible(false);
            horse.setCustomName("");
        }

        // Finally remove ownership of the horse
        horse.setTamed(false);
        stable.removeHorse(horse);

        StableMaster.langMessage(player, "command.release.released");
    }
}
