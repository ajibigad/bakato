package com.ajibigad.bakingapp.utils;

import com.ajibigad.bakingapp.R;

/**
 * Created by ajibigad on 17/06/2017.
 */

public class RecipeImageHelper {

    public static int getRecipeImage(String recipeName){
        switch (recipeName){
            case "Nutella Pie":
                return R.drawable.nutellapie;
            case "Brownies":
                return R.drawable.brownies;
            case "Yellow Cake":
                return R.drawable.yellowcake;
            case "Cheesecake":
                return R.drawable.cheesecake;
        }
        return R.drawable.questionmark;
    }
}
