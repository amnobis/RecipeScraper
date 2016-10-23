package com.anobis.scraper.runner;

import com.anobis.scraper.listener.IStatusListener;
import com.anobis.scraper.data.Recipe;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.anobis.scraper.data.Recipe.Nutrition;
import com.anobis.scraper.data.Recipe.Grade;

/**
 * @author anobis
 */
public class AllRecipePageReader implements PageReader {
    private static final String NAME_KEY = "";
    private static final String NUTRIENT_KEY = "nutrientLine__item--amount";
    private static final String INGREDIENT_KEY = "recipe-ingred_txt";
    private static final String RATINGS_KEY = "recipe-summary__stars";
    private static final String IMAGE_KEY = "";

    private final String url;
    private final List<IStatusListener> statusListeners;

    public AllRecipePageReader(final String url,
                               final List<IStatusListener> statusListeners) {
        this.url = url;
        this.statusListeners = statusListeners;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void run() {
        try {
            Document doc = getDocument(url);
            String name = getName(doc, NAME_KEY);
            Nutrition nutrition = getNutrition(doc, NUTRIENT_KEY);
            Grade grade = getGrade(doc, RATINGS_KEY);
            List<String> ingredients = getIngredients(doc, INGREDIENT_KEY);
            ByteBuffer image = getImage(doc, IMAGE_KEY);

            System.out.println("Recipe was found!");

            statusListeners.forEach(listener -> {
                listener.recipeFound(new Recipe(name, nutrition, ingredients, grade, image));
            });
        } catch (RuntimeException e) {
            System.out.println("Retrying recipe at URL: " + url);
            statusListeners.forEach(listener -> {
                listener.queryFailed(this);
            });
        }
    }

    private static String getName(Document doc, String nameKey) {
        return "";
    }

    private static ByteBuffer getImage(Document doc, String imageKey) {
        return ByteBuffer.wrap(new byte[]{0});
    }

    private static List<String> getIngredients(Document doc, String ingredientKey) {
        Elements ingredientsElems = doc.getElementsByClass(ingredientKey);

        List<String> ingredients = ingredientsElems
                .stream()
                .filter(elem -> !elem.attr("data-id").isEmpty())
                .map(Element::text)
                .collect(Collectors.toList());

        return ingredients;
    }

    private static Grade getGrade(Document doc, String ratingsKey) {
        Elements ratingsElems = doc.getElementsByClass("recipe-summary__stars").first().getElementsByTag("meta");

        double rating = 0;
        int numReviews = 0;

        for (Element elem : ratingsElems) {
            switch (elem.attr("itemprop")) {
                case "ratingValue":
                    rating = Double.valueOf(elem.attr("content"));
                    break;
                case "reviewCount":
                    numReviews = Integer.valueOf(elem.attr("content"));
                    break;
            }
        }
        return new Grade(rating, numReviews);
    }

    private static Nutrition getNutrition(Document doc, String nutrientKey) {
        Elements nutrientElems = doc.getElementsByClass(nutrientKey);

        Function<Element, Double> getData = element -> {
            return Double.valueOf(element.text().replaceFirst(element.ownText(), ""));
        };

        double cals = 0;
        double fat = 0;
        double carbs = 0;
        double protein = 0;
        double cholest = 0;
        double sodium = 0;


        for (Element elem : nutrientElems) {
            switch (elem.attr("itemprop")) {
                case "calories":
                    cals = getData.apply(elem);
                    break;
                case "fatContent":
                    fat = getData.apply(elem);
                    break;
                case "carbohydrateContent":
                    carbs = getData.apply(elem);
                    break;
                case "proteinContent":
                    protein = getData.apply(elem);
                    break;
                case "cholesterolContent":
                    cholest = getData.apply(elem);
                    break;
                case "sodiumContent":
                    sodium = getData.apply(elem);
                    break;
            }
        }
        return new Nutrition(cals, fat, carbs, protein, cholest, sodium);
    }

    private static Document getDocument(String url) {
        try {
           return Jsoup.connect(url).get();
        } catch (IOException e) {
           throw new RuntimeException("Unable to retrieve url " + url);
        }
    }
}
