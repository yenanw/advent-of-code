import java.util.Map;
import java.util.TreeMap;

public class Luggage implements Comparable<Luggage> {
    private String name;
    private Map<Luggage, Integer> contains;
    
    public Luggage(String name) {
        this.name = name;
        this.contains = new TreeMap<>();
    }

    public String getName() {
        return name;
    }

    public void contain(Luggage luggage, int amount) {
        contains.put(luggage, amount);
    }

    public boolean canContain(Luggage lug) {
        if (contains.containsKey(lug))
            return true;

        for (Luggage l : contains.keySet()) {
            if (l.canContain(lug))
                return true;
        }
        return false;
    }

    public int maxBagsContained() {
        int count = 0;
        
        for (Luggage lug : contains.keySet()) {
            int amount = contains.get(lug);
            count += amount + amount * lug.maxBagsContained();
        }
        return count;
    }

    public Map<Luggage, Integer> getContained() {
        return contains;
    }

    public boolean contains(Object obj) {
        if (!(obj instanceof Luggage)) 
            return false;

        return contains.containsKey((Luggage)obj);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof String)
            return getName().equals((String)obj);

        if (obj instanceof Luggage)
            return getName().equals(((Luggage)obj).getName());

        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(name).append("]: ").append("{ ");
        for (Luggage lug : contains.keySet()) {
            sb.append("[").append(contains.get(lug)).append(" ").append(lug.getName()).append("] ");
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public int compareTo(Luggage lug) {
        return getName().compareTo(lug.getName());
    }
}
