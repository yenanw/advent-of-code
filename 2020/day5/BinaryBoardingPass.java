

public class BinaryBoardingPass {

    private int row = -1;
    private int column = -1;
    private int seatId = -1;

    BinaryBoardingPass(String code) {
        parseSeatCode(code);
    }

    int getRow() {
        return row;
    }

    int getColumn() {
        return column;
    }

    int getSeatID() {
        return seatId;
    }

    static boolean isValidSeatCode(String code) {
        if (code.length() != 10)
            return false;

        char[] chars = code.toCharArray();
        
        for (int i = 0; i < 10; i++) {
            char c = chars[i];
            if (i < 7) {
                if (c != 'F' && c != 'B')
                    return false;
            } else {
                if (c != 'L' && c != 'R')
                    return false;
            }
        }

        return true;
    }

    // code is a 10-characters string
    // first 7 characters can only be F or B
    // last 3 characters can only be R or L
    private void parseSeatCode(String code) {
        if (!isValidSeatCode(code))
            return;

        char[] rowCode = code.substring(0, 7).toCharArray();
        char[] colCode = code.substring(7).toCharArray();

        row = binaryDecode(0, 127, rowCode);
        column = binaryDecode(0, 7, colCode);
        seatId = row * 8 + column;
    }

    private int binaryDecode(int lo, int hi, char[] codes) {
        int min = lo;
        int max = hi;
        int value = -1;

        for (char c : codes) {
            if (c == 'F' || c == 'L') {
                max = (min + max) / 2;
                value = max;
            } else if (c == 'B' || c == 'R') {
                min = (min + max) / 2 + 1;
                value = min;
            } else {
                System.out.println("Unknown code: [" + c + "]");
                return -1;
            }
        }

        return value;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Row: ").append(row).append(", Column: ")
            .append(column).append(", Seat ID: ").append(seatId);
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println("FBFBBFFRLR -> " + new BinaryBoardingPass("FBFBBFFRLR"));
        System.out.println("BFFFBBFRRR -> " + new BinaryBoardingPass("BFFFBBFRRR"));
        System.out.println("FFFBBBFRRR -> " + new BinaryBoardingPass("FFFBBBFRRR"));
        System.out.println("BBFFBBFRLL -> " + new BinaryBoardingPass("BBFFBBFRLL"));
    }
}
