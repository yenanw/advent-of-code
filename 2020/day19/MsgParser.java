import java.util.ArrayList;
import java.util.List;

/**
 * A class responsible for parsing the rules described in the day19 challenge,
 * despite being called a parser, all of the "real" logic actually lies in the
 * Rule class
 *
 * @author       yenanw
 * @version      1.0
 * @see also     Day19, Rule
 */
public class MsgParser {
    private Rule[] rules;
    private String[] messages;

    /**
     * Constructs the parser class with a set of rules and a set of messages
     * to be parsed
     * 
     * @param rules
     * @param messages
     */
    public MsgParser(Rule[] rules, String[] messages) {
        this.rules = rules;
        this.messages = messages;
    }

    /**
     * Tests if the specified message can be parsed by the rule specified using
     * its id
     * 
     * @param ruleId The id of the rule to match
     * @param msg    The message string to test if it passes the rule
     * @return true if the message parses successfully, else false
     */
    public boolean matches(int ruleId, String msg) {
        return getRule(ruleId).matches(msg);
    }

    /**
     * Retrieves all messages that can be parsed using the specified rule 
     * 
     * @param ruleId The id of the rule to match
     * @return An array of message that is parsed successfully
     */
    public String[] allMatches(int ruleId) {
        Rule rule = getRule(ruleId);
        List<String> match = new ArrayList<>();

        for (String msg : messages) {
            if (rule.matches(msg))
                match.add(msg);
        }
        return match.toArray(String[]::new);
    }

    /**
     * Finds the rule with an id matching the specified id
     * 
     * @param ruleId The id of a rule
     * @return The rule with an id matching the specified id, else null
     */
    public Rule getRule(int ruleId) {
        for (Rule rule : rules) {
            if (rule.getID() == ruleId)
                return rule;
        }
        return null;
    }
}
