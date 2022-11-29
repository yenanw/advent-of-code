import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A class representing a set of intervals
 *
 * @author       yenanw
 * @version      1.0
 * @see also     Day16, TicketScanner
 */
public class Interval {
   private List<Integer[]> intervals = new ArrayList<>();

   /**
    * Adds a new possible range to be checked
    *
    * @param interval An int array containing at least 2 numbers, only the
    *                 the first two numbers will be used at interval
    */
    public void addInterval(int[] interval) {
        intervals.add(toIntegerArray(interval));
    }

    /**
     * Checks if the integer n is in any of the intervals
     * 
     * @param n The number to be checks
     * @return true if n is in any of the intervals, else false
     */
    public boolean isInRange(int n) {
        for (Integer[] interval : intervals) {
            if (n >= interval[0] && n <= interval[1])
                return true;        
        }
        return false;
    }

    private Integer[] toIntegerArray(int[] intArr) {
        return Arrays.stream(intArr)
               .mapToObj(i->Integer.valueOf(i))
               .toArray(Integer[]::new);
    }
}
