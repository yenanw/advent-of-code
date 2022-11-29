import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day16 {
    // im sorry for this monstrosity, but i wont fix it
    public static void parseInput(String[] input,
                                  TicketScanner ts,
                                  Ticket myTicket,
                                  List<Ticket> tickets) {
        int pointer = 0;
        // parse all rules
        do {
            String[] str = input[pointer].split(": ");
            String fieldName = str[0];

            Interval interval = new Interval();
            for (String itvl : str[1].split(" or ")) {
                int[] range = Stream.of(itvl.split("-"))
                              .mapToInt(Integer::parseInt)
                              .toArray();
                interval.addInterval(range);
            }

            ts.putRule(fieldName, interval);
            pointer++;
        } while (!input[pointer].isEmpty());

        // skip the "your ticket:" line
        while (!input[pointer].equals("your ticket:")) {
            pointer++;
        }
        pointer++;

        // parse your ticket
        myTicket.addFields(Stream.of(input[pointer].split(","))
                           .map(Integer::parseInt)
                           .collect(Collectors.toList()));

        // skip the "nearby tickets:" line
        while (!input[pointer].equals("nearby tickets:")) {
            pointer++;
        }
        pointer++;

        // parse nearby tickets
        for (int i = pointer; i < input.length; i++) {
            Ticket t = new Ticket();
            t.addFields(Stream.of(input[i].split(","))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList()));
            tickets.add(t);
        }
    }

    public static void main(String[] args) {
        try {
            String fileName = args[0];
            String[] lines = Files.lines(Paths.get(fileName))
                             .toArray(String[]::new);
            
            TicketScanner ts = new TicketScanner();
            Ticket myTicket = new Ticket();
            List<Ticket> tickets = new ArrayList<>();

            parseInput(lines, ts, myTicket, tickets);

            //----------------PART 1----------------
            int errorRate = 0;
            for (Ticket t : tickets) {
                for (int v : ts.invalidValues(t)) {
                    errorRate += v;
                }
            }
            System.out.println("The ticket scanning error rate is "
                               + errorRate);

            //----------------PART 2----------------
            // remove all invalid tickets
            tickets = tickets.stream()
                      .filter(t->!ts.isInvalid(t)) 
                      .collect(Collectors.toList());

            Map<Integer,String> possibleFields = ts.determineFields(tickets);
            int[] myTicketFields = myTicket.getFields();

            long prod = 1;
            for (int i : possibleFields.keySet()) {
                if (possibleFields.get(i).startsWith("departure"))
                    prod *= myTicketFields[i];
            }
            System.out.println("The product of those fields is " + prod);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
