package com.anobis.scraper;

import com.anobis.scraper.listener.IStatusListener;
import com.anobis.scraper.listener.RecipeListener;
import com.anobis.scraper.runner.AllRecipePageReader;
import com.anobis.scraper.runner.PageReader;
import com.anobis.scraper.runner.PageRunner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.Thread.sleep;

/**
 * @author anobis
 */
public class Scrape {
    private static final String MAIN_PAGE = "http://allrecipes.com/recipes/80/main-dish/?page=";
    private static final String RECIPE_PAGE = "http://allrecipes.com/recipe/";


    public static void main(String[] args) {
        List<IStatusListener> listeners = new ArrayList<>();
        LinkedBlockingQueue<PageReader> recipeQueue = new LinkedBlockingQueue<>();
        RecipeListener recipeListener = new RecipeListener(recipeQueue);
        listeners.add(recipeListener);
        PageRunner runner = new PageRunner(recipeQueue);
        runner.start(recipeQueue);

        for (int i = 1; i < 5; ++i) {
            try {
                Document doc = Jsoup.connect(MAIN_PAGE + i).get();
                Elements recipeElems = doc.body().getElementsByClass("favorite");

                for (Element recipeElem : recipeElems) {
                    String id = recipeElem.attr("data-id");
                    AllRecipePageReader reader = new AllRecipePageReader(RECIPE_PAGE + id, listeners);

                    recipeQueue.add(reader);
                }
            } catch(Exception e) {
                System.out.println("Unable to connect to URL " + MAIN_PAGE + i);
            }
        }
        runner.shutdown();
        try {
            sleep(2000);
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
        System.out.println(recipeListener.getRecipes());
    }
}



