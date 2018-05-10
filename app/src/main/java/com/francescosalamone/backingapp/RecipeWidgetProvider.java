package com.francescosalamone.backingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.widget.RemoteViews;

import com.francescosalamone.backingapp.Model.Recipes;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider{

    private static final String WIDGET_UPDATE = "DATA_WIDGET_UPDATED";
    private static final String RECIPE = "recipe";
    public static final String BACKGROUND = "backgroundColor";
    public static final String TEXT_COLOR = "textColor";
    private static Recipes recipe = null;
    private static int background = -1;
    private static int textcolor = -1;


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);

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

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if(intent.getAction().equals(WIDGET_UPDATE)) {
            recipe = intent.getParcelableExtra(RECIPE);
            background = intent.getIntExtra(BACKGROUND, context.getResources().getColor(R.color.colorAccent));
            textcolor = intent.getIntExtra(TEXT_COLOR, 0xFFFFFF);
            final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName name = new ComponentName(context, RecipeWidgetProvider.class);
            int[] appWidgetId = AppWidgetManager.getInstance(context).getAppWidgetIds(name);
            final int length = appWidgetId.length;
            if (length >= 1){
                int id = appWidgetId[length-1];
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);
                views.setTextViewText(R.id.recipe_name_widget_tv, recipe.getName());
                views.setTextViewText(R.id.recipe_ingredients_widget_tv, recipe.getIngredientsListAsText());

                if(recipe != null) {
                    Intent launchActivityIntent = new Intent(context, DetailActivity.class);

                    launchActivityIntent.putExtras(intent);
                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, launchActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    views.setOnClickPendingIntent(R.id.recipe_ingredients_widget_tv, pendingIntent);
                }

                // Instruct the widget manager to update the widget
                appWidgetManager.updateAppWidget(id, views);
            }
        }
    }
}

