import java.util.ArrayList;
import java.util.List;

/**
 * A class representing a ticket with unknown fields, it is simply a wrapper
 * class for the list of field values
 *
 * @author       yenanw
 * @version      1.0
 * @see also     Day16, TicketScanner
 */
public class Ticket {
    private List<Integer> fields = new ArrayList<>();
    
    /**
     * Appends a list of field values to the ticket
     * 
     * @param fields The list of field on the ticket
     */
    public void addFields(List<Integer> fields) {
        this.fields.addAll(fields);
    }

    /**
     * Returns the values of all fields the ticket has
     * 
     * @return An array of int representing the field values, order preserved
     */
    public int[] getFields() {
        return fields.stream().mapToInt(i->i).toArray();
    }
}
