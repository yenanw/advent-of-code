
class Instruction {

    private String operation;
    private int argument;
    private boolean isRead;

    public Instruction(String operation, int argument) {
        this.operation = operation;
        this.argument = argument;
        isRead = false;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String ope) {
        operation = ope;
    }
    
    public int getArgument() {
        return argument;
    }

    public void setArgument(int arg) {
        argument = arg;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead() {
        isRead = true;
    }

    public void unsetRead() {
        isRead = false;
    }

    @Override
    public String toString() {
        return operation + " " + argument;
    }

}
