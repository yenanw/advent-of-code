import java.util.ArrayList;
import java.util.List;


/**
 * A class representing an image tile described in the day20 challenge, it
 * functions sort of like a linked list but with 4 directions
 *
 * @author       yenanw
 * @version      1.0
 * @see also     Day20, TileManager
 */
public class Tile {
    // ugly public fields for easier access for my sake
    public Tile top = null;
    public Tile bottom = null;
    public Tile left = null;
    public Tile right = null;
    // to make sure we can always reset a tile
    private int[][] defaultTile;
    // the current tile
    private int[][] tile;
    private int id;
    // all of the possible arrangements of a tile, meaning all the combinations
    // of the rotations and vertical/horizontal flips, excluding duplicates
    private List<int[][]> arrangements;

    /**
     * Constructs a tile by computing all possible arrangements of the specified
     * tile and ID
     * 
     * @param tile The image tile
     * @param id   The ID of the image tile
     */
    public Tile(int[][] tile, int id) {
        this.tile = tile;
        arrangements = initArrangements(copy(tile));
        defaultTile = copy(tile);
        this.id = id;
    }

    /**
     * @return The ID of the image tile
     */
    public int getID() {
        return id;
    }

    /**
     * Sets the current tile as the specified int matrix
     * 
     * @param tile The speified tile to be set
     */
    public void setTile(int[][] tile) {
        this.tile = tile;
    }

    /**
     * Unsafe getter for the tile int matrix
     * 
     * @return The int matrix representing the tile
     */
    public int[][] getTile() {
        return tile;
    }

    /**
     * Unsafe getter for the all arrangments of this tile
     * 
     * @return The list containing all possible arrangments of the tile
     */
    public List<int[][]> getArrangements() {
        return arrangements;
    }

    /**
     * Safe getter, since it copies, for the tile but with all its border
     * stripped off
     * 
     * @return An int matrix with size (tile.length-2)*(tile[0].length-2)
     */
    public int[][] stripBorder() {
        int[][] stripped = new int[tile.length-2][tile[0].length-2];
        for (int i = 1; i < tile.length-1; i++) {
            for (int j = 1; j < tile.length-1; j++) {
                stripped[i-1][j-1] = tile[i][j];
            }   
        }
        return stripped;
    }
   
    /**
     * Checks if the bottom row of this tile matches the top row of the
     * specified tile
     * 
     * @param t The specified tile to be checked
     * @return true if top of the specified tile matches the bottom of this
     *         tile, else false
     */
    public boolean matchBottom(Tile t) {
        int[][] tM = t.getTile();
        for (int i = 0; i < tile[0].length; i++) {
            if (tile[tile.length-1][i] != tM[0][i])
                return false;
        }
        return true;
    }

    /**
     * Checks if the top row of this tile matches the bottom row of the
     * specified tile
     * 
     * @param t The specified tile to be checked
     * @return true if bottom of the specified tile matches the top of this
     *         tile, else false
     */
    public boolean matchTop(Tile t) {
        int[][] tM = t.getTile();
        for (int i = 0; i < tile[0].length; i++) {
            if (tile[0][i] != tM[tM.length-1][i])
                return false;
        }
        return true;
    }

    /**
     * Checks if the left column of this tile matches the right column of the
     * specified tile
     * 
     * @param t The specified tile to be checked
     * @return true if right of the specified tile matches the left of this
     *         tile, else false
     */
    public boolean matchLeft(Tile t) {
        int[][] tM = t.getTile();
        for (int i = 0; i < tile.length; i++) {
            if (tile[i][0] != tM[i][tM[0].length-1])
                return false;
        }
        return true;
    }

    /**
     * Checks if the right column of this tile matches the left column of the
     * specified tile
     * 
     * @param t The specified tile to be checked
     * @return true if left of the specified tile matches the right of this
     *         tile, else false
     */
    public boolean matchRight(Tile t) {
        int[][] tM = t.getTile();
        for (int i = 0; i < tile.length; i++) {
            if (tile[i][tile[0].length-1] != tM[i][0])
                return false;
        }
        return true;
    }

    /**
     * Resets the whole tile to the initial state
     */
    public void reset() {
        top = null;
        bottom = null;
        left = null;
        right = null;
        tile = copy(defaultTile);
    }

    /**
     * Checks if the tile is free to arrange, meaning the tile is not connected
     * to any sides yet
     * 
     * @return true if all sides are null, else false
     */
    public boolean isFree() {
        return countNonNullSides() == 0;
    }

    /**
     * Checks if the tile completely done, meaning all 4 sides of the tile is 
     * connected to something
     * 
     * @return true if all sides are non-null, else false
     */
    public boolean isReady() {
        return countNonNullSides() == 4;
    }

    /**
     * Checks if the tile is an edge, meaning only 3 sides of the tile is
     * connected
     * 
     * @return true if only 3 sides are non-null, else false
     */
    public boolean isEdge() {
        return countNonNullSides() == 3;
    }

    /**
     * Checks if the tile is a corner, meaning only 2 sides of the tile is
     * connected, it will give false positives as it will return true when
     * for instance, only the top and bottom are connected
     * 
     * @return true if only 2 sides are non-null, else false
     */
    public boolean isCorner() {
        return countNonNullSides() == 2;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Tile))
            return false;
            
        return this.getID() == ((Tile)obj).getID();
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for (int i = 0; i < tile.length; i++) {
            for (int j = 0; j < tile[0].length; j++) {
                hash += defaultTile[i][j];
            }
        }
        return hash*37;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Tile ID: ").append(id).append("\n");
        for (int[] row : tile) {
            for (int v : row) {
                if (v <= 0)
                    sb.append('.');
                else
                    sb.append('#');
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private int countNonNullSides() {
        int count = 0;
        if (top != null)
            count++;
        if (bottom != null)
            count++;
        if (left != null)
            count++;
        if (right != null)
            count++;

        return count;
    }

    private List<int[][]> initArrangements(int[][] tile) {
        List<int[][]> arr = new ArrayList<>();

        int[][] b1 = copy(tile);
        // add the 4 rotations of the default matrix
        arr.add(b1);
        for (int i = 0; i < 3; i++) {
            b1 = rotateL(b1);
            arr.add(b1);
        }

        int[][] b2 = copy(tile);
        flipH(b2);
        // add the 4 rotations of the horizontally flipped matrix
        arr.add(b2);
        for (int i = 0; i < 3; i++) {
            b2 = rotateL(b2);
            arr.add(b2);
        }

        // the rest of the arrangements are all duplicates of each other so
        // they are dismissed
        return arr;
    }

    //----------------------------Auxiliary methods-----------------------------
    // the methods below have nothing to do with the Tile class, they are here
    // because i need them, I'm sorry Robert-sama for violating the principles
    private int[][] copy(int[][] m) {
        int[][] copy = new int[m.length][m[0].length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                copy[i][j] = m[i][j];
            }
        }
        return copy;
    }

    private void flipH(int[][] m) {
        for (int i = 0; i < m.length; i++) {
            reverse(m[i]);
        }
    }

    public void flipV(int[][] m) {
        int cLength = m[0].length;
        for (int i = 0; i < cLength; i++) {
            for (int o = 0; o < m.length / 2; o++) {
                int tmp = m[o][i];
                m[o][i] = m[cLength-(o+1)][i];
                m[cLength-(o+1)][i] = tmp;
            }
        }
    }

    private void reverse(int[] arr) {
        for (int i = 0; i < arr.length / 2; i++) {
            int tmp = arr[i];
            arr[i] = arr[arr.length-(i+1)];
            arr[arr.length-(i+1)] = tmp;
        }
    }

    private int[][] rotateL(int[][] m) {
        int[][] t = transpose(m);
        flipH(t);
        return t;
    }

    private int[][] transpose(int[][] m) {
        int[][] tM = new int[m[0].length][m.length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                tM[j][i] = m[i][j];
            }
        }
        return tM;
    }
}
