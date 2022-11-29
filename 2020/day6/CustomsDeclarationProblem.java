import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map;
import java.util.stream.Collectors;

class CustomsDeclarationProblem {

    static List<Integer> getUniqueAnswerCounts(List<String> answers) {
        List<Integer> answerCounts = new ArrayList<>();
        Set<Character> uniqueAnswer = new TreeSet<>();

        for (String ans : answers) {
            if (ans.isBlank()) {
                answerCounts.add(uniqueAnswer.size());
                uniqueAnswer.clear();
            } else {
                for (char c : ans.toCharArray()) {
                    if (!uniqueAnswer.contains(c)) {
                        uniqueAnswer.add(c);
                    }
                }
            }
        }

        return answerCounts;
    }

    static List<Integer> getCommonAnswerCounts(List<String> answers) {
        List<Integer> answerCounts = new ArrayList<>();
        Map<Character, Integer> ansMap = new TreeMap<>();

        int answerCount = 0;
        for (String ans : answers) {
            if (ans.isBlank()) {
                int count = 0;
                for (char key : ansMap.keySet()) {
                    if (ansMap.get(key) == answerCount) {
                        count++;
                    }
                }

                answerCount = 0;
                answerCounts.add(count);
                ansMap.clear();
            } else {
                answerCount++;
                for (char c : ans.toCharArray()) {
                    int inc = 1;
                    if (ansMap.containsKey(c)) {
                        inc = ansMap.get(c) + 1;
                    } 
                    
                    ansMap.put(c, inc);
                }
            }
        }

        return answerCounts;
    }

    public static void main(String[] args) {
        try {
            String input = args[0];
            List<String> answers = Files.lines(Paths.get(input))
                                    .collect(Collectors.toList());
            // add a blank line at the end for easier coding
            answers.add(" ");

            // ------------------------------ PART 1 ------------------------------
            int sum1 = 0;
            for (int count : getUniqueAnswerCounts(answers)) {
                sum1 += count;
            }

            System.out.println("The unique answer sum is " + sum1);
            // ---------------------------- PART 1 END ----------------------------
            // ------------------------------ PART 2 ------------------------------
            int sum2 = 0;
            for (int count : getCommonAnswerCounts(answers)) {
                sum2 += count;
            }

            System.out.println("The common answer sum is " + sum2);
            // ---------------------------- PART 2 END ----------------------------
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}