import java.util.LinkedList;

/**
 * Class representing a player with a deck of cards
 *
 * @author       yenanw
 * @version      1.0
 * @see also     Day22, SpaceCard
 */
public class Player {
    private String name;
    private LinkedList<Integer> hand;

    /**
     * Contructs the player class with a name and a starting hand
     * 
     * @param name The name of the player
     * @param hand The starting cards of the player
     */
    public Player (String name, LinkedList<Integer> hand) {
        this.name = name;
        this.hand = hand;
    }

    /**
     * @return The player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Retrives the player's hand of cards
     * 
     * @return The copy of the player's hand
     */
    public LinkedList<Integer> getHand() {
        return new LinkedList<>(hand);
    }

    /**
     * Retrieves the player's hand of cards with the specified size
     * 
     * @param size The size of the new hand
     * @return The player's current hand but with a size limit
     */
    public LinkedList<Integer> getHand(int size) {
        LinkedList<Integer> nhand = getHand();
        while (nhand.size() > size) {
            nhand.removeLast();
        }
        return nhand;
    }

    /**
     * Draws a card from the top of the hand
     * 
     * @return The value of the card drawn
     */
    public int draw() {
        return hand.poll();
    }

    /**
     * Places a card at the end of the hand
     * 
     * @param card The card to be put at the end of the hand
     */
    public void addLast(int card) {
        hand.addLast(card);
    }

    /**
     * Places a card at the top of the hand
     * 
     * @param card The card to be put at the top of the hand
     */
    public void addFirst(int card) {
        hand.addFirst(card);
    }

    /**
     * Replaces the old hand of cards with the specified one
     * 
     * @param hand The new hand of cards
     */
    public void newHand(LinkedList<Integer> hand) {
        this.hand = hand;
    }

    /**
     * @return The size of the hand
     */
    public int handSize() {
        return hand.size();
    }

    /**
     * Checks if the the player still has any card can be played
     * 
     * @return true if the hand is not empty, else false
     */
    public boolean hasCardLeft() {
        return !hand.isEmpty();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Player))
            return false;
        // this is a stupid check, two players are the same if they have the
        // same name, but enough for this minigame
        Player p = (Player)obj;
        return getName().equals(p.getName());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("'s deck: ").append(hand.toString());
        return sb.toString();
    }
}
