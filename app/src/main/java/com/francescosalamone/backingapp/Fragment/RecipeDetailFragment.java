package com.francescosalamone.backingapp.Fragment;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.francescosalamone.backingapp.Adapter.RecipesAdapter;
import com.francescosalamone.backingapp.Adapter.StepsShortDescriptionAdapter;
import com.francescosalamone.backingapp.Model.Recipes;
import com.francescosalamone.backingapp.Model.Steps;
import com.francescosalamone.backingapp.R;
import com.francescosalamone.backingapp.Utils.JsonUtils;
import com.francescosalamone.backingapp.databinding.FragmentRecipeDetailBinding;
import com.squareup.picasso.Picasso;

import java.util.List;


public class RecipeDetailFragment extends Fragment implements StepsShortDescriptionAdapter.ItemClickListener {

    private static final String RECIPE_INSTANCE_STATE = "recipe";
    private static final String BACKGROUND_INSTANCE_STATE = "background";
    private static final String TEXTCOLOR_INSTANCE_STATE = "textColor";
    private static final String RV_POSITION_INSTANCE_STATE = "rv_position";

    private FragmentRecipeDetailBinding mBinding;
    private int backgroundColor;
    private int textColor;
    private Recipes recipe;
    private LinearLayoutManager layoutManager;

    private OnStepClickListener mCallback;

    public interface OnStepClickListener{
        void onStepClicked(List<Steps> steps, int position);
    }

    //With this we are sure that in the host activity we implement the OnStepClickListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnStepClickListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement OnStepClickListener");
        }
    }

    public RecipeDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RECIPE_INSTANCE_STATE, recipe);
        outState.putInt(BACKGROUND_INSTANCE_STATE, backgroundColor);
        outState.putInt(TEXTCOLOR_INSTANCE_STATE, textColor);
        outState.putInt(RV_POSITION_INSTANCE_STATE, layoutManager.findFirstVisibleItemPosition());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_detail, container, false);
        View rootView = mBinding.getRoot();

        ((AppCompatActivity)getActivity()).setSupportActionBar(mBinding.toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        layoutManager = new LinearLayoutManager(getContext());
        mBinding.shortStepsDescriptionFragmentRv.setLayoutManager(layoutManager);
        mBinding.shortStepsDescriptionFragmentRv.setHasFixedSize(true);

        StepsShortDescriptionAdapter mStepsShortDescriptionAdapter = new StepsShortDescriptionAdapter(this);
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
            layoutManager.scrollToPosition(savedInstanceState.getInt(RV_POSITION_INSTANCE_STATE));
        }

        if(!recipe.getImage().equals(JsonUtils.NO_URL_AVAILABLE)) {
            Picasso.get()
                    .load(recipe.getImage())
                    .into(mBinding.recipeImageFragmentIv);
        }

        mStepsShortDescriptionAdapter.setBackground(backgroundColor);
        mStepsShortDescriptionAdapter.setTextColor(textColor);
        mStepsShortDescriptionAdapter.setRecipe(recipe);

        //Show the title only when the toolbar is collapsed
        mBinding.appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange=-1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    mBinding.collapsingToolbar.setTitle(recipe.getName());
                    mBinding.backgroundToolbar.setVisibility(View.VISIBLE);
                    mBinding.backgroundToolbar.setBackgroundColor(backgroundColor);
                    mBinding.backgroundToolbar.setAlpha(0.6f);
                    isShow = true;
                } else if(isShow) {
                    mBinding.backgroundToolbar.setVisibility(View.GONE);
                    mBinding.collapsingToolbar.setTitle(" ");//carefull there should be a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });
        return rootView;
    }

    @Override
    public void onItemClicked(List<Steps> steps, int position) {
        mCallback.onStepClicked(steps, position);
    }
}
