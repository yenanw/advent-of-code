import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

class BitmaskParser2 {

    private String mask = "";
    private Map<Long,Long> mems = new TreeMap<>();

    public void updateMask(String mask) {
        this.mask = mask;
    }

    public void updateMemory(int index, long value) {
        long[] maskedIndices = mask(index);

        for (long i : maskedIndices) {
            mems.put(i, value);
        }
    }

    public long[] mask(long index) {
        if (mask.isBlank())
            return new long[]{index};
        
        StringBuilder sb = new StringBuilder(Long.toBinaryString(index));

        // creates a 36 bit string from the index
        // reverse because well, binary numebrs
        sb.reverse();
        while (sb.length() < 36) {
            sb.append('0');
        }
        sb.reverse();

        // mask the string
        for (int i = 0; i < mask.length(); i++) {
            char bit = mask.charAt(i);
            if (bit == '1' || bit == 'X') {
                sb.setCharAt(i, bit); 
            }
        }

        // generate all possible combinations from the floating bits
        List<String> floatingBits = generateFloatingBits(sb.toString());
        // turn it into an array
        long[] indices = new long[floatingBits.size()];
        for (int i = 0; i < indices.length; i++) {
            indices[i] = Long.parseLong(floatingBits.get(i), 2);
        }

        return indices;
    }

    public long getSum() {
        long sum = 0;
        for (long i : mems.keySet()) {
            sum += mems.get(i);
        }
        return sum;
    }

    // man do i love recursion
    private List<String> generateFloatingBits(String masked) {
        List<String> masks = new ArrayList<>();

        StringBuilder sb = new StringBuilder(masked);
        for (int i = 0; i < masked.length(); i++) {
            if (masked.charAt(i) == 'X') {
                sb.setCharAt(i, '0');
                masks.addAll(generateFloatingBits(sb.toString()));
                sb.setCharAt(i, '1');
                masks.addAll(generateFloatingBits(sb.toString()));
                return masks;
            }

            if (i == masked.length()-1) {
                masks.add(sb.toString());
            }
        }

        return masks;
    }
}
