package com.ajibigad.bakingapp;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.ajibigad.bakingapp.adapter.OnListFragmentInteractionListener;
import com.ajibigad.bakingapp.data.Recipe;
import com.ajibigad.bakingapp.data.Step;

import org.parceler.Parcels;

import java.util.List;

import static com.ajibigad.bakingapp.StepFragment.SELECTED_STEP;
import static com.ajibigad.bakingapp.utils.AppConstants.FETCHED_RECIPES;
import static com.ajibigad.bakingapp.utils.AppConstants.SELECTED_RECIPE;

public class RecipeActivity extends AppCompatActivity implements OnListFragmentInteractionListener<Step>{

    private Recipe selectedRecipe;

    private FragmentManager fragmentManager;

    private boolean isShowingStep, isTableLayout;

    private List<Recipe> recipes;
    private int selectedRecipeIndex = 0;
    private int selectedStepIndex = 0;

    private MenuItem previousButton, nextButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        isTableLayout = findViewById(R.id.recipe_tablet_layout) != null;

        if(getIntent().hasExtra(SELECTED_RECIPE) && getIntent().hasExtra(FETCHED_RECIPES)){
            selectedRecipe = Parcels.unwrap(getIntent().getParcelableExtra(SELECTED_RECIPE));
            recipes = Parcels.unwrap(getIntent().getParcelableExtra(FETCHED_RECIPES));

            getSupportActionBar().setTitle(selectedRecipe.getName());
            setSelectedRecipeIndex();

            IngredientFragment ingredientFragment = new IngredientFragment();
            ingredientFragment.setArguments(getIntent().getExtras());

            StepsFragment stepsFragment = new StepsFragment();
            stepsFragment.setArguments(getIntent().getExtras());

            fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().add(R.id.ingredient_container, ingredientFragment)
                    .add(R.id.steps_container, stepsFragment);
            if(isTableLayout){
                fragmentTransaction.add(R.id.step_container, generateStepFragment(selectedRecipe.getSteps().get(0)));
            }
            fragmentTransaction.commit();


            bottomNavigationView.setOnNavigationItemSelectedListener(
                    new BottomNavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.action_previous:
                                    if(isShowingStep){
                                        Step selectedStep = getPreviousStep();
                                        displayStepView(selectedStep);
                                    } else {
                                        selectedRecipe = getPreviousRecipe();
                                        changeRecipeDisplayed();
                                    }
                                    break;

                                case R.id.action_next:
                                    if(isShowingStep){
                                        Step selectedStep = getNextStep();
                                        displayStepView(selectedStep);
                                    } else {
                                        selectedRecipe = getNextRecipe();
                                        changeRecipeDisplayed();
                                    }
                                    break;

                            }
                            handleNavigationButtonsState();
                            return true;
                        }
                    });
            previousButton = bottomNavigationView.getMenu().getItem(0);
            nextButton = bottomNavigationView.getMenu().getItem(1);
            handleNavigationButtonsState();
        }
        else finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                if(isShowingStep){
                    removeStepView();
                }else{
                    NavUtils.navigateUpFromSameTask(this);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(isShowingStep){
            removeStepView();
        }else{
            super.onBackPressed();
        }
    }

    private void setSelectedRecipeIndex(){
        for(int index = 0; index < recipes.size(); index++){
            if(recipes.get(index).getId() == selectedRecipe.getId()){
                selectedRecipeIndex = index;
                break;
            }
        }
    }

    private void setSelectedStepIndex(Step selectedStep){
        for (int index = 0; index < selectedRecipe.getSteps().size(); index++){
            if(selectedRecipe.getSteps().get(index).getId() == selectedStep.getId()){
                selectedStepIndex = index;
            }
        }
    }

    private void changeRecipeDisplayed(){
        getSupportActionBar().setTitle(selectedRecipe.getName());

        Bundle recipeBundle = new Bundle();
        recipeBundle.putParcelable(SELECTED_RECIPE, Parcels.wrap(selectedRecipe));

        IngredientFragment ingredientFragment = new IngredientFragment();
        ingredientFragment.setArguments(recipeBundle);

        StepsFragment stepsFragment = new StepsFragment();
        stepsFragment.setArguments(recipeBundle);

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.ingredient_container, ingredientFragment)
                .replace(R.id.steps_container, stepsFragment);
        if(isTableLayout){
            fragmentTransaction.add(R.id.step_container, generateStepFragment(selectedRecipe.getSteps().get(0)));
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onListFragmentInteraction(Step selectedStep) {
        Toast.makeText(this, "Step selected " + selectedStep.getShortDescription(), Toast.LENGTH_SHORT).show();
        displayStepView(selectedStep);
    }

    private StepFragment generateStepFragment(Step selectedStep){
        StepFragment stepFragment = new StepFragment();
        Bundle stepBundle = new Bundle();
        stepBundle.putParcelable(SELECTED_STEP, Parcels.wrap(selectedStep));
        stepFragment.setArguments(stepBundle);
        return stepFragment;
    }

    private void displayStepView(Step selectedStep){
        getSupportActionBar().setTitle(selectedStep.getShortDescription());
        setSelectedStepIndex(selectedStep);
        if (isTableLayout){
            fragmentManager.beginTransaction().replace(R.id.step_container, generateStepFragment(selectedStep)).commit();
            handleNavigationButtonsState();
            return;
        }
        else if(isShowingStep){
            fragmentManager.beginTransaction().replace(R.id.step_container, generateStepFragment(selectedStep)).commit();
        }
        else{
            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentById(R.id.ingredient_container))
                    .hide(fragmentManager.findFragmentById(R.id.steps_container))
                    .add(R.id.step_container, generateStepFragment(selectedStep)).commit();
        }
        isShowingStep = true;
        handleNavigationButtonsState();
    }

    private void removeStepView(){
        getSupportActionBar().setTitle(selectedRecipe.getName());
        fragmentManager.beginTransaction().show(fragmentManager.findFragmentById(R.id.ingredient_container))
                .show(fragmentManager.findFragmentById(R.id.steps_container))
                .remove(fragmentManager.findFragmentById(R.id.step_container)).commit();
        isShowingStep = false;
    }

    private Recipe getPreviousRecipe(){
        return recipes.get(--selectedRecipeIndex);
    }

    private Recipe getNextRecipe(){
        return recipes.get(++selectedRecipeIndex);
    }

    private Step getPreviousStep(){
        return selectedRecipe.getSteps().get(--selectedStepIndex);
    }

    private Step getNextStep(){
        return selectedRecipe.getSteps().get(++selectedStepIndex);
    }

    private void handlePreviousBottonState(){
        if(isShowingStep){
            previousButton.setEnabled(selectedStepIndex != 0);
        }
        else{
            previousButton.setEnabled(selectedRecipeIndex != 0);
        }
    }

    private void handleNextBottonState(){
        if(isShowingStep){
            nextButton.setEnabled(selectedStepIndex != selectedRecipe.getSteps().size() - 1);
        }
        else {
            nextButton.setEnabled(selectedRecipeIndex != recipes.size() - 1);
        }
    }

    private void handleNavigationButtonsState(){
        handlePreviousBottonState();
        handleNextBottonState();
    }
}
