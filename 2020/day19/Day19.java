import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Day19 {
    public static Rule[] parseRules(String[] lines) {
        // welp, i don't know regex so...
        Map<Integer,Rule> rules = new HashMap<>();
        // create an empty body for all rules, since the rule class is
        // defined recursively
        for (String line : lines) {
            if (line.isBlank())
                break;

            String[] strs = line.split(": ");
            int id = Integer.parseInt(strs[0]);
            String ruleStr = strs[1];
            Rule rule;

            if (ruleStr.startsWith("\"")) {
                String[] s = ruleStr.split("\"");
                rule = new Rule(id, s[1]);
            } else if (ruleStr.contains(" | ")) {
                rule = new Rule(id, new ArrayList<>(), new ArrayList<>());
            } else {
                rule = new Rule(id, new ArrayList<>());
            }
            rules.put(id, rule);
        }
        // now that we have the empty bodies we can just fill the insides
        for (String line : lines) {
            if (line.isBlank())
                break;

            String[] strs = line.split(": ");
            int id = Integer.parseInt(strs[0]);
            String ruleStr = strs[1];
            Rule rule = rules.get(id);

            if (ruleStr.startsWith("\"")) {
                // the default case is already done
                continue;
            } else if (ruleStr.contains(" | ")) {
                String[] s = ruleStr.split(" \\| ");
                String[] fst = s[0].split(" ");
                String[] snd = s[1].split(" ");
                int[] fstIds = Stream.of(fst).mapToInt(Integer::parseInt)
                                             .toArray();
                int[] sndIds = Stream.of(snd).mapToInt(Integer::parseInt)
                                            .toArray();
                for (int rid : fstIds)
                    rule.getFirstRules().add(rules.get(rid));
                for (int rid : sndIds)
                    rule.getSecondRules().add(rules.get(rid));
            } else {
                int[] ruleIds = Stream.of(ruleStr.split(" "))
                                            .mapToInt(Integer::parseInt)
                                            .toArray();
                for (int rid : ruleIds)
                    rule.getFirstRules().add(rules.get(rid));
            }
        }
        return rules.entrySet()
                    .stream()
                    .map(x -> x.getValue())
                    .toArray(Rule[]::new);
    }

    public static String[] parseMessages(String[] lines) {
        int p = 0;
        for (int i = 0; i < lines.length; i++) {
            // find the break point where the messages begin
            if (lines[i].isBlank())
                p = i + 1;
        }
        // simply add all messages, line by line to the list
        List<String> msgs = new ArrayList<>();
        for (int i = p; i < lines.length; i++) {
            msgs.add(lines[i].trim());
        }
        return msgs.toArray(String[]::new);
    }

    public static void main(String[] args) {
        try {
            String fileName = args[0];
            String[] lines = Files.lines(Paths.get(fileName))
                                  .toArray(String[]::new);

            MsgParser mp = new MsgParser(parseRules(lines),
                                         parseMessages(lines));
            
            //----------------PART 1----------------
            System.out.println("There are " + mp.allMatches(0).length +
                               " messages that matches rule 0");

            //----------------PART 2----------------
            // just change the input and try again, it will work automatically
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
