import java.util.Collection;
import java.util.Set;
import java.util.HashSet;

/**
 * A class representing whatever thing the day17 challenge specifies, it's
 * far from the most efficient implementation, but as least it's elegant and
 * works within the acceptable time frame
 * 
 * good luck trying this on any dimension higher than 4 though
 * 
 * frankly this challenge probably runs the fastest with hard-coded dimensions,
 * a.k.a. 3 or 4 nested loops
 *
 * @author       yenanw
 * @version      1.0
 * @see also     Day17, Coord
 */
public class InfiniteCube {
    private Set<Coord> activeCoords = new HashSet<>();
    private int dimen;
    private int[] lower;
    private int[] higher;
    
    /**
     * Constructs an infinite cube according to the day17 challenge given a
     * set of start coordinates and a dimension
     * 
     * @param initCoords The coordinates of the active cubes from the start
     * @param dimensions The dimens to execute the cycles in
     */
    public InfiniteCube(Collection<Coord> initCoords, int dimensions) {
        activeCoords.addAll(initCoords);
        dimen = dimensions;
        higher = new int[dimen];
        lower = new int[dimen];
        shrink();
    }

    /**
     * Does a specified amount of cycles with the cube
     * 
     * @param repeat The amount of times to repeat to cycle
     */
    public void cycle(int repeat) {
        for (int i = 0; i < repeat; i++) {
            Set<Coord> modifiedCoord = new HashSet<>(activeCoords);

            cycle(0, new int[dimen], modifiedCoord);

            activeCoords = modifiedCoord;
            shrink();
        }
    }

    /**
     * Counts the size of all active coords
     * 
     * @return The size of all active coords
     */
    public int countActiveCubes() {
        return activeCoords.size();
    }
    
    /**
     * Finds all neighboring cubes of the specified coordinate
     * 
     * @param coord The coordinate to get neighbors from
     * @return A set of all neighbors of the specified coordinate
     */
    public Set<Coord> getNeighbors(Coord coord) {
        Set<Coord> neighbors = getNeighbors(coord, 0, new HashSet<>());
        neighbors.remove(coord);
        return neighbors;
    }

    /**
     * Counts the amount of active neighbors the specified coord has
     * 
     * @param coord The coordinate to get neighbors from
     * @return The amount of active neighbors the specified coord has
     */
    public int countActiveNeighbors(Coord coord) {
        int count = 0;
        for (Coord c : getNeighbors(coord)) {
            if (activeCoords.contains(c))
                count++;
        }
        return count;
    }

    private void cycle(int index, int[] current, Set<Coord> modified) {
        if (index >= dimen)
            return;

        for (int i = lower[index]-1; i <= higher[index]+1; i++) {
            current[index] = i;
            // not sure about the exact complexity of this recursion, but
            // presumably factorial growth lmao
            cycle(index+1, current, modified); 

            // if we are at the end of the recursion
            if (index == dimen-1) {
                Coord coord = new Coord(current);
                int neighborCount = countActiveNeighbors(coord);

                // specifications for activate/inacticate the cubes
                if (activeCoords.contains(coord)) {
                    if (neighborCount != 2 && neighborCount != 3)
                        modified.remove(coord);
                } else {
                    if (neighborCount == 3) 
                        modified.add(coord);
                }
            }
        }
    }

    private Set<Coord> getNeighbors(Coord coord,
                                    int index,
                                    Set<Coord> neighbors) {
        // O(3^n) exponential growth for this recursion
        if (index < coord.length()) {
            int[] coords = coord.getCoords();

            coords[index] = coord.get(index) - 1;
            neighbors.addAll(
                      getNeighbors(new Coord(coords), index+1, neighbors));

            neighbors.addAll(
                      getNeighbors(coord, index+1, neighbors));

            coords[index] = coord.get(index) + 1;
            neighbors.addAll(
                      getNeighbors(new Coord(coords), index+1,neighbors));
        } else {
            neighbors.add(coord);
        }
        return neighbors;
    }

    private void shrink() {
        for (Coord coord : activeCoords) {
           int[] coords = coord.getCoords();
           for (int i = 0; i < coords.length; i++) {
                if (coords[i] > higher[i])
                    higher[i] = coords[i];
                else if (coords[i] < lower[i])
                    lower[i] = coords[i];
           }
        }
    }
}
