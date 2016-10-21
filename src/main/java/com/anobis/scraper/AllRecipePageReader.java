package com.anobis.scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.anobis.scraper.Recipe.Nutrition;
import com.anobis.scraper.Recipe.Grade;

/**
 * @author anobis <austin.nobis@amd.com>
 */
public class AllRecipePageReader implements PageReader {
    private final String url;
    private final List<IStatusListener> statusListeners;
    private final String nutrientKey;
    private final String ingredientKey;
    private final String ratingsKey;
    private final String imageKey;

    public AllRecipePageReader(final String url,
                               final List<IStatusListener> statusListeners,
                               final String nutrientKey,
                               final String ingredientKey,
                               final String ratingsKey, String imageKey) {
        this.url = url;
        this.statusListeners = statusListeners;
        this.nutrientKey = nutrientKey;
        this.ingredientKey = ingredientKey;
        this.ratingsKey = ratingsKey;
        this.imageKey = imageKey;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void run() {
        try {
            Document doc = getDocument(url);
            Nutrition nutrition = getNutrition(doc, nutrientKey);
            Grade grade = getGrade(doc, ratingsKey);
            List<String> ingredients = getIngredients(doc, ingredientKey);
            ByteBuffer image = getImage(doc, imageKey);

            System.out.println("Recipe was found!");

            statusListeners.forEach(listener -> {
                listener.recipeFound(new Recipe(nutrition, ingredients, grade, image));
            });
        } catch (RuntimeException e) {
            System.out.println("Unable to retrieve recipe at " + url);
        }
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
