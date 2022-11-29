import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Day17 {
    public static List<Coord> parseCoords(String[] input, int dimen) {
        List<Coord> activeCoords = new ArrayList<>();
        for (int y = 0; y < input.length; y++) {
            for (int x = 0; x < input[0].length(); x++) {
                if (input[y].charAt(x) == '#') {
                    List<Integer> coords = new ArrayList<>();
                    coords.add(x);
                    coords.add(y);
                    // dynamically increase the dimensions
                    for (int i = 2; i < dimen; i++)
                        coords.add(0);
                        
                    Coord coord = new Coord(coords.stream()
                                            .mapToInt(i->i)
                                            .toArray());
                    activeCoords.add(coord);
                }
            }
        }
        return activeCoords;
    }

    public static void main(String[] args) {
        try {
            String fileName = args[0];
            String[] lines = Files.lines(Paths.get(fileName))
                             .toArray(String[]::new);

            //----------------PART 1----------------                 
            InfiniteCube ic = new InfiniteCube(parseCoords(lines, 3), 3);
            ic.cycle(6);
            System.out.println("There are " + ic.countActiveCubes() +
                               " active cubes after the sixth cycle for 3D cubes");

             //----------------PART 2---------------- 
            ic = new InfiniteCube(parseCoords(lines, 4), 4);
            ic.cycle(6);
            System.out.println("There are " + ic.countActiveCubes() +
                               " active cubes after the sixth cycle for 4D cubes");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
