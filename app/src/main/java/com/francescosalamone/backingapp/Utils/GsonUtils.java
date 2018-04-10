package com.francescosalamone.backingapp.Utils;

import android.util.Log;

import com.francescosalamone.backingapp.Model.Recipes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by Francesco on 05/04/2018.
 */

public class GsonUtils {

    public static List<Recipes> parseGsonToRecipes(String data) {
        List<Recipes> recipesList;
        Type recipesListType = new TypeToken<ArrayList<Recipes>>(){}.getType();

        Gson gson = new Gson();
        recipesList = gson.fromJson(data, recipesListType);

        return recipesList;
    }

    public static String parseUnsplash(String data){
        class Urls {
            String regular;
        }
        class Results{
            Urls urls;
        }
        class Unsplash{
            List<Results> results;
        }

        Unsplash unsplashData;
        Gson gson = new Gson();
        unsplashData = gson.fromJson(data, Unsplash.class);

        return unsplashData.results.get(0).urls.regular;
    }

}
