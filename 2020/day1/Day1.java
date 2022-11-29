import java.nio.file.Files;
import java.nio.file.Paths;

public class Day1 {
    public static void main(String args[]) {
        try {
            String inputFile = args[0];
            int[] nums = Files.lines(Paths.get(inputFile))
                        .mapToInt(i->Integer.parseInt(i))
                        .toArray();

            //----------------PART 1----------------
            int[] pair = NSum.findPair(nums, 2020);
            System.out.println("The two numbers that sum to 2020 is: " +
                                pair[0] + " and " + pair[1]);
            System.out.println("The product is " + pair[0]*pair[1]);

            //----------------PART 2----------------
            int[] triple = NSum.findTriple(nums, 2020);
            System.out.println("The three numbers that sum to 2020 is: " +
                                triple[0] + ", " + triple[1] + " and " + triple[2]);
            System.out.println("The product is " + triple[0]*triple[1]*triple[2]);

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
