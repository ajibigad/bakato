package com.ajibigad.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.ajibigad.bakingapp.IngredientFragment;
import com.ajibigad.bakingapp.R;
import com.ajibigad.bakingapp.data.Recipe;
import com.ajibigad.bakingapp.utils.AppConstants;

import org.parceler.Parcels;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    private static final String TAG = RecipeWidgetProvider.class.getSimpleName();

    private Recipe selectedRecipe;

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                         int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views;

        if(selectedRecipe != null){
            views = new RemoteViews(context.getPackageName(), R.layout.ingredient_view);
            views.setTextViewText(R.id.tv_ingredients, IngredientFragment.formatIngredientsAsBulletPoints(context, selectedRecipe.getIngredients()));

            Intent intent = new Intent(context, RecipeWidgetProvider.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{appWidgetId});
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.btn_view_recipes, pendingIntent);
            selectedRecipe = null;
        }else {
            views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
            Intent remoteServiceIntent = new Intent(context, RecipeWidgetRemoteViewsService.class);
            views.setRemoteAdapter(R.id.recipe_list, remoteServiceIntent);
            views.setEmptyView(R.id.recipe_list, R.id.tv_error_message_display);

            // template to handle the click listener for each item
            Intent clickIntentTemplate = new Intent(context, RecipeWidgetProvider.class);
            clickIntentTemplate.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            clickIntentTemplate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{appWidgetId});

            PendingIntent clickPendingIntentTemplate = PendingIntent.getBroadcast(context,
                    0, clickIntentTemplate, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.recipe_list, clickPendingIntentTemplate);
        }
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public static void sendRefreshBroadcast(Context context) {
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.setComponent(new ComponentName(context, RecipeWidgetProvider.class));
        context.sendBroadcast(intent);
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        final String action = intent.getAction();
        if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            // refresh all your widgets
            AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            ComponentName cn = new ComponentName(context, RecipeWidgetProvider.class);

            if(intent.hasExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS)  && intent.hasExtra(AppConstants.SELECTED_RECIPE)){
                selectedRecipe = Parcels.unwrap(intent.getBundleExtra(AppConstants.SELECTED_RECIPE).getParcelable(AppConstants.SELECTED_RECIPE));
            }

            mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.recipe_list);

            Log.i(TAG, "WIDGET UPDATE BROADCAST SENT");
        }
        super.onReceive(context, intent);
    }
}

