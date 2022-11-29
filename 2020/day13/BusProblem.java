import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class BusProblem {


    //----------------------------------PART 1----------------------------------
    static int matchingBus(int timeStamp, int[] buses) {
        Map<Integer,Integer> idToTime = new HashMap<>();

        for (int id : buses) {
            idToTime.put(id, timeOfArrival(timeStamp, id));
        }

        int minID = buses[0];
        for (int id : idToTime.keySet()) {
            if (idToTime.get(id) < idToTime.get(minID))
                minID = id;
        }

        return minID;
    }

    static int timeOfArrival(int timestamp, int busID) {
        if (timestamp % busID == 0) // divisible, return the timestamp directly
            return timestamp;
        
        return (timestamp / busID) * busID + busID;
    }

    static int[] parseBusSchedule(String busSchedule) {
        String[] schedules = busSchedule.split(",");
        List<Integer> busIDs = new ArrayList<>();

        for (String busID : schedules) {
            if (!busID.equals("x")) {
                busIDs.add(Integer.parseInt(busID));
            }
        }
        
        return busIDs.stream().mapToInt(i->i).toArray();
    }
    //----------------------------------PART 1----------------------------------


    //----------------------------------PART 2----------------------------------

    static Map<Integer,Integer> parseSchedule(String busSchedule) {
        String[] schedules = busSchedule.split(",");
        Map<Integer,Integer> remainders = new HashMap<>();

        for (int i = 0; i < schedules.length; i++) {
            if (!schedules[i].equals("x")) {
                // map the modulos to the remainders¨
                int mod = Integer.parseInt(schedules[i]);
                int r;
                if (i == 0)
                    r = 0;
                else
                    r = mod - i;

                remainders.put(mod,r);
            }
        }

        System.out.println(remainders);
        return remainders;
    }

    // yeah i looked it up...
    static long CRT(Map<Integer,Integer> mods) {
        long N = 1;
        for (int mod : mods.keySet()) {
            // calculate the N which is the product of all modulos
            N *= mod;
        }

        long sumCRT = 0;
        for (int mod : mods.keySet()) {
            int r = mods.get(mod);
            long n = N / mod;
            int x = 1;

            long a = n % mod;
            for (int i = 0; i < mod; i++) {
                long s = a * i - 1;
                if (s % mod == 0) {
                    x = i;
                    break;
                }
            }
            
            sumCRT += r * n * x;
            System.out.println("N="+n+", mod="+mod+", x="+x);
        }
        System.out.println("The sum is " + sumCRT + ", the N is " + N);
        return sumCRT % N;
    }

    //----------------------------------PART 2----------------------------------

    static public void main(String[] args) {
        try {
            String inputFile = args[0];

            List<String> input = Files.lines(Paths.get(inputFile))
                                 .collect(Collectors.toList());

            int timestamp = Integer.parseInt(input.get(0));
            int[] busIDs = parseBusSchedule(input.get(1));

            System.out.println("The bus that comes the earliest is " 
                                + matchingBus(timestamp, busIDs));

            System.out.println("The earliest timestamp is " +
                                CRT(parseSchedule(input.get(1))));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}