import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class BoardingPassProblem {

    public static void main(String[] args) {
        try {
            String input = args[0];

            BinaryBoardingPass[] boardingPasses = 
                                    Files.lines(Paths.get(input))
                                    .map(code -> new BinaryBoardingPass(code))
                                    .toArray(BinaryBoardingPass[]::new);

            List<Integer> seatIDs = new ArrayList<>();

            for (BinaryBoardingPass pass : boardingPasses) {
                seatIDs.add(pass.getSeatID());
            }

            Collections.sort(seatIDs);
            
            for (int i = 0; i < seatIDs.size() - 1; i++) {
                if (seatIDs.get(i) + 1 != seatIDs.get(i+1)) {
                    System.out.println("Missing id found in between: " + seatIDs.get(i)
                                        + " and " + seatIDs.get(i+1));
                    break;
                }
            }

            System.out.println("The highest seat ID is " + seatIDs.get(seatIDs.size()-1));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}