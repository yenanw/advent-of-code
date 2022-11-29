import java.nio.file.Files;
import java.nio.file.Paths;

public class Day2 {
    public static Validator getValidator(String[] toParse) {
        String[] conditions = toParse[0].split("-");

        int fst = Integer.parseInt(conditions[0]);
        int snd = Integer.parseInt(conditions[1]);
        char target = toParse[1].charAt(0);
        String password = toParse[2].trim();

        return new Validator(fst, snd, target, password);
    }

    public static void main(String args[]) {
        try {
            String fileName = args[0];
            String[][] passwords = Files.lines(Paths.get(fileName))
                                   .map(str->str.split(" "))
                                   .toArray(String[][]::new);

            //----------------PART 1----------------
            int validPassPart1 = 0;
            for (String[] toParse : passwords) {
                if (getValidator(toParse).validateOld())
                    validPassPart1++;
            }
            System.out.println("Using the old policy there are " + validPassPart1
                               + " valid passwords");

            //----------------PART 2----------------
            int validPassPart2 = 0;
            for (String[] toParse : passwords) {
                if (getValidator(toParse).validateNew())
                    validPassPart2++;
            }
            System.out.println("Using the new policy there are " + validPassPart2
                               + " valid passwords");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
