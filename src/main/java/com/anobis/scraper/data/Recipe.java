package com.anobis.scraper.data;

import com.google.common.base.MoreObjects;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author anobis
 *
 * This class represents a recipe which currently includes ingredients,
 * the nutrition information, an image of the recipe, and the grade for
 * the recipe
 */
public class Recipe {
    private final String name;
    private final List<String> ingredients;
    private final Nutrition nutrition;
    private final ByteBuffer image;
    private final Grade grade;


    public Recipe(String name, Nutrition nutrition, List<String> ingredients, Grade grade, ByteBuffer image) {
        this.name = Objects.requireNonNull(name);
        this.nutrition = Objects.requireNonNull(nutrition);
        this.ingredients = Objects.requireNonNull(ingredients);
        this.grade = Objects.requireNonNull(grade);
        this.image = image;
    }

    public Optional<ByteBuffer> getImage() {
        return Optional.of(image);
    }

    public List<String> getIngredients() {
        return Collections.unmodifiableList(ingredients);
    }

    public Nutrition getNutrition() {
        return nutrition;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("ingredients", ingredients)
                .add("nutrition", nutrition)
                .add("image", image)
                .add("grade", grade)
                .toString();
    }

    /**
     * A grade is composed of the number of reviews for the recipe
     * as well as the rating of the recipe. This is used to create
     * a "grading" system to rank recipes.
     */
    public static class Grade {
        private final double rating;
        private final int numReviews;

        public Grade(double rating, int numReviews) {
            this.rating = Objects.requireNonNull(rating);
            this.numReviews = Objects.requireNonNull(numReviews);
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("rating", rating)
                    .add("numReviews", numReviews)
                    .toString();
        }
    }

    /**
     * Nutrition is the nutrition information of the recipe which includes
     * the calories as well as the caloric breakdown. It also includes
     * the amount of dietary cholesterol and the sodium level.
     *
     * Units are as follows (plans to update double type to unit class)
     * calories    : Calories
     * fat         : grams
     * carbs       : grams
     * protein     : grams
     * cholesterol : milligrams
     * sodium      : milligrams
     *
     */
    public static class Nutrition {
        private final double calories;
        private final double fat;
        private final double carbohydrates;
        private final double protein;
        private final double cholesterol;
        private final double sodium;

        public Nutrition(double calories, double fat, double carbohydrates, double protein, double cholesterol, double sodium) {
            this.calories = Objects.requireNonNull(calories);
            this.fat = Objects.requireNonNull(fat);
            this.carbohydrates = Objects.requireNonNull(carbohydrates);
            this.protein = Objects.requireNonNull(protein);
            this.cholesterol = Objects.requireNonNull(cholesterol);
            this.sodium = Objects.requireNonNull(sodium);
        }

        public double getCalories() {
            return calories;
        }

        public double getFat() {
            return fat;
        }

        public double getCarbohydrates() {
            return carbohydrates;
        }

        public double getProtein() {
            return protein;
        }

        public double getCholesterol() {
            return cholesterol;
        }

        public double getSodium() {
            return sodium;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("calories", calories)
                    .add("fat", fat)
                    .add("carbohydrates", carbohydrates)
                    .add("protein", protein)
                    .add("cholesterol", cholesterol)
                    .add("sodium", sodium)
                    .toString();
        }
    }
}
