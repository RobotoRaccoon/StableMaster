package me.robotoraccoon.stablemaster.data;

import org.bukkit.entity.Animals;

import java.util.ArrayList;
import java.util.List;

/**
 * A class to contain a list of unique animals that can be popped
 * @author RobotoRaccoon
 */
public class AnimalSet {

    /** The internal list */
    private List<Animals> list = new ArrayList<>();

    /**
     * Add an animal to the set
     * @param animal Animal to add
     * @return True if animal was added, false if the set already contained the animal
     */
    public boolean add(Animals animal) {
        if (!list.contains(animal)) {
            list.add(animal);
            return true;
        }
        return false;
    }

    /**
     * Retrieve the last unique animal added. No guarantee of ordering
     * @return An animal from the set, null if the set is empty
     */
    public Animals pop() {
        return isEmpty() ? null : list.remove(list.size() - 1);
    }

    /**
     * Check if the set is empty
     * @return True if empty, false otherwise
     */
    public boolean isEmpty() {
        return list.isEmpty();
    }
}
