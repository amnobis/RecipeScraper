package com.anobis.scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.Thread.sleep;

/**
 * @author anobis
 */
public class scrape {
    private static final String MAIN_PAGE = "http://allrecipes.com/recipes/80/main-dish/?page=";
    private static final String RECIPE_PAGE = "http://allrecipes.com/recipe/";
    private static final String NUTRIENT_KEY = "nutrientLine__item--amount";
    private static final String INGREDIENT_KEY = "recipe-ingred_txt";
    private static final String RATINGS_KEY = "recipe-summary__stars";
    private static final String IMAGE_KEY = "";

    public static void main(String[] args) {
        List<IStatusListener> listeners = new ArrayList<>();
        RecipeListener recipeListener = new RecipeListener();
        LinkedBlockingQueue<PageReader> recipeQueue = new LinkedBlockingQueue<>();
        listeners.add(recipeListener);
        PageRunner runner = new PageRunner();
        runner.start(recipeQueue);

        for (int i = 1; i < 100; ++i) {
            try {
                Document doc = Jsoup.connect(MAIN_PAGE + i).get();
                Elements recipeElems = doc.body().getElementsByClass("favorite");

                for (Element recipeElem : recipeElems) {
                    String id = recipeElem.attr("data-id");
                    AllRecipePageReader reader = new AllRecipePageReader(RECIPE_PAGE + id,
                            listeners,
                            NUTRIENT_KEY,
                            INGREDIENT_KEY,
                            RATINGS_KEY,
                            IMAGE_KEY);

                    recipeQueue.add(reader);
                }
            } catch(Exception e) {
                System.out.println("Unable to connect to URL " + MAIN_PAGE + i);
            }
        }
        runner.shutdown(recipeQueue);
        try {
            sleep(2000);
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
        System.out.println(recipeListener.getRecipes());
    }
}



