import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;

public class Day22 {
    public static Player[] parsePlayers(String[] input) {
        Player[] players = new Player[2];

        String name = "";
        LinkedList<Integer> hand = new LinkedList<>();
        for (String line : input) {
            if (line.startsWith("Player")) {
                name = line.split(":")[0];
            } else if (line.isBlank()) {
                players[0] = new Player(name, hand);
                name = "";
                hand = new LinkedList<>();
            } else {
                hand.add(Integer.parseInt(line));
            }
        }
        players[1] = new Player(name, hand);
        return players;
    }

    public static void main(String[] args) {
        try {
            String fileName = args[0];
            String[] lines = Files.lines(Paths.get(fileName))
                                  .toArray(String[]::new);

            Player[] p1 = parsePlayers(lines);
            SpaceCard sc = new SpaceCard(p1[0], p1[1]);
            //----------------PART 1----------------
            System.out.println("The winner of Combat has a score of " +
                               sc.score(sc.playCombat()) + "\n\n");

            //----------------PART 2----------------
            Player[] p2 = parsePlayers(lines);
            sc = new SpaceCard(p2[0], p2[1]);
            System.out.println("The winner of Recursiuve Combat has a score of "
                               + sc.score(sc.playRecursiveCombat()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
