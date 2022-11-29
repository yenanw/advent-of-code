import java.util.Map;
import java.util.TreeMap;

class BitmaskParser1 {

    private String mask = "";
    private Map<Integer,Long> mems = new TreeMap<>();

    public void updateMask(String mask) {
        this.mask = mask;
    }

    public void updateMemory(int index, long value) {
        mems.put(index, mask(value));
    }

    public long mask(long value) {
        if (mask.isBlank())
            return value;

        long r = value;

        for (int i = mask.length()-1; i >= 0; i--) {
            char bit = mask.charAt(i);

            if (bit == '1' || bit == '0') {
                r = apply(bit, mask.length()-1-i, r);
            }
        }
        return r;
    }

    public long getSum() {
        long sum = 0;
        for (int i : mems.keySet()) {
            sum += mems.get(i);
        }
        return sum;
    }

    private long apply(char bit, int index, long value) {
        return switch (bit) {
            case '0' -> value & ~pow(2,index);
            case '1' -> value | pow(2,index);
            default -> value;
        };
    }
    
    private boolean isEven(long n) {
        return (n & 1) == 0;
    }
    
    private long pow(long a, int b) {
        if (b == 0)
            return 1;
        if (b == 1)
            return a;

        if (isEven(b))
            return pow(a*a, b/2);
        else
            return a * pow(a*a, b/2);
    }
}
