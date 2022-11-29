import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class created to solve the day21 challenge, it simply uses the Recipe class
 * and tries to compute various stuff
 *
 * @author       yenanw
 * @version      1.0
 * @see also     Day21, Recipe
 */
public class FoodHandler {
    private Set<Recipe> recipes;

    /**
     * Constructs the class with a set of recipes
     * 
     * @param recipes The set of recipes that is parsed from the input
     */
    public FoodHandler(Set<Recipe> recipes) {
        this.recipes = recipes;
    }

    /**
     * @return The amount of times the safe ingredients appears in the recipes
     */
    public int countSafeIngredients() {
        Set<String> safeIngrs = getAllIngredients();
        safeIngrs.removeAll(getAllergicIngredients().keySet());

        int count = 0;
        for (Recipe recipe : recipes) {
            Set<String> rIngrs = recipe.getIngredients();
            for (String ingr : safeIngrs) {
                if (rIngrs.contains(ingr))
                    count++;
            }
        }
        return count;
    }

    /**
     * Simply converts the keySet from getAllergicIngredients() to a sorted
     * strign array
     * 
     * @return The array containing all allergic ingredients sorted after
     *         the natural/alphabetical order
     */
    public String[] getSortedAllergicIngredients() {
        Map<String,String> allergicIngrs = getAllergicIngredients();
        List<String> sorted = new ArrayList<>();
        allergicIngrs.entrySet()
                     .stream()
                     .sorted(Map.Entry.comparingByValue())
                     .forEachOrdered(x -> sorted.add(x.getKey()));
        return sorted.toArray(String[]::new);
    }

    /**
     * Finds all ingredients that contain an allergen
     * 
     * @return The map with ingredients as key and their corresponding allergen
     *         as value
     */
    public Map<String,String> getAllergicIngredients() {
        Map<String,List<Recipe>> allergenMap = mapAllergens();
        // the identified ingredients that contain an allergen
        Map<String,String> ingrAllergy = new HashMap<>();
        // the unidentified ingredients that might contain an allergen
        Map<Set<String>,String> unidentified = new HashMap<>();
        
        // first step:
        //      for each allergen and the recipes that contain the allergen,
        //      remove all unique ingredients from the list because they
        //      obviously don't contain the specified allergen,
        //
        //      once this process is finished, what's left are an unfinished
        //      lookup table for each ingredient and their allergens, and a map
        //      containing unidentified ingredients
        //
        //      if this doesn't identify at least one ingredient then we fucked
        for (String allergen : allergenMap.keySet()) {
            List<Recipe> recipeList = allergenMap.get(allergen);
            Set<String> ingrs1 = new HashSet<>(recipeList.get(0)
                                               .getIngredients());
            for (int i = 1; i < recipeList.size(); i++) {
                Set<String> ingrs2 = recipeList.get(i).getIngredients();
                ingrs1.retainAll(ingrs2);
            }

            if (ingrs1.size() == 1) {
                String ingr = "";
                for (String str : ingrs1)
                    ingr = str;

                ingrAllergy.put(ingr, allergen);
            } else {
                unidentified.put(ingrs1, allergen);
            }
        }

        // Then just keep referring to the lookup table and remove any
        // ingredient if they are already in the table, and remove the entry
        // from the unindentified map if they contain only one ingredient,
        // until the unidentified map becomes empty
        while (!unidentified.isEmpty()) {
            // ugly workaround to prevent concurrent modification
            Set<Set<String>> toRemove = new HashSet<>();
            Set<Map<Set<String>,String>> toAdd = new HashSet<>();

            for (Set<String> ingrs : unidentified.keySet()) {
                if (ingrs.size() == 1) {
                    String ingr = "";
                    for (String str : ingrs)
                        ingr = str;

                    ingrAllergy.put(ingr, unidentified.get(ingrs));
                    toRemove.add(ingrs);
                } else {
                    // ugly workaround to prevent modification to the key so
                    // that the map no longer recognizes it
                    Set<String> ingrsCopy = new HashSet<>(ingrs);
                    if (ingrsCopy.removeAll(ingrAllergy.keySet())) {
                        toRemove.add(ingrs);
                        Map<Set<String>,String> hm = new HashMap<>();
                        hm.put(ingrsCopy, unidentified.get(ingrs));
                        toAdd.add(hm);
                    }
                }
            }

            for (Set<String> ingrs : toRemove)
                unidentified.remove(ingrs);
            for (Map<Set<String>,String> ingrMap : toAdd)
                unidentified.putAll(ingrMap);
        }
        return ingrAllergy;
    }

    /**
     * Retrieves all ingrediens from all recipes, excluding the duplicates
     * 
     * @return A set of ingredients
     */
    public Set<String> getAllIngredients() {
        Set<String> ingredients = new HashSet<>();
        for (Recipe recipe : recipes) {
            ingredients.addAll(recipe.getIngredients());
        }
        return ingredients;
    }

    private Map<String,List<Recipe>> mapAllergens() {
        // maps the allergens to all recipes that contain the specific allergen
        Map<String,List<Recipe>> m = new HashMap<>();
        for (Recipe recipe : recipes) {
            for (String allergen : recipe.getAllergens()) {
                if (!m.containsKey(allergen)) {
                    List<Recipe> recipeList = new ArrayList<>();
                    m.put(allergen, recipeList);
                }
                m.get(allergen).add(recipe);
            }
        }
        return m;
    }
}
