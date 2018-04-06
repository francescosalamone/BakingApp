package com.francescosalamone.backingapp.Adapter;

import android.content.ContentUris;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.francescosalamone.backingapp.BuildConfig;
import com.francescosalamone.backingapp.Model.Recipes;
import com.francescosalamone.backingapp.R;
import com.francescosalamone.backingapp.databinding.RecipesItemsBinding;

import java.util.List;

/**
 * Created by Francesco on 05/04/2018.
 */

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder> {
    List<Recipes> recipes;
    RecipesItemsBinding mBinding;

    // https://api.unsplash.com/search/photos?page=1&per_page=1&query=office&client_id=MY_API_KEY
    private String unsplashApiKey = BuildConfig.api;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.recipes_items;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutId, parent, false);
        mBinding = DataBindingUtil.bind(view);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipes recipe = recipes.get(position);
        //TODO show the data in the recyclerView
        if(recipe.getName().equals("")){
            mBinding.recipesNameTv.setText(R.string.noNameRecipe);
        } else {
            mBinding.recipesNameTv.setText(recipe.getName());
        }
    }

    @Override
    public int getItemCount() {
        if(recipes == null) {
            return 0;
        } else {
            return recipes.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {

            super(itemView);
        }
    }

    public void setRecipes(List<Recipes> recipes){
        this.recipes = recipes;
        notifyDataSetChanged();
    }
}
