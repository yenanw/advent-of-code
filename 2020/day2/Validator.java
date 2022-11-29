/**
 * Helper class for solving the day2 challenge
 *
 * @author       yenanw
 * @version      1.0
 * @see also     Day2
 */
public class Validator {
    private int fst;
    private int snd;
    private char target;
    private String password;

    /**
     * Constructs a password validator by passing in the required conditions,
     * the target character and the password
     * 
     * @param fst      The first number of the condition
     * @param snd      The second number of the condition
     * @param target   The character which the condition applies to
     * @param password The password to be validated
     */
    public Validator(int fst, int snd, char target, String password) {
        this.fst = fst;
        this.snd = snd;
        this.target = target;
        this.password = password;
    }

    /**
     * Validate the password by using the old password policy, in other words
     * the target character must appear fst to snd times in the password,
     * use this to solve part 1
     * 
     * @return true if the password is valid, else false
     */
    public boolean validateOld() {
        int count = 0;
        for (char c : password.toCharArray()) {
            if (target == c)
                count++;
        }
        return count >= fst && count <= snd;
    }

    /**
     * Validate the password by using the new password policy, in other words
     * the target character must appear at index (fst-1) xor index (snd-1),
     * if it appears at both indices then the password is still invalid,
     * use this to solve part 2
     * 
     * @return true if the password is valid, else false
     */
    public boolean validateNew() {
        char charAtfst = password.charAt(fst-1);
        char charAtsnd = password.charAt(snd-1);
        return (charAtfst == target) ^ (charAtsnd == target);
    }
}
