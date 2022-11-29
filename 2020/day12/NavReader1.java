import java.util.ArrayList;
import java.util.List;


// solver for the part 1, can't easily modify it for part 2
class NavReader1 {
    
    int north = 0;
    int south = 0;
    int east = 0;
    int west = 0;
    char direction = 'E';
    
    List<NavInstruction> ins = new ArrayList<>();

    public NavReader1(List<NavInstruction> ins) {
        this.ins.addAll(ins);
    }

    public int ManhattanDistance() {
        return Math.abs(north-south) + Math.abs(east-west);
    }

    public void readAll() {
        for (int i = 0; i < ins.size(); i++) {
            execute(ins.get(i));
        }
    }

    public void execute(NavInstruction ins) {
        char action = ins.getAction();
        int value = ins.getValue();

        switch (action) {
            case 'E' -> east += value;
            case 'W' -> west += value;
            case 'S' -> south += value;
            case 'N' -> north += value;
            case 'R' -> rotRight(value / 90);
            case 'L' -> rotLeft(value / 90);
            case 'F' -> forward(value);
        }
    }

    public void forward(int value) {
        switch (direction) {
            case 'E' -> east += value;
            case 'W' -> west += value;
            case 'S' -> south += value;
            case 'N' -> north += value;
        }
    }

    public void rotLeft(int step) {
        for (int i = 0; i < step; i++) {
            rotLeftOnce();
        }
    }

    public void rotRight(int step) {
        for (int i = 0; i < step; i++) {
            rotRightOnce();
        }
    }

    // hard-coded rotation since the degrees can only be 90, 180 or 270
    // sooooo...
    public void rotLeftOnce() {
        direction = switch (direction) {
                        case 'E' -> 'N';
                        case 'W' -> 'S';
                        case 'S' -> 'E';
                        case 'N' -> 'W';
                        default -> ' ';
                    };

        if (direction == ' ') {
            System.out.println("error rotating left");
        }
    }

    public void rotRightOnce() {
        direction = switch (direction) {
                        case 'E' -> 'S';
                        case 'W' -> 'N';
                        case 'S' -> 'W';
                        case 'N' -> 'E';
                        default -> ' ';
                    };

        if (direction == ' ') {
            System.out.println("error rotating left");
        }
    }

}
