import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * Class representing the floor describe in the day24 challenge
 *
 * @author       yenanw
 * @version      1.0
 * @see also     Day24
 */
public class TileHandler {
    private Tile center;
    private Set<Tile> blackTiles;
    private Tile[][] floor;
    private String[] instructions;

    /**
     * Contructs this class given a list of instructions and a size of how big
     * the floor is
     * 
     * @param instructions The list of instructions to fix the floor with
     * @param size         The size of the floor
     */
    public TileHandler(String[] instructions, int size) {
        this.blackTiles = new HashSet<>();
        this.instructions = instructions;
        // i'm just gonna say that i have ascended with this
        this.floor = initTiles(size);
    }

    /**
     * Runs through all the instructions to "renovate the floor" according to
     * the day24 challenge
     */
    public void renovate() {
        for (String ins : instructions) {
            parseIns(ins);
        }
    }

    /**
     * Simulates what happens to the tiles after 1 day
     */
    public void updateTiles() {
        Set<Tile> toBeFlipped = new HashSet<>();
        // the tiles that needs to be checked are the black tiles and all
        // neighbors of the black tiles, any other tiles are white and have
        // 0 black neighbor, so they don't need to be considered
        Set<Tile> tilesToCheck = new HashSet<>(blackTiles);
        tilesToCheck.addAll(getNeighborsOfBlackTiles());

        for (Tile tile : tilesToCheck) {
            int blackCount = 0;
            for (Tile neighbor : getNeighbors(tile).values()) {
                if (!neighbor.isWhite)
                    blackCount++;
            }
            // apply the art display rules here
            if (!tile.isWhite) {
                if (blackCount == 0 || blackCount > 2)
                    toBeFlipped.add(tile);
            } else {
                if (blackCount == 2)
                    toBeFlipped.add(tile);
            }
        }
        // flip all tiles that needs to be flipped
        for (Tile tile : toBeFlipped) {
            flipTile(tile);
        }
    }

    /**
     * Parses an instruction to flip a tile
     * 
     * @param ins The instruction the parse
     */
    public void parseIns(String ins) {
        Tile tile = center;
        Queue<Character> chrs = strToQueue(ins);

        while (!chrs.isEmpty()) {
            char ch = chrs.poll();
            String dir =  Character.toString(ch);

            if (ch == 's' || ch == 'n') {
                dir += chrs.poll();
            } 

            tile = moveTile(tile, dir);
        }

        flipTile(tile);
    }

    /**
     * Counts how many black tiles there are
     * 
     * @return The amount of all black tiles
     */
    public int countBlackTiles() {
        return blackTiles.size();
    }

    private Queue<Character> strToQueue(String str) {
        // convert the string to a queue of chars so that it becomes easier to
        // process the directions
        Queue<Character> chrQueue = new LinkedList<>();
        for (char ch : str.toCharArray()) {
            chrQueue.add(ch);
        }
        return chrQueue;
    }
    
    private Tile moveTile(Tile tile, String dir) {
        // returns the tile it should be moving to
        return getNeighbors(tile).get(dir);
    }

    private Set<Tile> getNeighborsOfBlackTiles() {
        Set<Tile> neighbors = new HashSet<>();
        for (Tile black : blackTiles) {
            neighbors.addAll(getNeighbors(black).values());
        }
        return neighbors;
    }

    private Map<String,Tile> getNeighbors(Tile tile) {
        // yeah, i dunno about hexagons, so i just harded-coded the neighbors,
        Map<String,Tile> neighbors = new HashMap<>();
        int r = tile.row, c = tile.col;
        
        neighbors.put("e", floor[r][c+1]);
        neighbors.put("w", floor[r][c-1]);

        // the odd numbered row is shifted 1 step to the right to pretend that
        // it's a hexagon
        if ((r & 1) == 0) {
            neighbors.put("ne", floor[r-1][c]);
            neighbors.put("se", floor[r+1][c]);
            neighbors.put("nw", floor[r-1][c-1]);
            neighbors.put("sw", floor[r+1][c-1]);
        } else {
            neighbors.put("ne", floor[r-1][c+1]);
            neighbors.put("se", floor[r+1][c+1]);
            neighbors.put("nw", floor[r-1][c]);
            neighbors.put("sw", floor[r+1][c]);
        }

        return neighbors;
    }

    private void flipTile(Tile tile) {
        if (!tile.isWhite) {
            blackTiles.remove(tile);
            tile.isWhite = true;
        } else {
            blackTiles.add(tile);
            tile.isWhite = false;
        }
    }

    private Tile[][] initTiles(int size) {
        // initialize the tile matrix given a size, so that the size of the
        // floor becomes fixed, pretty scuffed and inefficient, but it becomes
        // hella easier to code with
        Tile[][] m = new Tile[size][size];
        for (int r = 0; r < m.length; r++) {
            for (int c = 0; c < m[0].length; c++) {
                m[r][c] = new Tile(r, c);
            }
        }
        center = m[size/2][size/2];
        return m;
    }

    // the tile class representing a tile, in reality it's just a fancy point
    // in a matrix
    private class Tile {        
        boolean isWhite = true;
        int row;
        int col;

        public Tile(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Tile))
                return false;

            Tile tile = (Tile)obj;
            return this.row == tile.row && this.col == tile.col;
        }
        
        @Override
        public int hashCode() {
            // scuffed hash function, good enough for this for now
            return row * col + col;
        }
    }
}
