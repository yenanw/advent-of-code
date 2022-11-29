import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Day23 {
    public static int[] charToInt(char[] chArr) {
        int[] arr = new int[chArr.length];
        for (int i = 0; i < chArr.length; i++) {
            arr[i] = Character.getNumericValue(chArr[i]);
        }
        return arr;
    }

    public static void main(String[] args){
        try {
            String fileName = args[0];
            int[] labels = charToInt(Files.readString(Paths.get(fileName))
                                          .toCharArray());

            //----------------PART 1----------------
            CupGame cp = new CupGame(labels);
            cp.move(100);
            int indexTo1 = 0;
            String cupsStr = cp.getCups();
            char[] cups = cupsStr.toCharArray();
            for (int i = 0; i < cups.length; i++) {
                if (Character.getNumericValue(cups[i]) == 1) {
                    indexTo1 = i;
                    break;
                }
            }
            cupsStr = cupsStr.substring(indexTo1+1) +
                      cupsStr.substring(0, indexTo1);

            System.out.println("Labels on the cups after 1 becomes "
                               + cupsStr + "\n");

            //----------------PART 2----------------
            cp = new CupGame(labels, 1000000);
            cp.move(10000000);
            String afterOne = cp.nextCups(1, 2);
            System.out.println("The two cups right after cup 1 are "
                               + afterOne);
            long[] lbls = Stream.of(afterOne.split(" "))
                                 .mapToLong(Long::parseLong)
                                 .toArray();
            long product = lbls[0] * lbls[1];
            System.out.println("The product is " + product);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
