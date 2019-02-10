package me.robotoraccoon.stablemaster.commands;

import me.robotoraccoon.stablemaster.data.Stable;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;

/**
 * Abstract superclass for every sub-command that requires an interaction with an animal
 * @author RobotoRaccoon
 */
public abstract class InteractCommand extends SubCommand {

    /** Can only the owner run this command */
    private boolean ownerRequired = true;
    /** Can this command be used on any animal or just AbstractHorses */
    private boolean tameablesAllowed = true;

    /**
     * Default constructor, must be called by the sub-class
     * @param name Name of the command
     */
    public InteractCommand(String name) {
        super(name);
    }

    /**
     * Handle a player punching their animal
     * @param stable The players stable
     * @param player Player that hit the animal
     * @param animal Animal interacted with
     */
    public abstract void handleInteract(Stable stable, Player player, Tameable animal);

    /**
     * Should be overridden by sub-classes as required. Removes the player from the internal queue
     * @param player Player to remove
     */
    public void removeFromQueue(Player player) {
        // Do nothing by default
    }

    /**
     * Get if you need to be the owner to use this command
     * @return ownerRequired If only owners can use this command
     */
    public final boolean isOwnerRequired() {
        return ownerRequired;
    }

    /**
     * Set if you need to be the owner to use this command
     * @param ownerRequired If only owners can use this command
     */
    protected final void setOwnerRequired(boolean ownerRequired) {
        this.ownerRequired = ownerRequired;
    }

    /**
     * Get if tameable animals are allowed, or just AbstractHorses
     * @return tameablesAllowed If any tameable animal is allowed
     */
    public final boolean isTameablesAllowed() {
        return tameablesAllowed;
    }

    /**
     * Set if tameable animals are allowed, or just AbstractHorses
     * @param tameablesAllowed If any tameable animal is allowed
     */
    protected final void setTameablesAllowed(boolean tameablesAllowed) {
        this.tameablesAllowed = tameablesAllowed;
    }
}
