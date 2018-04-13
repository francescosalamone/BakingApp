package com.francescosalamone.backingapp;

import android.databinding.DataBindingUtil;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;

import com.francescosalamone.backingapp.Adapter.RecipesAdapter;
import com.francescosalamone.backingapp.Model.Recipes;
import com.francescosalamone.backingapp.Utils.GsonUtils;
import com.francescosalamone.backingapp.Utils.JsonUtils;
import com.francescosalamone.backingapp.Utils.NetworkUtility;
import com.francescosalamone.backingapp.databinding.ActivityMainBinding;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>,
RecipesAdapter.ItemClickListener{

    private static final String RECIPES_NAME = "name";
    private static final String RECIPES_INSTANCE_STATE = "recipesList";
    private static final int RECIPES_LOADER = 101;
    //the real loader id will be the result of UNSPLASH_LOADER_BASE + index of the element in the list
    //in this way will be possible to know where is the position of the list to edit with the new image
    private static final int UNSPLASH_LOADER_BASE = 10000;

    private ActivityMainBinding mBinding;
    private RecipesAdapter mRecipesAdapter;

    // https://api.unsplash.com/search/photos?page=1&per_page=1&query=office&client_id=MY_API_KEY
    private String unsplashApiKey = BuildConfig.api;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(RECIPES_INSTANCE_STATE, (ArrayList <? extends Parcelable>) mRecipesAdapter.getRecipes());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int numberOfColumns = 1;
        if(getResources().getBoolean(R.bool.isLargeScreen)){
            numberOfColumns = 2;
        }

        GridLayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.recipesRv.setLayoutManager(layoutManager);
        mBinding.recipesRv.setHasFixedSize(true);

        mRecipesAdapter = new RecipesAdapter(this);
        mBinding.recipesRv.setAdapter(mRecipesAdapter);

        if(savedInstanceState != null){
            mRecipesAdapter.setRecipes(savedInstanceState.<Recipes>getParcelableArrayList(RECIPES_INSTANCE_STATE));
        } else {
            updateDataFromInternet(RECIPES_LOADER, null);
        }

        mBinding.swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateDataFromInternet(RECIPES_LOADER, null);
            }
        });
    }

    private void updateDataFromInternet(int whichLoader, Bundle bundle){
        boolean isConnected = NetworkUtility.checkInternetConnection(this);
        if(isConnected){
            try{
                if(getSupportLoaderManager().getLoader(whichLoader).isStarted()){
                    getSupportLoaderManager().restartLoader(whichLoader, bundle, this);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                getSupportLoaderManager().initLoader(whichLoader, bundle, this);
            }
        } else {
            mBinding.swipeToRefresh.setRefreshing(false);
        }
    }

    @Override
    public Loader<String> onCreateLoader(final int i, final Bundle bundle) {

        return new AsyncTaskLoader<String>(this) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }

            @Override
            public String loadInBackground() {
                try {
                    if(i == RECIPES_LOADER) {
                        return NetworkUtility.getContentFromHttp(NetworkUtility.RECIPES_URI, null);
                        //all loader >= UNSPLASH_LOADER_BASE belong to unsplash loader
                    } else if(i >= UNSPLASH_LOADER_BASE){
                        String query = bundle.getString(RECIPES_NAME);
                        return NetworkUtility.getContentFromHttp(null, NetworkUtility.buildUrl(unsplashApiKey,
                                NetworkUtility.UNSPLASH_BASE_URI, query));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String json) {
        if(loader.getId() == RECIPES_LOADER){
            List<Recipes> recipesList = GsonUtils.parseGsonToRecipes(json);

            if(recipesList == null || recipesList.size() == 0){
                getSupportLoaderManager().destroyLoader(RECIPES_LOADER);
                mBinding.swipeToRefresh.setRefreshing(false);
                return;
            }
            mRecipesAdapter.setRecipes(recipesList);

            for(int i =0; i< recipesList.size(); i++){
                if(recipesList.get(i).getImage().equals("")){
                    Bundle bundle = new Bundle();
                    bundle.putString(RECIPES_NAME, recipesList.get(i).getName());
                    updateDataFromInternet(UNSPLASH_LOADER_BASE + i, bundle);
                }
            }

            getSupportLoaderManager().destroyLoader(RECIPES_LOADER);
            mBinding.swipeToRefresh.setRefreshing(false);
        } else if(loader.getId() >= UNSPLASH_LOADER_BASE){
            try {
                String imageUrl = JsonUtils.getImageFromUnsplashJson(json);
                int position = loader.getId() - UNSPLASH_LOADER_BASE;
                mRecipesAdapter.updatePicture(imageUrl, position);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            getSupportLoaderManager().destroyLoader(loader.getId());
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    @Override
    public void onItemClick(int clickItemPosition) {

    }
}
