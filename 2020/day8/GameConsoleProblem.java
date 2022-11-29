import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class GameConsoleProblem {

    static List<Instruction> parseInstructions(List<String> insString) {
        List<Instruction> insList = new ArrayList<>();

        for (String ins : insString) {
            String[] s = ins.split(" ");
            String op = s[0];
            int arg = Integer.parseInt(s[1]);
            insList.add(new Instruction(op, arg));
        }

        return insList;
    }

    
    static int findNonRepeatingSequence(List<Instruction> allIns) {

        InstructionParser parser = new InstructionParser();
        parser.feedInstruction(allIns);
        for (Instruction ins : allIns) {
            parser.reset();
            String op = ins.getOperation();

            // test by changing jmp -> nop or nop -> jmp
            if (op.equals("jmp")) {
                ins.setOperation("nop");
            } else if (op.equals("nop")) {
                ins.setOperation("jmp");
            } else {
                continue;
            }

            // try to run the instructions with the modified operation
            boolean hasTerminated = !parser.runUntilRepeat();
            // always reset the op to avoid side effects
            ins.setOperation(op);

            if (hasTerminated)
                break;
        }
        
        return parser.getAccumulator();
    }

    static public void main(String[] args) {
        try {
            String inputFile = args[0];

            List<Instruction> instructions = parseInstructions(
                                            Files.lines(Paths.get(inputFile))
                                            .collect(Collectors.toList()));

            InstructionParser parser = new InstructionParser();
            parser.feedInstruction(instructions);
            parser.runUntilRepeat();

            System.out.println("The accumulator is: " + parser.getAccumulator());
            System.out.println("The accumulator after it terminates is: " 
                                + findNonRepeatingSequence(instructions));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}