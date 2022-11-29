/**
 * Class representing the encryption system in the day25 challenge, propably
 *
 * @author       yenanw
 * @version      1.0
 * @see also     Day25
 */
public class Cryption {
    public static final int MODULO_NUM = 20201227;

    /**
     * Tries to establish a handshake given two public keys
     * 
     * @param pubKey1 The first public key
     * @param pubKey2 The second public key
     * @return The encryption key
     */
    public static long encryptionKey(long pubKey1, long pubKey2) {
        int loopSize1 = loopSize(pubKey1);
        int loopSize2 = loopSize(pubKey2);

        long eKey1 = transform(pubKey1, loopSize2);
        long eKey2 = transform(pubKey2, loopSize1);

        if (eKey1 == eKey2)
            return eKey1;

        // the handshake cannot be established
        return -1;
    }

    /**
     * Using the algorithm described in the day25 challenged to transform a
     * number
     * 
     * @param subject  The subject number
     * @param loopSize The loop size/amount of times the process to be repeated
     * @return The transformed number
     */
    public static long transform(long subject, int loopSize) {
        long value = 1;
        for (int i = 0; i < loopSize; i++) {
            value = (value * subject) % MODULO_NUM;
        }
        return value;
    }

    /**
     * Uses trial and error to get the loop size of the specified public key
     * 
     * @param publicKey The public key to calculate loop size from
     * @return The loop size of the specified public key or an endless loop
     */
    public static int loopSize(long publicKey) {
        int loopSize = 0;
        long value = 1;
        while (true) {
            value = (value * 7) % MODULO_NUM;
            loopSize++;

            if (value == publicKey) {
                break;
            }
            // there is possibilty for an infinite loop here, but it works for
            // the input so who cares ¯\_(ツ)_/¯
        }
        return loopSize;
    }

    private Cryption() {
        // don't instantiate me!
    }
}
