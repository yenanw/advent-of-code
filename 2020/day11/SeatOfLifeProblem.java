import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

class SeatOfLifeProblem {

    final static private char empty = 'L';
    final static private char taken = '#';
    final static private char floor = '.';

    static private char[][] seats;
    static private int maxRow;
    static private int maxCol;

    static private void simulate() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            char[][] cSeats = getCopy();

            //printSeat();
            //scanner.nextLine();

            for (int r = 0; r < seats.length; r++) {
                for (int c = 0; c < seats[0].length; c++) {
                    // adjacentTaken(r, c) for part 1
                    int adjacent = takenSeatsInView(r, c);

                    if (seats[r][c] == empty && adjacent == 0) {
                        cSeats[r][c] = taken;
                        // adjacent >= 4 for part 1
                        // adjacent >= 5 for part 2
                        // will generify it later, maybe
                    } else if (seats[r][c] == taken && adjacent >= 5) { 
                        cSeats[r][c] = empty;
                    }
                }
            }

            if (isSame(cSeats)) {
                scanner.close();
                break;
            }

            seats = cSeats;
        }

    }

    static private char[][] getCopy() {
        char[][] copy = new char[seats.length][seats[0].length];

        for (int r = 0; r < seats.length; r++) {
            for (int c = 0; c < seats[0].length; c++) {
                copy[r][c] = seats[r][c];
            }
        }

        return copy;
    }

    static private boolean isSame(char[][] cSeats) {
        for (int r = 0; r < seats.length; r++) {
            for (int c = 0; c < seats[0].length; c++) {
                if (seats[r][c] != cSeats[r][c]) {
                    return false;
                }
            }
        }

        return true;
    }

    static private int adjacentTaken(int row, int col) {
        int count = 0;

        for (int c = -1; c <= 1; c++) {
            for (int r = -1; r <= 1; r++) {
                if (!(r == 0 && c == 0)) {
                    int nRow = r + row;
                    int nCol = c + col;
                    if (isInbound(nRow, nCol) && seats[nRow][nCol] == '#')
                        count++;
                }
            }
        }

        return count;
    }

    static private int takenSeatsInView(int row, int col) {
        int count = 0;

        for (int c = -1; c <= 1; c++) {
            for (int r = -1; r <= 1; r++) {
                if (!(r == 0 && c == 0)) {
                    if (!emptyInSight(c, r, row, col))
                        count++;
                }
            }
        }

        return count;
    }

    static private boolean emptyInSight(int dx, int dy, int row, int col) {
        int nRow = row + dy;
        int nCol = col + dx;

        while (isInbound(nRow, nCol)) {
            if (seats[nRow][nCol] == taken) {
                return false;
            } else if (seats[nRow][nCol] == empty) {
                return true;
            }

            nRow += dy;
            nCol += dx;
        }

        return true;
    }

    static private boolean isInbound(int r, int c) {
        return r >= 0 && c >= 0 &&
               r < maxRow && c < maxCol;
    }

    static private int countTaken() {
        int count = 0;

        for (int r = 0; r < seats.length; r++) {
            for (int c = 0; c < seats[0].length; c++) {
                if (seats[r][c] == taken) {
                    count++;
                }
            }
        }

        return count;
    }

    static private void printSeat() {
        clearScreen();
        for (int r = 0; r < seats.length; r++) {
            for (int c = 0; c < seats[0].length; c++) {
                System.out.print(seats[r][c]);
            }

            System.out.println();
        }
    }

    static public void clearScreen() {  
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    } 

    static public void main(String[] args) {
        try {
            String inputFile = args[0];

            seats = Files.lines(Paths.get(inputFile))
                    .map(line -> line.toCharArray())
                    .toArray(char[][]::new);
            maxRow = seats.length;
            maxCol = seats[0].length;

            simulate();

            System.out.println("There are a total of " + countTaken() + " seats taken");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}