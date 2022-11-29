import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Day21 {
    public static FoodHandler parseFood(String[] lines) {
        Set<Recipe> recipes = new HashSet<>();
        for (String line : lines) {
            String[] rgx = line.split(" \\(contains ");
            Set<String> ingredients = new HashSet<>(Arrays.asList(
                                                    rgx[0].split(" ")));
            Set<String> allergens = new HashSet<>(Arrays.asList(
                                                  rgx[1].split("\\)")[0]
                                                  .split(", ")));
            recipes.add(new Recipe(ingredients, allergens));
        }
        return new FoodHandler(recipes);
    }

    public static void main(String[] args) {
        try {
            String fileName = args[0];
            String[] lines = Files.lines(Paths.get(fileName))
                             .toArray(String[]::new);
            
            FoodHandler fh = parseFood(lines);

            //----------------PART 1----------------
            System.out.println("All non-allergic ingredients appeared " +
                               fh.countSafeIngredients() + " times");

            //----------------PART 2----------------
            String ingrs = String.join(",",fh.getSortedAllergicIngredients());
            System.out.println("The Canonical Dangerous Ingredient List:");
            System.out.println(ingrs);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
