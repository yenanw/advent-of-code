public class NavInstruction {
    
    private char action;
    private int value;
    
    public NavInstruction(String ins) {
        parseIns(ins);
    }

    public char getAction() {
        return action;
    }

    public void setAction(char action) {
        this.action = action;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void parseIns(String ins) {
        String action = ins.substring(0, 1);
        String value =ins.substring(1);

        this.action = action.charAt(0);
        this.value = Integer.parseInt(value);
    }

    @Override
    public String toString() {
        return Character.toString(action) + ": " + value;
    }
}
