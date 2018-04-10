package com.francescosalamone.backingapp.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Francesco on 05/04/2018.
 */

public class NetworkUtility {
    public static final String RECIPES_URI = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    public static final String UNSPLASH_BASE_URI = "https://api.unsplash.com/search/photos?page=1&per_page=1&orientation=landscape&order_by=popular";
    public static final String UNSPLASH_QUERY = "query";
    public static final String UNSPLASH_DISH =" dish";
    public static final String UNSPLASH_API = "client_id";

    public static String getContentFromHttp(String recipesUri, URL unsplashURL) throws IOException {
        URL url = null;

        if(recipesUri != null){
            try {
                url = new URL(recipesUri);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } else if(unsplashURL != null){
            url = unsplashURL;
        }


        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream inputStream = httpURLConnection.getInputStream();

            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }

        } finally {
            httpURLConnection.disconnect();
        }
    }

    public static boolean checkInternetConnection(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnectedOrConnecting();
    }

    public static URL buildUrl (String apiKey, String baseURL, String query){
        Uri uri = Uri.parse(baseURL).buildUpon()
                .appendQueryParameter(UNSPLASH_QUERY, query + UNSPLASH_DISH)
                .appendQueryParameter(UNSPLASH_API, apiKey)
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }
}
