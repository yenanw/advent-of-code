import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class created to solve the day20 challenge
 *
 * @author       yenanw
 * @version      1.0
 * @see also     Day20, Tile
 */
public class TileManager {
    private Tile[] tiles;
    private int size;

    /**
     * Constructs the class with the specified image tiles
     * 
     * @param tiles All of the tiles from the input, note that in order to
     *              solve the puzzle, the length of the tiles must be a squared
     *              number so that the final image will form a square
     */
    public TileManager(Tile[] tiles) {
        this.tiles = tiles;
        this.size = (int)Math.sqrt(tiles.length);
    }

    /**
     * Rearrange all tiles so that they are all connected
     */
    public void arrange() {
        Tile[] corners = findCorners();
        arrange(corners[0], new HashSet<>());
    }

    /**
     * A scuffed method to find the corners from the given tiles, I don't know
     * why it works, but it does
     * 
     * @return The corners of the tiles given from the start
     */
    public Tile[] findCorners() {
        List<Tile> corners = new ArrayList<>();
        for (int i = 0; i < tiles.length; i++) {
            Tile t1 = tiles[i];
            // for each tile in the array, go through all other tiles and see
            // if any of them matches a side
            for (int j = 0; j < tiles.length; j++) {
                if (i == j)
                    continue;
                Tile t2 = tiles[j];

                for (int[][] t : t2.getArrangements()) {
                    t2.setTile(t);
                    if (matchTiles(t1, t2)) {
                        t2.reset();
                        break;
                    }
                }
                
                if (t1.isReady())
                    break;
            }
            // if only two sides are matched, then this tile must be a corner,
            if (t1.isCorner())
                corners.add(t1);

            // reset after to make sure no weird stuff is happening
            reset();
        }
        return corners.toArray(Tile[]::new);
    }

    /**
     * Starting from the top left corner, recursively go through the right and
     * bottom tiles to add their respective tiles to an int matrix, and
     * contruct the image tile from the matrix
     * 
     * @return A tile of the actual image
     */
    public Tile concatTiles() {
        Tile topLeft = null;
        // finds the top left corner
        for (Tile tile : tiles) {
            if (tile.top == null && tile.left == null && tile.isCorner()) {
                topLeft = tile;
                break;
            }
        }
        // calculates the side lengths, -2 because we need to strip the border
        int row = topLeft.getTile().length - 2;
        int col = topLeft.getTile()[0].length - 2;
        int[][] image = new int[size * row][size * col];

        concatTiles(topLeft, image, 0, 0, new HashSet<>());
        // contructs the image tile, id is kina whatever
        return new Tile(image, -1);
    }

    /**
     * Goes through all possible arrangements of the image tile and tries to
     * count the amount of times the pattern appears in teh tile
     * 
     * @param t       The tile to find the pattern from
     * @param pattern THe int matrix representing the pattern
     * @return The amount of times the pattern appears in the tile
     */
    public int countMatchingPattern(Tile t, int[][] pattern) {
        int count = 0;
        List<int[][]> arrangements = t.getArrangements();

        int p = 0;
        int[][] tile = arrangements.get(p);
        // observe this absolute beauty, surprised it doesn't take years to
        // compute this loop 
        while (count == 0) {
            for (int i = 0; i < tile.length - pattern.length; i++) {
                for (int j = 0; j < tile[0].length - pattern[0].length; j++) {
                    boolean match = true;
                    for (int r = 0; r < pattern.length; r++) {
                        for (int c = 0; c < pattern[0].length; c++) {
                            if (pattern[r][c] > 0 &&
                                pattern[r][c] != tile[i+r][j+c]) {
                                match = false;
                                break;
                            }
                        }   
                        
                        if (!match)
                            break;
                    }
                    
                    if (match)
                        count++;
                }
            }
            tile = arrangements.get(p++);
            if (p >= arrangements.size())
                break;
        }
        return count;
    }

    /**
     * Simply call reset on all tiles to completely reset everything
     */
    public void reset() {
        for (Tile tile : tiles) {
            tile.reset();
        }
    }

    private void concatTiles(Tile current, int[][] imageTile,
                             int row, int col, Set<Tile> visited) {
        // it's possible to run into an already visited tile
        if (visited.contains(current))
            return;

        int[][] tile = current.stripBorder();
        visited.add(current);
        // concatenate the tile at the specified indices
        for (int r = 0; r < tile.length; r++) {
            for (int c = 0; c < tile[0].length; c++) {
                imageTile[row+r][col+c] = tile[r][c];
            }
        }
        
        int rowLength = current.getTile().length - 2;
        int colLength = current.getTile()[0].length - 2;
        // as long as we are not at the right edge, concatenate the right tile
        if (current.right != null) {
            concatTiles(current.right, imageTile, 
                        row, col+colLength, visited);
        }
        // as long as we are not at the bottom edge, concatenate the bottom tile
        if (current.bottom != null) {
            concatTiles(current.bottom, imageTile, 
                        row+rowLength, col, visited);
        }
    }

    private void arrange(Tile t1, Set<Tile> visited) {
        // note that the start tile should be one of corners
        if (visited.contains(t1))
            return;
        visited.add(t1);

        // find all sides of the specified tile
        for (Tile t2 : tiles) {
            if (t1.equals(t2) || t1.isReady())
                continue;
            if (t2.isFree()) {
                for (int[][] tArr : t2.getArrangements()) {
                    t2.setTile(tArr);
                    if (matchTiles(t1, t2))
                        break;
                }
            } else {
                matchTiles(t1, t2);
            }
        }
        // recursively go through all of the tiles' sides to find their
        // matching sides untill we have visited all of the tiles
        if (t1.top != null)
            arrange(t1.top, visited);
        if (t1.bottom != null)
            arrange(t1.bottom, visited);
        if (t1.left != null)
            arrange(t1.left, visited);
        if (t1.right != null)
            arrange(t1.right, visited);
    }

    private boolean matchTiles(Tile t1, Tile t2) {
        boolean isSet = false;
        if (t1.top == null && t1.matchTop(t2)) {
            t1.top = t2;
            t2.bottom = t1;
            isSet = true;
        } else if (t1.bottom == null && t1.matchBottom(t2)) {
            t1.bottom = t2;
            t2.top = t1;
            isSet = true;
        } else if (t1.left == null && t1.matchLeft(t2)) {
            t1.left = t2;
            t2.right = t1;
            isSet = true;
        } else if (t1.right == null && t1.matchRight(t2)) {
            t1.right = t2;
            t2.left = t1;
            isSet = true;
        }
        return isSet;
    }
    
}
