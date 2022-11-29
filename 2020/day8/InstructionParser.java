import java.util.ArrayList;
import java.util.List;

class InstructionParser {

    private int acc;
    private int pointer;
    private List<Instruction> allIns;

    public InstructionParser() {
        reset();
    }

    public void feedInstruction(List<Instruction> ins) {
        allIns.clear();
        this.allIns.addAll(ins);
    }

    public void reset() {
        acc = 0;
        pointer = 0;
        if (allIns != null)
            allIns.forEach(Instruction::unsetRead);
        else
            allIns = new ArrayList<>();
    }

    public int getAccumulator() {
        return acc;
    }

    public boolean runUntilRepeat() {
        do {
            Instruction ins = allIns.get(pointer);
            execute(ins);
            ins.setRead();

            if (pointer >= allIns.size())
                // the program successfully runs to the end
                // without encountering infinite loop
                return false;
        } while (!allIns.get(pointer).isRead());
        // the program repeated itself
        return true;
    }

    public void execute(Instruction ins) {
        switch (ins.getOperation()) {
            case "jmp":
                pointer += ins.getArgument();
                break;
            case "acc": // fall through to nop to increment pointer
                acc += ins.getArgument();
            case "nop":
                pointer++;
                break;
            default:
                System.out.println("Unrecognized instruction: [" + ins.getOperation() + "]");
        }
    }
}