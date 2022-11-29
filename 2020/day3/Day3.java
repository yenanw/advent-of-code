import java.nio.file.Files;
import java.nio.file.Paths;

public class Day3 {
    public static int[] parseLine(String line) {
        int[] row = new int[line.length()];
        for (int i = 0; i < row.length; i++) {
            if (line.charAt(i) == '.')
                row[i] = 0;
            else
                row[i] = 1;
        }
        return row;
    }

    public static void main(String args[]) {
        try {
            String input = args[0];
            int[][] map = Files.lines(Paths.get(input))
                          .map(line->parseLine(line))
                          .toArray(int[][]::new);   

            //----------------PART 1----------------        
            int n1 = new SlopeTraversal(map, 3, 1).traverseToBottom(1);
            System.out.println("For slope right 3, down 1, there are " + n1 +
                               " trees");

            //----------------PART 2----------------
            int n2 = new SlopeTraversal(map, 1, 1).traverseToBottom(1);
            System.out.println("For slope right 1, down 1, there are " + n2 +
                               " trees");
            int n3 = new SlopeTraversal(map, 5, 1).traverseToBottom(1);
            System.out.println("For slope right 5, down 1, there are " + n3 +
                                " trees");
            int n4 = new SlopeTraversal(map, 7, 1).traverseToBottom(1);
            System.out.println("For slope right 7, down 1, there are " + n4 +
                                " trees");
            int n5 = new SlopeTraversal(map, 1, 2).traverseToBottom(1);
            System.out.println("For slope right 1, down 2, there are " + n5 +
                               " trees");
            System.out.println("The product of all trees on these slopes is " +
                                n1 * n2 * n3 * n4 * n5);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
