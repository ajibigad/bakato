package com.ajibigad.bakingapp.data;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by ajibigad on 17/06/2017.
 */

@Parcel
public class Recipe {

    private int id;
    private String name;
    private String servings;
    private List<Ingredient> ingredients;
    private List<Step> steps;


    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServings() {
        return servings;
    }

    public void setServings(String servings) {
        this.servings = servings;
    }
}
