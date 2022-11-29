import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class JoltAdapterProblem {

    static long countArrangements(List<Integer> joltz) {
        // a "still naive but most likely better than the other naive algorithm"
        // algorithm which breaks the list into smaller lists and uses the
        // other naive algorithm as a helper method to calculate all arrangements

        // it's an ok algorithm to use if the the input doesn't consist many
        // or even entirely of a long sequence of numbers with a difference of 
        // 1 or 2, because in the worst case, it might never create a sublist
        //
        // but in our case it's totally fine (⌒ω⌒)
        // so i don't even care anymore, if it works, it works

        List<List<Integer>> subLists = new ArrayList<>();
    
        int from = 0; // determines where to break the list

        // loops through the list once and creates a sublist whenever the
        // difference is 3
        for (int i = 0; i < joltz.size()-1; i++) {
            int diff = joltz.get(i+1) - joltz.get(i);
            if (diff == 3) {
                subLists.add(joltz.subList(from, i + 1));
                from = i + 1;

                // this part just makes sure we don't leave anything behind
                // and prevents the sublists to be empty in the worst case
                if (i == joltz.size() - 2) { 
                    subLists.add(joltz.subList(from, joltz.size()));
                }
            }
        }

        long count = 1;

        // here we multiply all possible arrangements of the smaller lists,
        // so the total will be all possible arrangements of the combined list
        for (List<Integer> jolts : subLists) {
            count *= countArrangements(jolts, 0);
        }
        
        return count;
    }

    static long countArrangements(List<Integer> joltz, int start) {
        // the very very fucking naive brute-force algorithm that calculates
        // all arrangements by recursively calling itself, has an exponetial
        // complexity (lol)

        long count = 1; // the first list is also an arrangement

        // if the size is <=2 then it can only have 1 arrangement at most
        if (joltz.size() <= 2) 
            return count;

        int s = start;  // pointer that helps us to determine where we are
                        // in the list

        // first we take the difference of the 1st and 3rd elements in the list
        // and compare it, if it's <=3 then we know for sure we can ditch the
        // 2nd element and create a arrangement from it, and to calcalute the
        // all other possible arrangements derived from the new arrangement
        // we simply just recusively call the same methods and continue the
        // cpu massacre
        //
        // the loop condition might be erroneous for some specific inputs but
        // otherwise it works so ¯\_(ツ)_/¯
        while (s != joltz.size() - 2) {
            int n1 = joltz.get(s);
            int n3 = joltz.get(s+2);
            
            if (n3 - n1 <= 3) {
                // create a new list and calculate its arrangements
                List<Integer> joltAlt = new ArrayList<>(joltz);
                joltAlt.remove(s+1);
                count += countArrangements(joltAlt, s);
            }

            s++;
        }

        return count;
    }

    static int[] joltDiff(List<Integer> jolts) {
        // solves part 1, it simply just loops through the list and calculates
        // the difference between i and i+1

        int diff1 = 0; 
        int diff2 = 0;
        int diff3 = 0;

        for (int i = 0; i < jolts.size()-1; i++) {
            int diff = jolts.get(i+1) - jolts.get(i);
            switch (diff) {
                case 1 -> diff1++;
                case 2 -> diff2++;
                case 3 -> diff3++;
            }
        }

        // joltDiffs[0] = amount of difference by 1
        // joltDiffs[1] = amount of difference by 2
        // joltDiffs[2] = amount of difference by 3
        int[] joltDiffs = {diff1, diff2, diff3};

        return joltDiffs;
    }

    static public void main(String[] args) {
        try {
            String inputFile = args[0];

            List<Integer> jolts = Files.lines(Paths.get(inputFile))
                                  .map(Integer::parseInt)
                                  .collect(Collectors.toList());
            // add a 0 at the beginning
            jolts.add(0);
            Collections.sort(jolts);
            // add a (biggest num) + 3 at the end
            jolts.add(jolts.get(jolts.size()-1)+3);

            System.out.println("The differences are :" + 
                              Arrays.toString(joltDiff(jolts)));
            System.out.println("The are " + countArrangements(jolts) +
                               " possible arrangents");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}