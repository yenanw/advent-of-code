import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Day15 {
    public static void main(String[] args) {
        try {
            String inputFile = args[0];
            int[] nums = Arrays.stream(Files.readString(Paths.get(inputFile))
                         .split(","))
                         .mapToInt(Integer::parseInt)
                         .toArray();

            //----------------PART 1----------------
            MemoryGame mg = new MemoryGame(nums);
            int part1 = mg.playUntil(2020);
            System.out.println("The 2020th number spoken is " + part1);

            //----------------PART 2----------------
            mg.restart();
            int part2 = mg.playUntil(30000000);
            System.out.println("The 30000000th number spoken is " + part2);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
