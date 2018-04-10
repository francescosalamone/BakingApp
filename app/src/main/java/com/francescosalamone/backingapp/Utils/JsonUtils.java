package com.francescosalamone.backingapp.Utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Francesco on 08/04/2018.
 */

public class JsonUtils {
    public static final String NO_URL_AVAILABLE = "-1";

    public static String getImageFromUnsplashJson(String data) throws JSONException {
        final String UNSPLASH_IMAGE = "small";
        final String UNSPLASH_RESULTS = "results";
        final String UNSPLASH_URLS = "urls";

        JSONObject unsplashObject = new JSONObject(data);
        if(unsplashObject.has(UNSPLASH_RESULTS)){
            JSONArray unsplashResults = unsplashObject.getJSONArray(UNSPLASH_RESULTS);
            JSONObject unsplashObjectResult = new JSONObject(unsplashResults.getString(0));
            if(unsplashObjectResult.has(UNSPLASH_URLS)){
                JSONObject unsplashUrlObject = new JSONObject(String.valueOf(unsplashObjectResult.optString(UNSPLASH_URLS)));
                if(unsplashUrlObject.has(UNSPLASH_IMAGE)){
                    return unsplashUrlObject.optString(UNSPLASH_IMAGE);
                }
            }
        }


        return NO_URL_AVAILABLE;
    }
}
