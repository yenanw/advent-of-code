/**
 * Helper class for solving the day3 challenge
 *
 * @author       yenanw
 * @version      1.0
 * @see also     Day1
 */
public class SlopeTraversal {
    private int[][] map;
    private int dx;
    private int dy;

    /**
     * Constructor
     * 
     * @param map The integer matrix to traverse in
     * @param dx  The step it takes in x-axis every iteration
     * @param dy  The step it takes in y-axis every iteration
     */
    public SlopeTraversal(int[][] map, int dx, int dy) {
        this.map = map;
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * Traverse the map from (0,0) to the last row and count the amount of
     * times it encountered the number n
     * 
     * @param n The number to be checked for encounter
     * @return The number of times n appears on the slope
     */
    public int traverseToBottom(int n) {
        int count = 0;
        int c = 0;
        for (int r = 0; r < map.length; r += dy) {
            if (map[r][c] == n)
                count++;
            // use modulo to keep traversing on the same map
            c = (c+dx) % map[r].length;
        }
        return count;
    }
}
