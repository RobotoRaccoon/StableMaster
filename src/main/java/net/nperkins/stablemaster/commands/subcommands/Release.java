package net.nperkins.stablemaster.commands.subcommands;

import net.nperkins.stablemaster.StableMaster;
import net.nperkins.stablemaster.commands.CommandInfo;
import net.nperkins.stablemaster.commands.SubCommand;
import net.nperkins.stablemaster.data.Stable;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Release extends SubCommand {

    public Release() {
        setName("release");
    }

    public void handle(CommandInfo commandInfo) {
        final Player player = (Player) commandInfo.getSender();

        StableMaster.commandQueue.put(player, this);
        StableMaster.langMessage(player, "punch-animal");
    }

    public void handleInteract(Stable stable, Player player, AbstractHorse horse) {
        final ConfigurationSection config = StableMaster.getPlugin().getConfig().getConfigurationSection("command.info");
        final Inventory inv = horse.getInventory();

        // Saddle must drop, else it cannot be tamed later
        ItemStack[] contents = inv.getContents();
        if (contents[0] != null) {
            horse.getWorld().dropItemNaturally(horse.getLocation(), contents[0]);
            contents[0] = null;
            inv.setContents(contents);
        }

        if (config.getBoolean("clear-custom-name")) {
            horse.setCustomNameVisible(false);
            horse.setCustomName("");
        }

        // Finally remove ownership of the horse
        horse.setTamed(false);
        stable.removeHorse(horse);

        StableMaster.langFormat(player, "command.release.released", horse.getType());
    }
}
