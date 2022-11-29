import java.nio.file.Files;
import java.nio.file.Paths;

public class Day18 {
    public static void main(String[] args) {
        try {
            String fileName = args[0];
            String[] exprs = Files.lines(Paths.get(fileName))
                             .toArray(String[]::new);

            Calculator calc = new Calculator();

            //----------------PART 1----------------
            calc.changePrecedence("+", 3);
            calc.changePrecedence("*", 3);
            long sum1 = 0;
            for (String expr : exprs) {
                sum1 += calc.eval(expr);
            }
            System.out.println("The sum of all expressions in part 1 is "
                               + sum1);

            //----------------PART 2----------------
            calc = new Calculator();
            calc.changePrecedence("+", 4);
            calc.changePrecedence("*", 3);
            long sum2 = 0;
            for (String expr : exprs) {
                sum2 += calc.eval(expr);
            }
            System.out.println("The sum of all expressions in part 2 is "
                               + sum2);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}