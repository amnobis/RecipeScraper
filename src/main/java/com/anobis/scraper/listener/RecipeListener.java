package com.anobis.scraper.listener;

import com.anobis.scraper.data.Recipe;

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
