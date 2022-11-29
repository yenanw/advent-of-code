import java.util.Set;

/**
 * A data container for the input from the day21 challenge
 *
 * @author       yenanw
 * @version      1.0
 * @see also     Day21, FoodHandler
 */
public class Recipe {
    private Set<String> ingredients;
    private Set<String> allergens;
    private Integer hashCode;

    /**
     * Construcs the recipe class by taking in two sets of data
     * 
     * @param ingredients The ingredients of this recipe
     * @param allergens   The allergens of this recipe
     */
    public Recipe(Set<String> ingredients, Set<String> allergens) {
        this.ingredients = ingredients;
        this.allergens = allergens;
    }

    /**
     * @return The set containing the ingredients
     */
    public Set<String> getIngredients() {
        return ingredients;
    }

    /**
     * @return The set containing the allergens
     */
    public Set<String> getAllergens() {
        return allergens;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Recipe))
            return false;
        
        Recipe recipe = (Recipe)obj;
        Set<String> otherIngrs = recipe.getIngredients();
        Set<String> otherAllergens = recipe.getAllergens();
        // two recipes are the same if their ingredients and allergens are same
        if (ingredients.size() != otherIngrs.size() ||
            allergens.size() != otherAllergens.size())
            return false;

        for (String ingr : ingredients) {
            if (!otherIngrs.contains(ingr))
                return false;
        }
        
        for (String allergen : allergens) {
            if (!otherAllergens.contains(allergen))
                return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        if (hashCode == null) {
            StringBuilder sb = new StringBuilder();
            for (String ingr : ingredients)
                sb.append(ingr);
            for (String allergen : allergens)
                sb.append(allergen);
            hashCode = sb.toString().hashCode();
        }
        return hashCode;
    }
}
