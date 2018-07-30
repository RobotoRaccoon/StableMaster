package me.robotoraccoon.stablemaster.commands.subcommands;

import me.robotoraccoon.stablemaster.LangString;
import me.robotoraccoon.stablemaster.StableMaster;
import me.robotoraccoon.stablemaster.commands.CommandInfo;
import me.robotoraccoon.stablemaster.commands.SubCommand;
import me.robotoraccoon.stablemaster.data.Stable;
import me.robotoraccoon.stablemaster.data.StabledHorse;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;

import java.util.concurrent.ConcurrentHashMap;

public class AddRider extends SubCommand {

    private ConcurrentHashMap<Player, OfflinePlayer> addRiderQueue = new ConcurrentHashMap<>();

    public AddRider() {
        setTameablesAllowed(false);
        setMinArgs(1);
        setName("addrider");
    }

    public void handle(CommandInfo commandInfo) {
        final Player player = (Player) commandInfo.getSender();
        final String riderName = commandInfo.getArg(0);

        OfflinePlayer rider = StableMaster.getPlugin().getServer().getOfflinePlayer(riderName);
        if (rider != null && rider.hasPlayedBefore()) {
            StableMaster.commandQueue.put(player, this);
            addRiderQueue.put(player, rider);
            new LangString("punch-animal").send(player);
        } else {
            new LangString("error.player-not-found").send(player);
        }
    }

    public void handleInteract(Stable stable, Player player, Tameable animal) {
        final AbstractHorse horse = (AbstractHorse) animal;
        StabledHorse stabledHorse = stable.getHorse(horse);
        OfflinePlayer rider = addRiderQueue.get(player);
        removeFromQueue(player);

        if (stabledHorse.isRider(rider)) {
            new LangString("command.addrider.is-rider", rider.getName()).send(player);
        } else {
            stabledHorse.addRider(rider);
            new LangString("command.addrider.added", rider.getName(), StableMaster.getAnimal(horse.getType())).send(player);
        }
    }

    @Override
    public void removeFromQueue(Player player) {
        addRiderQueue.remove(player);
    }
}
