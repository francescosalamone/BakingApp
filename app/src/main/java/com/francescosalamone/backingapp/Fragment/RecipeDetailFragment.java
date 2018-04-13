package com.francescosalamone.backingapp.Fragment;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.francescosalamone.backingapp.Adapter.RecipesAdapter;
import com.francescosalamone.backingapp.Adapter.StepsShortDescriptionAdapter;
import com.francescosalamone.backingapp.Model.Recipes;
import com.francescosalamone.backingapp.R;
import com.francescosalamone.backingapp.Utils.JsonUtils;
import com.francescosalamone.backingapp.databinding.FragmentRecipeDetailBinding;
import com.squareup.picasso.Picasso;


public class RecipeDetailFragment extends Fragment implements StepsShortDescriptionAdapter.ItemClickListener {

    private static final String RECIPE_INSTANCE_STATE = "recipe";
    private static final String BACKGROUND_INSTANCE_STATE = "background";
    private static final String TEXTCOLOR_INSTANCE_STATE = "textColor";
    private FragmentRecipeDetailBinding mBinding;
    private StepsShortDescriptionAdapter mStepsShortDescriptionAdapter;
    private int backgroundColor;
    private int textColor;
    private Recipes recipe;

    public RecipeDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RECIPE_INSTANCE_STATE, recipe);
        outState.putInt(BACKGROUND_INSTANCE_STATE, backgroundColor);
        outState.putInt(TEXTCOLOR_INSTANCE_STATE, textColor);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_detail, container, false);
        View rootView = mBinding.getRoot();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mBinding.shortStepsDescriptionFragmentRv.setLayoutManager(layoutManager);
        mBinding.shortStepsDescriptionFragmentRv.setHasFixedSize(true);

        mStepsShortDescriptionAdapter = new StepsShortDescriptionAdapter(this);
        mBinding.shortStepsDescriptionFragmentRv.setAdapter(mStepsShortDescriptionAdapter);

        if(savedInstanceState == null) {
            Intent intent = getActivity().getIntent();
            recipe = intent.getParcelableExtra(RecipesAdapter.RECIPE);
            backgroundColor = intent.getIntExtra(RecipesAdapter.BACKGROUND, getResources().getColor(R.color.colorAccent));
            textColor = intent.getIntExtra(RecipesAdapter.TEXT_COLOR, 0xFFFFFF);
        } else {
            recipe = savedInstanceState.getParcelable(RECIPE_INSTANCE_STATE);
            backgroundColor = savedInstanceState.getInt(BACKGROUND_INSTANCE_STATE);
            textColor = savedInstanceState.getInt(TEXTCOLOR_INSTANCE_STATE);
        }

        mBinding.recipeNameFragmentTv.setText(recipe.getName());
        mBinding.servingFragmentTv.setText(String.valueOf(recipe.getServings()));
        mBinding.recipeIngredientsFragmentTv.setText(recipe.getIngredientsListAsText());
        mBinding.forkAndKnife.setColorFilter(backgroundColor);
        if(!recipe.getImage().equals(JsonUtils.NO_URL_AVAILABLE)) {
            Picasso.get()
                    .load(recipe.getImage())
                    .into(mBinding.recipeImageFragmentIv);
        }

        mStepsShortDescriptionAdapter.setBackground(backgroundColor);
        mStepsShortDescriptionAdapter.setTextColor(textColor);
        mStepsShortDescriptionAdapter.setSteps(recipe.getSteps());
        return rootView;
    }

    @Override
    public void onItemClicked(int position) {

    }
}
