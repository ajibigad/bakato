package com.ajibigad.bakingapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ajibigad.bakingapp.R;
import com.ajibigad.bakingapp.data.Recipe;
import com.ajibigad.bakingapp.utils.RecipeImageHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Recipe} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class RecipeRecyclerViewAdapter extends RecyclerView.Adapter<RecipeRecyclerViewAdapter.ViewHolder> {

    private List<Recipe> recipes = new ArrayList<>();
    private final OnListFragmentInteractionListener mListener;
    Context context;

    public RecipeRecyclerViewAdapter(Context context, OnListFragmentInteractionListener listener) {
        mListener = listener;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_recipes, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Recipe selectedRecipe = recipes.get(position);
        holder.recipeImage.setImageResource(RecipeImageHelper.getRecipeImage(selectedRecipe.getName()));
        Picasso.with(context)
                .load(RecipeImageHelper.getRecipeImage(selectedRecipe.getName()))
                .fit()
                .centerCrop()
                .into(holder.recipeImage);
        holder.tvRecipeName.setText(selectedRecipe.getName());
        holder.tvRecipeServings.setText(context.getString(R.string.servings, selectedRecipe.getServings()));

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(selectedRecipe);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public View view;

        @BindView(R.id.iv_recipe_image)
        public ImageView recipeImage;

        @BindView(R.id.tv_recipe_name)
        public TextView tvRecipeName;

        @BindView(R.id.tv_recipe_servings)
        TextView tvRecipeServings;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }
}
