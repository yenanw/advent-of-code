/**
 * A simple helper class for solving the 2-sum/3-sum problem
 *
 * @author       yenanw
 * @version      1.0
 * @see also     Day1
 */
public class NSum {
	/**
	  * A brute-force algorithm for finding the first pair in the array which
	  * sums to a given number
	  *
	  * @param input The int array to search through
	  * @param sum   The number being tested
	  * @return An empty array if no pairs can be found, else an int array that
	  *         consists of the 2 integers that sums to the given numbers
	  */
    public static int[] findPair(int input[], int sum) {
        for (int i = 0; i < input.length; i++) {
            for (int j = i + 1; j < input.length; j++) {
                if (input[i] + input[j] == sum) {
                    return new int[] {
                        input[i], input[j]
                    };
                }
            }
        }
        return new int[0];
    }

    /**
      * A brute-force algorithm for finding the first triple in the array which 
      * sums to a given number
      *
      * @param input The int array to search through
      * @param sum   The number being tested
      * @return An empty array if no triples can be found, else an int array
      *         that consists of the 3 integers that sums to the given numbers
      */
    public static int[] findTriple(int input[], int sum) {
        for (int i = 0; i < input.length; i++) {
            for (int j = i + 1; j < input.length; j++) {
                for (int o = j + 1; o < input.length; o++) {
                    if (input[i] + input[j] + input[o] == sum) {
                        return new int[] {
                            input[i], input[j], input[o]
                        };
                    }
                }
            }
        }
        return new int[0];
    }

    private NSum() {
        // don't instantiate me!
    }
}
