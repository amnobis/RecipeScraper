import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author anobis
 */
public class scrape {
    public static void main(String[] args) {
        try {
            List<Recipe> recipes = new ArrayList<>();
            for (int i = 1; i < 5; ++i) {
                Document doc = Jsoup.connect("http://allrecipes.com/recipes/80/main-dish/?page=" + i).get();
                Elements recipeElems = doc.body().getElementsByClass("favorite");

                for (Element recipeElem : recipeElems) {
                    String id = recipeElem.attr("data-id");

                    System.out.println(id);
                    Document docu = Jsoup.connect("http://allrecipes.com/recipe/" + id).get();
                    Elements nutrientsElems = docu.getElementsByClass("nutrientLine__item--amount");
                    Elements ingredientsElems = docu.getElementsByClass("recipe-ingred_txt");
                    List<String> ingredients = ingredientsElems
                            .stream()
                            .filter(elem -> !elem.attr("data-id").isEmpty())
                            .map(Element::text)
                            .collect(Collectors.toList());

                    double cals = 0;
                    double fat = 0;
                    double carbs = 0;
                    double protein = 0;
                    double cholest = 0;
                    double sodium = 0;

                    Function<Element, Double> getData = element -> {
                        return Double.valueOf(element.text().replaceFirst(element.ownText(), ""));
                    };

                    for (Element elem : nutrientsElems) {
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

                    Recipe recipe = new Recipe(
                        new Recipe.Nutrition(
                            cals,
                            fat,
                            carbs,
                            protein,
                            cholest,
                            sodium),
                        ingredients,
                        new Recipe.Grade(0.0, 0),
                        null);
                    recipes.add(recipe);
                }
            }
            System.out.println(recipes);

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}



