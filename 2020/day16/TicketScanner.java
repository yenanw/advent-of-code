import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A class representing a set of intervals
 *
 * @author       yenanw
 * @version      1.0
 * @see also     Day16, Ticket, Interval
 */
public class TicketScanner {
    private Map<String,Interval> rules = new HashMap<>();

    /**
     * Adds/Updates a rule for a given field
     * 
     * @param field    The field which the rules apply to
     * @param interval The rules, or the interval this field's value must be in
     */
    public void putRule(String field, Interval interval) {
        rules.put(field, interval);
    }

    /**
     * Computes which index should be mapped to which field
     * 
     * @param tickets A list of valid tickets
     * @return A map where the key is the index and the value is the
     *         corresponding field
     */
    public Map<Integer,String> determineFields(List<Ticket> tickets) {
        int[][] tMatrix = toMatrix(tickets);
        Map<Integer,Set<String>> possibleFields = new HashMap<>();

        for (int c = 0; c < tMatrix[0].length; c++) {
            for (String field : rules.keySet()) {
                boolean possible = true;
                // test if all field value on index c is in the interval the
                // field specifies
                for (int r = 0; r < tMatrix.length; r++) {
                    Interval interval = rules.get(field);
                    if (!interval.isInRange(tMatrix[r][c])) {
                        possible = false;
                        break;
                    }
                }
                // if the above requirement qualifies, then map the index to
                // the field
                if (possible) {
                    if (!possibleFields.containsKey(c)) {
                        Set<String> fields = new HashSet<>();
                        fields.add(field);
                        possibleFields.put(c, fields);
                    } else {
                        possibleFields.get(c).add(field);
                    }
                }
            }
        }
        // reduce all possible fields to a single field
        return shrinkPossiblities(possibleFields);
    }

    /**
     * Returns all invalid values from a ticket
     * 
     * @param ticket The ticket to be checked
     * @return An array containing all invalid values
     */
    public int[] invalidValues(Ticket ticket) {
        List<Integer> invalid = new ArrayList<>();
        for (int f : ticket.getFields()) {
            if (!isValidForAny(f))
                invalid.add(f);
        }
        return invalid.stream().mapToInt(i->i).toArray();
    }

    /**
     * Checks if any field of a ticket contains an invalid value, meaning it
     * cannot be applied to any rule/field
     * 
     * @param ticket The ticket to be checked
     * @return true if any of the ticket's value is invalid, else false
     */
    public boolean isInvalid(Ticket ticket) {
        for (int field : ticket.getFields()) {
            if (!isValidForAny(field))
                return true;
        }
        return false;
    }

    /**
     * Checks if a number/value can be applied to any rule/field
     * 
     * @param num The number to be checked
     * @return true if the number is in range for any field, else false
     */
    public boolean isValidForAny(int num) {
        for (String field : rules.keySet()) {
            if (rules.get(field).isInRange(num))
                return true;
        }
        return false;
    }

    private int[][] toMatrix(List<Ticket> tickets) {
        return tickets.stream()
               .map(t->t.getFields())
               .toArray(int[][]::new);
    }

    private Map<Integer,String> shrinkPossiblities(
                                Map<Integer,Set<String>> m) {
        Map<Integer,String> shrunk = new HashMap<>();
        // copy the map to prevent alias problem
        Map<Integer,Set<String>> possiblities = new HashMap<>(m);
        
        // keep removing the fields which we already know that cannot be mapped
        // to another index, until the map is empty, a field cannot be mapped
        // to anotehr index if the index mapped to the field contains only this
        // field, hence, this method is dependent on an appropriate input
        while (possiblities.size() != 0) {
            String field = "";
            for (int i : possiblities.keySet()) {
                Set<String> fields = possiblities.get(i);
                if (fields.size() == 1) {
                    for (String f : fields) {
                        field = f;
                    }
                    possiblities.remove(i);
                    shrunk.put(i, field);
                    break;
                }
            }

            for (int i : possiblities.keySet())
                possiblities.get(i).remove(field);
        }
        return shrunk;
    }
}
