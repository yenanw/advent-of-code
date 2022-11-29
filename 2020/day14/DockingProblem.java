import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

class DockingProblem {

    // solves part 1, this is rather problematic to parse before hand
    // so im just doing it while updating the memories
    static long getSumOfMemory1(List<String> input) {
        BitmaskParser1 parser = new BitmaskParser1();

        for (String line : input) {
            String[] tokens = line.split(" = ");
            if (tokens[0].equals("mask")) {
                parser.updateMask(tokens[1]);
            } else {
                int index = Integer.parseInt(tokens[0].split("]")[0].substring(4));
                long value = Long.parseLong(tokens[1]);
                parser.updateMemory(index, value);
            }
        }

        return parser.getSum();
    }

    // solves part 2, the only difference to the other method is this uses
    // BitmaskParser2 instead of 1, copy-paste programming lets go
    static long getSumOfMemory2(List<String> input) {
        BitmaskParser2 parser = new BitmaskParser2();

        for (String line : input) {
            String[] tokens = line.split(" = ");
            if (tokens[0].equals("mask")) {
                parser.updateMask(tokens[1]);
            } else {
                int index = Integer.parseInt(tokens[0].split("]")[0].substring(4));
                long value = Long.parseLong(tokens[1]);
                parser.updateMemory(index, value);
            }
        }

        return parser.getSum();
    }

    static public void main(String[] args) {
        try {
            String inputFile = args[0];
            
            List<String> input = Files.lines(Paths.get(inputFile))
                                 .collect(Collectors.toList());

            System.out.println("The sum of decoder ver1 is " + getSumOfMemory1(input));
            System.out.println("The sum of decoder ver2 is " + getSumOfMemory2(input));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}