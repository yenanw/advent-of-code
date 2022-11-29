import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

/**
 * A very limited calculator class created after the day18 challenge, it can
 * only perfom addition and multiplication, but with any precedence
 *
 * @author       yenanw
 * @version      1.0
 * @see also     Day18, Token
 */
public class Calculator {
    private Map<String,Integer> operators = new HashMap<>();

    /**
     * Constructs the class by defaulting to the normal precedence of addition
     * and multiplication
     */
    public Calculator() {
        operators.put("+", 3);
        operators.put("+", 4);
    }

    /**
     * Evaluates an infix expression by using shunting-yard's algorithm, there
     * are however several limitations:
     *  * 1. The operands can only be single digit
     *  * 2. Other than the numbers, there can only be "(", ")" for parenthesis,
     *  *    "+" for addition and "*" for multiplication
     * 
     * @param expr The expression to be evaluated
     * @return
     */
    public long eval(String expr) {
        return eval(toPostfix(tokenize(expr)));
    }

    /**
     * Changes the precedence of existing operators, does nothing if the given
     * operator does not exist
     * 
     * @param operatorToken The token/string representing the operator,
     *                      currently only accepts "+" and "*"
     * @param precedence    Any int bigger than 0
     */
    public void changePrecedence(String operatorToken, int precedence) {
        if (operators.containsKey(operatorToken))
            operators.put(operatorToken, precedence);
    }

    private long eval(Queue<Token> postfix) {
        Stack<Token> evalStack = new Stack<>();

        while (!postfix.isEmpty()) {
            String token = postfix.peek().getToken();
            if (isOperator(token)) {
                String op1 = evalStack.pop().getToken();
                String op2 = evalStack.pop().getToken();
                evalStack.push(calc(op1,op2,token));
                postfix.poll();
            } else {
                evalStack.push(postfix.poll());
            }
        }
        // assuming there never will be any unary operator, then the stack will
        // always have only one element left which is the final answer
        return Long.parseLong(evalStack.pop().getToken());
    }

    private Token[] tokenize(String expr) {
        List<Token> tokens = new ArrayList<>();
        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);
            if (c == '(' || c == ')' || isOperator(Character.toString(c))) {
                tokens.add(new Token(c));
            } else if (Character.isDigit(c)) {
                tokens.add(new Token(c));
            }
        }
        return tokens.stream().toArray(Token[]::new);
    }

    // this is, in other words, the shunting-yard's algorithm,
    // like hell am i gonna come up with my own parsing algorithm
    private Queue<Token> toPostfix(Token[] tokens) {
        Queue<Token> output = new LinkedList<>();
        Stack<Token> operators = new Stack<>();
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i].getToken();
            if (isOperand(token)) {
                output.add(tokens[i]);
            } else if (isOperator(token)) {
                while (true) {
                    if (operators.isEmpty())
                        break;

                    String top = operators.peek().getToken();
                    if ((getPrecedence(top) > getPrecedence(token) ||
                        (getPrecedence(top) == getPrecedence(token) &&
                        getAssoc(token) == Assoc.LEFT))) {
                        output.add(operators.pop());
                    } else {
                        break;
                    }
                }
                operators.push(tokens[i]);
            } else if (token.equals("(")) {
                operators.push(tokens[i]);
            } else if (token.equals(")")) {
                while (true) {
                    if (operators.isEmpty())
                        break;
                    if (operators.peek().getToken().equals("("))
                        break;
                        
                    output.add(operators.pop());
                }
                if (operators.isEmpty())
                    System.out.println("Mismatched parenthesis!");
                operators.pop();
            }
        }

        while (!operators.isEmpty())
            output.add(operators.pop());

        return output;
    }

    private Token calc(String op1, String op2, String operator) {
        long n1 = Long.parseLong(op1);
        long n2 = Long.parseLong(op2);
        switch(operator) {
            case "*":
                return new Token(Long.toString(n1 * n2));
            case "+":
                return new Token(Long.toString(n1 + n2));
            default:
                return null;
        }
    }
    
    private int getPrecedence(String token) {
        if (!operators.containsKey(token))
            return -1;
        
        return operators.get(token);
    }

    private enum Assoc {
        LEFT, RIGHT
    }

    private Assoc getAssoc(String token) {
        return Assoc.LEFT;
    }

    private boolean isOperator(String token) {
        for (String operator : operators.keySet()) {
            if (token.equals(operator))
                return true;
        }
        return false;
    }
    
    private boolean isOperand(String token) {
        try {
            Integer.parseInt(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
