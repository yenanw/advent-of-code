import java.util.ArrayList;
import java.util.List;

class NavReader2 {


    int NS = 1; // waypoint north/south, or y-axis
    int WE = 10; // waypoint west/east, or x-axis

    int rNS = 0;
    int rWE = 0;

    List<NavInstruction> ins = new ArrayList<>();

    public NavReader2(List<NavInstruction> ins) {
        this.ins.addAll(ins);
    }

    public int ManhattanDistance() {
        return Math.abs(rNS) + Math.abs(rWE);
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
            case 'E' -> WE += value;
            case 'W' -> WE -= value;
            case 'S' -> NS -= value;
            case 'N' -> NS += value;
            case 'R' -> rotRight(value);
            case 'L' -> rotLeft(value);
            case 'F' -> forward(value);
        }
    }
    
    public void forward(int value) {
        rNS += NS * value;
        rWE += WE * value;
    }

    // since it rotates only 90/180/270 degrees...
    public void rotRight(int value) {
        int step = value / 90;

        for (int i = 0; i < step; i++) {
            int tmpWE = this.WE;
            this.WE = NS;
            this.NS = -tmpWE;
        }
    }

    public void rotLeft(int value) {
        int step = value / 90;

        for (int i = 0; i < step; i++) {
            int tmpWE = this.WE;
            this.WE = -NS;
            this.NS = tmpWE;
        }
    }

}
