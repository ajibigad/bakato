package com.ajibigad.bakingapp.network;

import com.ajibigad.bakingapp.data.Recipe;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ajibigad on 17/06/2017.
 */

public class RecipeService {

    public final static String API_BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";

    private static final String TAG = RecipeService.class.getSimpleName();

    private static RecipeClient recipeClient;

    private RecipeService() {
        setupRetrofit();
    }

    public static RecipeService getInstance(){
        return new RecipeService();
    }

    private static void setupRetrofit() {
        OkHttpClient httpClient = new OkHttpClient.Builder().build();

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(
                                GsonConverterFactory.create()
                        );

        Retrofit retrofit = builder.client(httpClient).build();

        recipeClient = retrofit.create(RecipeClient.class);
    }

    public Call<List<Recipe>> getAllRecipe() {
        return recipeClient.getAllRecipe();
    }
}
