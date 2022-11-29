import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A memory game class based on the day15 challenge
 *
 * @author       yenanw
 * @version      1.0
 * @see also     Day15
 */
public class MemoryGame {
    private int[] startingNums;
    private Map<Integer,List<Integer>> spoken;

    /**
     * Constructs the memory game by giving a set of starting values
     * 
     * @param startingNums The numbers required to start the memory game
     */
    public MemoryGame(int[] startingNums) {
        this.startingNums = startingNums;
        this.spoken = new HashMap<>();
    }

    /**
     * Play the game according the set of rules defined in the day15 challenge,
     * until the current turn is greater than the parameter t
     * 
     * @param t The turn where the game shall end at
     * @return The number spoken at the turn given by the parameter, assuming
     *         all arguments are correct, else -1
     */
    public int playUntil(int t) {
        int turn = 1;
        int last = -1;
        
        // starting up the game by speaking all starting numbers in sequence
        for (int i = 0; i < startingNums.length; i++) {
            speak(startingNums[i], turn);
            last = startingNums[i];
            turn++;
        }

        do {
            last = countLast(last);
            speak(last, turn);
            turn++;
        } while (turn <= t);
        return last;
    }
    
    /**
     * Clears any saved instances from previous calls of the playUntil() method
     */
    public void restart() {
        spoken.clear();
    }

    private int countLast(int last) {
        int result;
        if (!spoken.containsKey(last) || spoken.get(last).size() < 2) {
            result = 0;
        } else {
            List<Integer> list = spoken.get(last);
            int n1 = list.get(list.size()-1);
            int n2 = list.get(list.size()-2);
            result = n1 - n2;
        }
        return result;
    }

    private void speak(int num, int turn) {
        List<Integer> list;
        if (spoken.containsKey(num)) {
            list = spoken.get(num);
            list.add(turn);
            while (list.size() > 2) {
                list.remove(0);
            }
        } else {
            list = new ArrayList<>();
            list.add(turn);
            spoken.put(num, list);
        }
    }
}
