package com.ajibigad.bakingapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ajibigad.bakingapp.data.Ingredient;
import com.ajibigad.bakingapp.data.Recipe;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ajibigad.bakingapp.utils.AppConstants.SELECTED_RECIPE;


/**
 * A simple {@link Fragment} subclass.
 */
public class IngredientFragment extends Fragment {

    Recipe selectedRecipe;

    @BindView(R.id.tv_ingredients)
    TextView ingredientsTextView;

    public IngredientFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ingredient, container, false);
        ButterKnife.bind(this, view);
        if(this.getArguments().containsKey(SELECTED_RECIPE)){
            selectedRecipe = Parcels.unwrap(this.getArguments().getParcelable(SELECTED_RECIPE));
        }
        ingredientsTextView.setText(formatIngredientsAsBulletPoints(getContext(), selectedRecipe.getIngredients()));
        return view;
    }

    public static String formatIngredientsAsBulletPoints(Context context, List<Ingredient> ingredients){
        String template = context.getString(R.string.ingredient_list_template);
        StringBuilder stringBuilder = new StringBuilder(context.getString(R.string.ingredient_list_header)).append("\n");
        for(Ingredient ingredient : ingredients){
            stringBuilder.append(String.format(template, ingredient.getName(), ingredient.getQuantity(), ingredient.getMeasure()))
            .append("\n");
        }

        Log.i("Formatted ingredients", stringBuilder.toString());

        return stringBuilder.toString();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
