import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The rule datatype describe in the day19 challenge, since I'm too lazy to
 * abstractify it, it will have every parameter specified in the challenge,
 * naturally only some of them will be used by each rule
 *
 * @author       yenanw
 * @version      1.0
 * @see also     Day19, MsgParser
 */
public class Rule {
    private List<Rule> fst = null;
    private List<Rule> snd = null;
    private String match = null;

    private int id;
    private RuleType ruleType;

    /**
     * A simple enum representing the three different cases a rule can be
     */
    private enum RuleType {
        MATCH, CASCADE, CASCADE_OR
    }

    /**
     * Constructs the rules with an id and a string, the string is a pattern
     * which this rule class needs to match
     * 
     * @param id    The id of this rule
     * @param match The pattern to match
     */
    public Rule(int id, String match) {
        this.id = id;
        this.match = match;
        this.ruleType = RuleType.MATCH;
    }

    /**
     * Constructs the rules with an id and a sequence of other rules, in order
     * to pass the parsing, the message must satisfy all of the rules in the
     * list sequentially
     * 
     * @param id       The id of this rule
     * @param fstRules The sequence of rules
     */
    public Rule(int id, List<Rule> fstRules) {
        this.id = id;
        this.fst = fstRules;
        this.ruleType = RuleType.CASCADE;
    }

    /**
     * Constructs the rules with an id and two sequences of otehr rules, in
     * order to pass the parsing, the message must satify at least one of the
     * sequenced rules
     * 
     * @param id       The id of this rule
     * @param fstRules The first sequence of rules 
     * @param sndRules The second sequence of rules
     */
    public Rule(int id, List<Rule> fstRules, List<Rule> sndRules) {
        this(id, fstRules);
        this.snd = sndRules;
        this.ruleType = RuleType.CASCADE_OR;
    }

    /**
     * @return The id of this rule
     */
    public int getID() {
        return id;
    }

    /**
     * Allows change to the first sequence of rules
     * 
     * @return The reference to the first ruleset list
     */
    public List<Rule> getFirstRules() {
        return fst;
    }

    /**
     * Allows change to the second sequence of rules
     * 
     * @return The reference to the first ruleset list
     */
    public List<Rule> getSecondRules() {
        return snd;
    }

    /**
     * Tests if the specified message matches the rule's specifications
     * 
     * @param msg The message to test
     * @return true if this rule can parse the message, else false
     */
    public boolean matches(String msg) {
        for (int last : matches(msg, 0)) {
            String parsed = msg.substring(0, last);
            
            if (msg.equals(parsed))
                return true;
        } 
        return false;
    }

    @Override
    public String toString() {
        // this gives a really nice string representation of the object
        StringBuilder sb = new StringBuilder();
        sb.append(id).append(":");
        switch (ruleType) {
            case MATCH:
                sb.append(" \"").append(match).append("\"");
                break;
            case CASCADE:
                for (Rule r : fst) {
                    sb.append(" ").append(r.getID());
                }
                break;
            case CASCADE_OR:
                for (Rule r : fst) { // duplicate code anyone?
                    sb.append(" ").append(r.getID());
                }
                sb.append(" | ");
                for (Rule r : snd) {
                    sb.append(r.getID()).append(" ");
                }
                break;
        }
        return sb.toString();
    }

    private Set<Integer> matches(String msg, int start) {
        // probably should generified it some more
        return switch (ruleType) {
            case MATCH -> isMatch(msg, start);
            case CASCADE -> passMatchTo(fst, msg, start);
            case CASCADE_OR -> passMatchToBoth(msg, start);
            default -> new HashSet<>();
        };
    }

    private Set<Integer> isMatch(String msg, int start) {
        // the parsing fails if the pointer is out of bound
        if (start >= msg.length())
            return new HashSet<>();
        
        int n = start + match.length();
        Set<Integer> next = new HashSet<>();
        next.add(n);
        // only when there are no characters left in the string will the
        // parsing be a valid one
        String m = msg.substring(start, n);
        if (match.equals(m))
            return next;
        // simply return an empty set if the parsing fails
        return new HashSet<>();
    }

    private Set<Integer> passMatchTo(List<Rule> rules, String msg, int start) {
        Set<Integer> next = new HashSet<>();
        // first case, it's here just to make the loop look cleaner
        next.addAll(rules.get(0).matches(msg, start)); 
        
        for (int i = 1; i < rules.size(); i++) {
            if (next.isEmpty()) {
                // exit condition, we don't want to waste time on a 
                // failed parsing
                return new HashSet<>();
            } else {
                Set<Integer> nNext = new HashSet<>();
                for (int n : next)
                    // explore all possiblities for the next rule
                    nNext.addAll(rules.get(i).matches(msg, n));
                // go to next rule and try all new possibilities again
                next = nNext;
            }
        }
        return next;
    }

    private Set<Integer> passMatchToBoth(String msg, int start) {
        // simply explores all possible outcome from both the first set of
        // rules and the second set of rules
        Set<Integer> r1 = passMatchTo(fst, msg, start);
        Set<Integer> r2 = passMatchTo(snd, msg, start);
        r1.addAll(r2);
        return r1;
    }
}
