import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

class ShipProblem {



    static public void main(String[] args) {
        try {
            String inputFile = args[0];

            List<NavInstruction> ins = Files.lines(Paths.get(inputFile))
                                       .map(NavInstruction::new)
                                       .collect(Collectors.toList());
            NavReader1 reader1 = new NavReader1(ins);
            reader1.readAll();

            NavReader2 reader2 = new NavReader2(ins);
            reader2.readAll();

            System.out.println("The wrong Manhattan distance is " + 
                                reader1.ManhattanDistance());
            System.out.println("The right Manhattan distance is " + 
                                reader2.ManhattanDistance());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
