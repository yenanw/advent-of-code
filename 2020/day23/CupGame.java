import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class representing the cup game described in the day23 challenge
 *
 * @author       yenanw
 * @version      1.0
 * @see also     Day23
 */
public class CupGame {
    // this is what memory-speed tradeoff looks like
    private Map<Integer,Cup> allCups;
    private Cup current;
    private int cupCount;

    /**
     * Constructs the game given an array of labels
     * 
     * @param labels The labels of the cups to be constructed
     */
    public CupGame(int[] labels) {
        current = initCups(labels);
    }

    /**
     * Constructs the game given an array of labels and an amount which
     * determines how many cups to create, the first few cups are always
     * created from the labels
     * 
     * @param labels The labels of the first few cups to be constructed
     */
    public CupGame(int[] labels, int total) {
        current = initCups(labels, total);
    }

    /**
     * Plays the game by moving the cups according the rules
     * 
     * @param t The amount of times the cups are moved
     */
    public void move(int t) {
        for (int i = 0; i < t; i++) {
            List<Cup> picked = pickUp();
            Cup dest = findDestination(picked);
            moveTo(picked, dest);

            current = current.next;
        }
    }

    /**
     * Starting from the current cup, get all labels from all cups in the order
     * they appear
     * 
     * @return The labels of all cups concatednated into a string
     */
    public String getCups() {
        StringBuilder sb = new StringBuilder();
        Cup cup = current;
        for (int i = 0; i < cupCount; i++) {
            sb.append(cup.label);
            cup = cup.next;
        }
        return sb.toString();
    }

    /**
     * Returns the labels of the cups after the specified cup, the amount of
     * cups returned are specified by the given amount
     * 
     * @param label  The label of the specified cup
     * @param amount The amount of cups to be retrieved
     * @return The labels of cups concatenated into a string separated by space
     */
    public String nextCups(int label, int amount) {
        Cup cup = allCups.get(label).next;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < amount; i++) {
            sb.append(cup.label).append(" ");
            cup = cup.next;
        }
        return sb.toString();
    }

    private Cup initCups(int[] init) {
        return initCups(init, init.length);
    }

    private Cup initCups(int[] init, int total) {
        cupCount = total;
        allCups = new HashMap<>();
        Cup[] cups = new Cup[cupCount];
        for (int i = 0; i < cupCount; i++) {
            // initialize all cups
            if (i < init.length)
                cups[i] = new Cup(init[i]);
            else
                cups[i] = new Cup(i+1);
        }

        for (int i = 0; i < cupCount; i++) {
            // link all cups together
            cups[i].prev = cups[Math.floorMod((i-1), cupCount)];
            cups[i].next = cups[Math.floorMod((i+1), cupCount)];
            // map the label to the cup so that we don't need loop the whole
            // linkedlist just to find a single cup, wastes alot of memory tho
            allCups.put(cups[i].label, cups[i]);
        }
        System.out.println("All " + allCups.size() +
                           " cups initialized successfully");
        return cups[0];
    }

    private List<Cup> pickUp() {
        List<Cup> picked = new ArrayList<>(3);
        Cup c = current.next;
        // pick the next 3 cups directly after the current one
        for (int i = 0; i < 3; i++) {
            picked.add(c);
            c = c.next;
        }
        return picked;
    }

    private Cup findDestination(List<Cup> picked) {
        int label = decrementLabel(current.label);
        Cup destination = allCups.get(label);

        while (picked.contains(destination) || destination == null) {
            label = decrementLabel(label);
            destination = allCups.get(label);
        }

        return destination;
    }

    private void moveTo(List<Cup> picked, Cup destination) {
        Cup fst = picked.get(0);
        // the cup at index 1 is ignored since it's always in the middle of
        // the first cup and the third cup
        Cup lst = picked.get(2);

        // assuming the cups are 1 2 3 4 5 and the picked cups are 2 3 4,
        // break the 2 3 4 out so we get 1 5 and 2 3 4
        fst.prev.next = lst.next;
        lst.next.prev = fst.prev;

        // insert the picked cups right after the destination
        Cup afterDest = destination.next;
        destination.next = fst;
        fst.prev = destination;
        afterDest.prev = lst;
        lst.next = afterDest;
    }

    private int decrementLabel(int label) {
        // assuming labels start from 1 and is continuous
        if (label == 1)
            return cupCount;
        return label-1;
    }

    private class Cup {
        // linkedlist but cup, yep
        Cup prev;
        Cup next;
        int label;

        public Cup(int label) {
            this.prev = null;
            this.next = null;
            this.label = label;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Cup))
                return false;

            Cup cup = (Cup)obj;
            return this.label == cup.label;
        }

        @Override
        public int hashCode() {
            return label;
        }

        @Override
        public String toString() {
            return prev.label + " (" + 
                   this.label + ") " +
                   next.label;
        }
    }
}
