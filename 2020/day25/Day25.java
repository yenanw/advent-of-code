import java.nio.file.Files;
import java.nio.file.Paths;

public class Day25 {
    public static void main(String[] args) {
        try {
            String fileName = args[0];
            String[] lines = Files.lines(Paths.get(fileName))
                                  .toArray(String[]::new);

            int num1 = Integer.parseInt(lines[0]);
            int num2 = Integer.parseInt(lines[1]);
            
            //----------------PART 1----------------
            System.out.println("The encryption key is " +
                                Cryption.encryptionKey(num1, num2));

            //----------------PART 2----------------
            // about that...
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
