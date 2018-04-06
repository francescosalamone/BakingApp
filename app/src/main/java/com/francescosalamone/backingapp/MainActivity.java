package com.francescosalamone.backingapp;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Loader;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;

import com.francescosalamone.backingapp.Adapter.RecipesAdapter;
import com.francescosalamone.backingapp.Model.Recipes;
import com.francescosalamone.backingapp.Utils.GsonUtils;
import com.francescosalamone.backingapp.Utils.NetworkUtility;
import com.francescosalamone.backingapp.databinding.ActivityMainBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    private static final int RECIPES_LOADER = 101;
    private ActivityMainBinding mBinding;
    private RecipesAdapter mRecipesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.recipesRv.setLayoutManager(layoutManager);
        mBinding.recipesRv.setHasFixedSize(true);

        mRecipesAdapter = new RecipesAdapter();
        mBinding.recipesRv.setAdapter(mRecipesAdapter);

        updateRecipesList();
    }

    private void updateRecipesList(){
        boolean isConnected = NetworkUtility.checkInternetConnection(this);
        if(isConnected){
            try{
                if(getLoaderManager().getLoader(RECIPES_LOADER).isStarted()){
                    getLoaderManager().restartLoader(RECIPES_LOADER, null, this);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                getLoaderManager().initLoader(RECIPES_LOADER, null, this);
            }
        }
    }

    @Override
    public Loader<String> onCreateLoader(int i, Bundle bundle) {

        return new AsyncTaskLoader<String>(this) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }

            @Override
            public String loadInBackground() {
                try {
                    return NetworkUtility.getContentFromHttp();
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
                return;
            }

            mRecipesAdapter.setRecipes(recipesList);
            getSupportLoaderManager().destroyLoader(RECIPES_LOADER);
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
