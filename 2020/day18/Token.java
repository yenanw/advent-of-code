/**
 * A wrapper class for string, to be honest I don't know why I have this
 *
 * @author       yenanw
 * @version      1.0
 * @see also     Day18, Calculator
 */
public class Token {
    private String token;
    
    /**
     * Constructs the class given a char
     * 
     * @param token The token to be constructed
     */
    public Token(char token) {
        this.token = Character.toString(token);
    }

    /**
     * Constructs the class given a string
     * 
     * @param token The token to be constructed
     */
    public Token(String token) {
        this.token = token;
    }

    /**
     * @return The string representation of the token
     */
    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return getToken();
    }
}
