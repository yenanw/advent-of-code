import java.util.Arrays;

/**
 * A class representing a coordinate with any dimension
 *
 * @author       yenanw
 * @version      1.0
 * @see also     Day17, InfiniteCube
 */
public class Coord {
    private final int[] coords;
    private Integer hashCode;

    /**
     * Constructs a Coord object with the specified coordinates
     * 
     * @param coords The coordinates to be copied from
     */
    public Coord(int[] coords) {
        this.coords = Arrays.copyOf(coords, coords.length);
    }

    /**
     * Returns the coordinates
     * 
     * @return The copy of coordinates
     */
    public int[] getCoords() {
        return Arrays.copyOf(coords, coords.length);
    }

    /**
     * Returns the value from the coordinates on the specified axis
     * 
     * @param index The axis/index in the coordinate system
     * @return The value in that axis
     */
    public int get(int index) {
        return coords[index];
    }

    /**
     * Returns the amount of dimensions
     * 
     * @return The length of the coordinate system
     */
    public int length() {
        return coords.length;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Coord))
            return false;

        Coord coord = (Coord)obj;
        if (this.length() != coord.length())
            return false;
        // two Coords are the same if they have the same coordinates
        for (int i = 0; i < length(); i++) {
            if (get(i) != coord.get(i))
                return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        // to work with HashSet, the hashCode() must be implemented,
        // initially i tried with TreeSet and that was a disaster, use HashSet
        // whenever possible
        if (hashCode == null) {
            int prod = 1;
            for (int i = 0; i < length(); i++) {
                if (get(i) == 0)
                    continue;
                else
                    prod *= get(i);
            }
            hashCode = 37 * prod;
        }
        return hashCode;
    }
}
