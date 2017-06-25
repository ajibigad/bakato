package com.ajibigad.bakingapp.network;

import com.ajibigad.bakingapp.data.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by ajibigad on 17/06/2017.
 */

public interface RecipeClient {

    @GET("baking.json")
    Call<List<Recipe>> getAllRecipe();
}
