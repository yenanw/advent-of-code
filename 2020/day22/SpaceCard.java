import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * Class created to solve the day22 challenge, this feels like a highschool
 * assignment not gonna lie, just follow the intruction and it's solved
 *
 * @author       yenanw
 * @version      1.0
 * @see also     Day22, Player
 */
public class SpaceCard {
    private Player player1;
    private Player player2;

    // scuffed way to make the game remember how many subgames there are
    private int game = 1;
    
    /**
     * Constructs the game with two players
     * 
     * @param player1 The first player
     * @param player2 The second player
     */
    public SpaceCard(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    /**
     * Plays the default version of the game, every step is printed out in the
     * console for inspection, solves part 1
     * 
     * @return The winner of the game
     */
    public Player playCombat() {
        int round = 1;
        Player winner = null;
        while (winner == null) {
            System.out.println("-- Round " + round + " --");
            System.out.println(player1);
            System.out.println(player2);

            int card1 = player1.draw();
            int card2 = player2.draw();

            System.out.println(player1.getName() + " plays: " + card1);
            System.out.println(player2.getName() + " plays: " + card2);

            if (card1 > card2) {
                System.out.println(player1.getName() + " wins round " + round + "!");
                player1.addLast(card1);
                player1.addLast(card2);
            } else if (card2 > card1) {
                System.out.println(player2.getName() + " wins round " + round + "!");
                player2.addLast(card2);
                player2.addLast(card1);
            } else {
                // somehow there are no identical cards in the deck, so this
                // actually is never going to happen
                System.out.println("It's a draw!");
            }
            System.out.println();

            if (!player1.hasCardLeft())
                winner = player2;
            else if (!player2.hasCardLeft())
                winner = player1;

            round++;
        }
        System.out.println(winner.getName() + " wins the game!");
        System.out.println();

        return winner;
    }

    /**
     * Plays the recursive version of the game, every step is printed out in
     * the console for inspection, solves part 2
     * 
     * @return The winner of the game
     */
    public Player playRecursiveCombat() {
       return playRecursiveCombat(player1, player2, 0);
    }

    // if this is too slow, comment out all printlns and try again
    private Player playRecursiveCombat(Player p1, Player p2, int prev) {
        // the two sets of previously appeared hands
        Set<LinkedList<Integer>> hands1 = new HashSet<>();
        Set<LinkedList<Integer>> hands2 = new HashSet<>();
        int round = 1;
        // remember which game this iteration is
        int thisGame = game;
        // the winner which is yet to be determined
        Player winner = null;

        System.out.println("=== Game " + thisGame + " ===");
        System.out.println();

        while (winner == null) {
            if (containsHand(hands1, p1.getHand()) ||
                containsHand(hands2, p2.getHand())) {
                // the the current hands of the players have already appeared
                // before then the winner is defaulted to be player 1, isn't
                // this rigged?
                winner = p1;
                break;
            } else {
                // add the new version of hands for inspection later
                hands1.add(p1.getHand());
                hands2.add(p2.getHand());
            }

            System.out.println("-- Round " + round + " (Game "+ thisGame + ") --");
            System.out.println(p1);
            System.out.println(p2);
            
            int card1 = p1.draw();
            int card2 = p2.draw();

            System.out.println(p1.getName() + " plays: " + card1);
            System.out.println(p2.getName() + " plays: " + card2);

            Player roundWinner = null;

            if (card1 <= p1.handSize() && card2 <= p2.handSize()) {
                Player newP1 = new Player(p1.getName(), p1.getHand(card1));
                Player newP2 = new Player(p2.getName(), p2.getHand(card2));

                System.out.println("Playing a sub-game to determine the winner...");
                System.out.println();
                game++;
                Player ngWinner = playRecursiveCombat(newP1, newP2, thisGame);

                if (ngWinner.equals(p1))
                    roundWinner = p1;
                else
                    roundWinner = p2;
            } else {
                if (card1 > card2)
                    roundWinner = p1;
                else if (card2 > card1)
                    roundWinner = p2;
            }

            if (roundWinner == null) {
                // this hopefully will never happen
                System.out.println("Round winner is null");
            } else if (roundWinner.equals(p1)) {
                p1.addLast(card1);
                p1.addLast(card2);
            } else if (roundWinner.equals(p2)) {
                p2.addLast(card2);
                p2.addLast(card1);
            }
            
            System.out.println(roundWinner.getName() + " wins round " + round +
                               " of game " + thisGame + "!");
            System.out.println();

            if (!p1.hasCardLeft())
                winner = p2;
            else if (!p2.hasCardLeft())
                winner = p1;

            round++;
        }
        System.out.println(winner.getName() + " wins game " + thisGame + "!");
        if (prev > 0)
            System.out.println("...anyway, back to game " + prev);

        return winner;
    }

    /**
     * Checks if a hand is contained by a set of hands
     * 
     * @param hands The set of hands
     * @param hand  The hand to be checked
     * @return true if the set of hands contains the specified hand, else false
     */
    public boolean containsHand(Set<LinkedList<Integer>> hands,
                                LinkedList<Integer> hand) {
        for (LinkedList<Integer> h : hands) {
            if (hand.containsAll(h) && h.size() == hand.size())
                return true;
        }
        return false;
    }

    /**
     * Calculates the specified player's score according to the instruction in
     * the day22 challenge
     * 
     * @param player The player to calculate scores from
     * @return The score of the specified player
     */
    public int score(Player player) {
        int score = 0;
        LinkedList<Integer> hand = new LinkedList<>();

        for (int i = player.handSize(); i > 0; i--) {
            int card = player.draw();
            score += i * card;
            hand.addFirst(card);
        }
        // since drawing modifies the hand, we need reset the hand
        player.newHand(hand);
        return score;
    }
    
}
