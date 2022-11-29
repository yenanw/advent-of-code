import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class XMASProblem {

    static List<Long> sumOfRange(List<Long> codes, long num) {
        for (int i = 0; i < codes.size(); i++) {
            long sum = codes.get(i);
            for (int j = i+1; j < codes.size(); j++) {
                sum += codes.get(j);


                if (sum > num)
                    break;

                if (sum == num)
                    return new ArrayList<>(codes.subList(i, j+1));
            }
        }

        return new ArrayList<>();
    }

    static long invalidSumXmas(List<Long> codes, int cond) {
        List<Long> prevs;

        for (int i = cond; i < codes.size(); i++) {
            prevs = new ArrayList<>(codes.subList(i - cond, i));

            if (!canBeSumOf(prevs, codes.get(i)))
                return codes.get(i);
        }

        return -1;
    }

    static boolean canBeSumOf(List<Long> numbers, Long num) {
        // sort the numbers so that we can skip the current iteration
        // whenever the sum is bigger than num,
        // we can also simple return false if the number in the list
        // is bigger than the num
        Collections.sort(numbers);

        for (int i = 0; i < numbers.size(); i++) {
            for (int j = i+1; j < numbers.size(); j++) {
                long n1 = numbers.get(i);
                long n2 = numbers.get(j);

                if (n1 >= num)
                    return false;

                if (n1 + n2 > num)
                    break;
                
                if (n1 + n2 == num)
                    return true;
            }
        }   
        return false;
    }

    static public void main(String[] args) {
        try {
            String inputFile = args[0];

            List<Long> numbers = Files.lines(Paths.get(inputFile))
                                    .map(Long::parseLong) // damn these numbers are big
                                    .collect(Collectors.toList());

            long invalidSum = invalidSumXmas(numbers, 25);
            List<Long> weaknessList = sumOfRange(numbers, invalidSum);
            Collections.sort(weaknessList);
            long weakness = weaknessList.get(0) + weaknessList.get(weaknessList.size()-1);

            System.out.println("The number that doesn't satisfy the property is: "
                                + invalidSum);
            System.out.println("The list that sums to the weakness has a size of: "
                                + weaknessList.size());                         
            System.out.println("The weakness is: " + weakness);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}