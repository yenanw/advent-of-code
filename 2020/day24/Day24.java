import java.nio.file.Files;
import java.nio.file.Paths;

public class Day24 {
    public static void main(String[] args) {
        try {
            String fileName = args[0];
            String[] lines = Files.lines(Paths.get(fileName))
                                  .toArray(String[]::new);
            // eh, so about this 200 stuff, just choose something appropriate,
            // cannot be bothered to make this dynamic lol
            TileHandler th = new TileHandler(lines, 200);

            //----------------PART 1----------------
            th.renovate();
            System.out.println("There are " + th.countBlackTiles() +
                               " black tiles");

            //----------------PART 2----------------
            for (int i = 0; i < 100; i++) {
                th.updateTiles();
            }
            System.out.println("After 100 days there are " +
                               th.countBlackTiles() + " black tiles");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
