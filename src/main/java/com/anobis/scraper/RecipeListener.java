package com.anobis.scraper;

import com.anobis.scraper.IStatusListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anobis <austin.nobis@amd.com>
 */
public class RecipeListener implements IStatusListener {
   private final List<Recipe> recipes = new ArrayList<>();

   @Override
   public void recipeFound(Recipe recipe) {
      recipes.add(recipe);
   }

    public List<Recipe> getRecipes() {
        return recipes;
    }
}
