package com.ajibigad.bakingapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ajibigad.bakingapp.R;
import com.ajibigad.bakingapp.adapter.OnListFragmentInteractionListener;
import com.ajibigad.bakingapp.adapter.RecipeRecyclerViewAdapter;
import com.ajibigad.bakingapp.data.Recipe;
import com.ajibigad.bakingapp.network.RecipeService;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class RecipesFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Recipe>>{

    private static final int RECIPES_LOADER = 1111;
    private final String TAG = RecipesFragment.class.getSimpleName();

    private RecipeService recipeService;

    @BindView(R.id.recipe_list)
    RecyclerView recipeRecyclerView;

    RecipeRecyclerViewAdapter recipeRecyclerViewAdapter;

    private OnListFragmentInteractionListener mListener;

    private OnRecipesFetchedListener onRecipesFetchedListener;

    @BindView(R.id.pb_loading_indicator)
    ProgressBar progressBar;
    @BindView(R.id.tv_error_message_display)
    TextView tvErrorMessage;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recipeService = RecipeService.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipes_list, container, false);
        ButterKnife.bind(this, view);

        // Set the layout manager
        recipeRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        recipeRecyclerViewAdapter = new RecipeRecyclerViewAdapter(getContext(), mListener);
        recipeRecyclerView.setAdapter(recipeRecyclerViewAdapter);
        loadRecipes();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
        if(context instanceof OnRecipesFetchedListener){
            onRecipesFetchedListener = (OnRecipesFetchedListener) context;
        } else {
            onRecipesFetchedListener = null;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void showErrorMessage() {
        tvErrorMessage.setVisibility(View.VISIBLE);
        recipeRecyclerView.setVisibility(View.INVISIBLE);
    }

    private void showRecipesView() {
        recipeRecyclerView.setVisibility(View.VISIBLE);
        tvErrorMessage.setVisibility(View.INVISIBLE);
    }

    private void showProgressBar() {
        recipeRecyclerView.setVisibility(View.INVISIBLE);
        tvErrorMessage.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    //use cached data
    private void loadRecipes() {
        LoaderManager loaderManager = getActivity().getSupportLoaderManager();
        loaderManager.initLoader(RECIPES_LOADER, null, this);
    }

    //refetch movies from internet
    private void reloadRecipes(){
        LoaderManager loaderManager = getActivity().getSupportLoaderManager();
        loaderManager.restartLoader(RECIPES_LOADER, null, this);
    }

    @Override
    public Loader<List<Recipe>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<List<Recipe>>(getContext()) {

            private List<Recipe> cachedRecipes;

            @Override
            protected void onStartLoading() {
                if (cachedRecipes != null && !cachedRecipes.isEmpty()) {
                    deliverResult(cachedRecipes);
                } else {
                    showProgressBar();
                    forceLoad();
                }
            }

            @Override
            public List<Recipe> loadInBackground() {
                try {
                    Response<List<Recipe>> response = recipeService.getAllRecipe().execute();
                    if (response.isSuccessful()) {
                        return response.body();
                    } else {
                        Log.i(TAG, String.format("Message : %s,Response code: %s", response.message(), response.code()));
                        return Collections.emptyList();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return Collections.emptyList();
                }
            }

            @Override
            public void deliverResult(List<Recipe> recipes) {
                cachedRecipes = recipes;
                super.deliverResult(recipes);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Recipe>> loader, List<Recipe> recipes) {
        progressBar.setVisibility(View.INVISIBLE);
        if (recipes.isEmpty()) {
            showErrorMessage();
        } else {
            recipeRecyclerViewAdapter.setRecipes(recipes);
            showRecipesView();
            if(onRecipesFetchedListener != null){
                onRecipesFetchedListener.onRecipesFetched(recipes);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Recipe>> loader) {
        recipeRecyclerViewAdapter.setRecipes(null);
    }

    public interface OnRecipesFetchedListener{

        void onRecipesFetched(List<Recipe> fetchedRecipes);

    }
}
