package com.anobis.scraper.listener;

import com.anobis.scraper.data.Recipe;
import com.anobis.scraper.runner.PageReader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author anobis <austin.nobis@amd.com>
 */
public class RecipeListener implements IStatusListener {
    private final List<Recipe> recipes = new ArrayList<>();
    private final LinkedBlockingQueue<PageReader> recipeQueue;

    public RecipeListener(LinkedBlockingQueue<PageReader> recipeQueue) {
        this.recipeQueue = recipeQueue;
    }

    @Override
    public void recipeFound(Recipe recipe) {
        recipes.add(recipe);
    }

    @Override
    public void queryFailed(PageReader pageReader) {
        recipeQueue.add(pageReader);
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }
}
