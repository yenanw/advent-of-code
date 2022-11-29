import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

class LuggageProblem {


    static int containableBagCount(Set<Luggage> luggages, String lugName) {
        Luggage lug = getLuggage(lugName, luggages);

        int sum = 0;
        for (Luggage l : luggages) {
            if (l.canContain(lug))
                sum++;
        }
        
        return sum;
    }

    static Set<Luggage> convertToLuggage(Map<String, Map<String, Integer>> parsedRules) {
        Map<String, Luggage> allLugages = new TreeMap<>();
        for (String name : parsedRules.keySet()) {
            allLugages.put(name, new Luggage(name));
        }

        for (String name : parsedRules.keySet()) {
            Luggage lug = allLugages.get(name);
            Map<String, Integer> contains = parsedRules.get(name);
            for (String conLug : contains.keySet()) {
                Luggage contained = allLugages.get(conLug);
                lug.contain(contained, contains.get(conLug));
            }
        }

        Set<Luggage> luggages = new TreeSet<>();
        for (String name : allLugages.keySet()) {
            luggages.add(allLugages.get(name));
        }

        return luggages;
    }

    static Map<String, Map<String, Integer>> parseRules(List<String> rules) {
        Map<String, Map<String, Integer>> luggageMap = new TreeMap<>();

        for (String rule : rules) {
            // this should always break up to 2 strings
            String[] lugStr = rule.split("bags contain");
            String name = lugStr[0].trim();

            String[] containedLug = lugStr[1].split(",");
            Map<String, Integer> filteredContain = new TreeMap<>();

            for (String lug : containedLug) {
                // string ex: 1 something something bag/s
                // the "bag/s" part is ignored
                String[] arr = lug.trim().split(" ");

                if (!arr[0].equals("no")) {
                    int num = Integer.parseInt(arr[0]);
                    String luggage = arr[1].concat(" ").concat(arr[2]);
                    filteredContain.put(luggage, num);
                }
            }
            
            luggageMap.put(name, filteredContain);
        }

        return luggageMap;
    }

    static Luggage getLuggage(String name, Set<Luggage> allLuggages) {
        for (Luggage lug : allLuggages) {
            if (lug.equals(name))
                return lug;
        }
        return null;
    }

    static public void main(String[] args) {
        try {
            String inputFile = args[0];
            List<String> luggageRules = Files.lines(Paths.get(inputFile))
                                        .collect(Collectors.toList());
            
            Set<Luggage> allLuggages = convertToLuggage(parseRules(luggageRules));

            Luggage myShinyGold = getLuggage("shiny gold", allLuggages);
            int count = containableBagCount(allLuggages, "shiny gold");

            System.out.println("There are " + count + " bag colors that can hold shiny gold");
            System.out.println("Shiny gold bag needs to contain " + myShinyGold.maxBagsContained()
                                + " other bags");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}