package com.francescosalamone.backingapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.francescosalamone.backingapp.Model.Recipes;
import com.francescosalamone.backingapp.Model.Steps;
import com.francescosalamone.backingapp.R;

import java.util.List;

import static android.view.View.GONE;

/**
 * Created by Francesco on 11/04/2018.
 */

public class StepsShortDescriptionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Recipes recipe;
    private final ItemClickListener clickListener;

    private int background = -1;
    private int textColor = -1;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public StepsShortDescriptionAdapter(ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ItemClickListener{
        void onItemClicked(List<Steps> steps, int itemClicked);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId;

        if(viewType == TYPE_HEADER) {

            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            layoutId = R.layout.fragment_recipe_detail_header;
            View view = inflater.inflate(layoutId, parent, false);
            return new HeaderViewHolder(view);

        } else if (viewType == TYPE_ITEM){
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            layoutId = R.layout.steps_short_description_item;
            View view = inflater.inflate(layoutId, parent, false);
            return new ItemViewHolder(view);
        }

        throw new RuntimeException("No type match for " + viewType + ".");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof ItemViewHolder) {
            int stepPosition = position -1;
            Steps step = recipe.getSteps().get(stepPosition);
            ((ItemViewHolder)holder).stepNumber.setText(String.valueOf(stepPosition));

            if (!step.getShortDescription().isEmpty()) {
                ((ItemViewHolder)holder).shortDescription.setText(step.getShortDescription());
            } else {
                ((ItemViewHolder)holder).shortDescription.setText(R.string.step_not_available);
            }

            if (stepPosition == 0) {
                ((ItemViewHolder)holder).lineUp.setVisibility(GONE);
            } else {
                ((ItemViewHolder)holder).lineUp.setVisibility(View.VISIBLE);
            }

            if (stepPosition == recipe.getSteps().size() - 1) {
                ((ItemViewHolder)holder).lineDown.setVisibility(GONE);
            } else {
                ((ItemViewHolder)holder).lineDown.setVisibility(View.VISIBLE);
            }

            if (background != -1) {
                ((ItemViewHolder)holder).lineUp.setBackgroundColor(background);
                ((ItemViewHolder)holder).lineDown.setBackgroundColor(background);
                ((ItemViewHolder)holder).circleStep.setColorFilter(background);
            }
            if (textColor != -1) {
                ((ItemViewHolder)holder).stepNumber.setTextColor(textColor);
            }
        } else if(holder instanceof HeaderViewHolder){

            ((HeaderViewHolder)holder).recipeName.setText(recipe.getName());
            ((HeaderViewHolder)holder).recipeServings.setText(String.valueOf(recipe.getServings()));
            ((HeaderViewHolder)holder).recipeIngredients.setText(recipe.getIngredientsListAsText());
            ((HeaderViewHolder)holder).forkAndKnifeIcon.setColorFilter(background);
        }
    }

    @Override
    public int getItemCount() {
        if(recipe.getSteps() == null) {
            return 0;
        } else {
            return recipe.getSteps().size()+1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder{
        private TextView recipeName;
        private TextView recipeIngredients;
        private TextView recipeServings;
        private ImageView forkAndKnifeIcon;

        public HeaderViewHolder(View itemView) {
            super(itemView);

            recipeName = itemView.findViewById(R.id.recipe_name_fragment_tv);
            recipeIngredients = itemView.findViewById(R.id.recipe_ingredients_fragment_tv);
            recipeServings = itemView.findViewById(R.id.serving_fragment_tv);
            forkAndKnifeIcon = itemView.findViewById(R.id.fork_and_knife);
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView stepNumber;
        private TextView shortDescription;
        private ImageView circleStep;
        private View lineUp;
        private View lineDown;

        public ItemViewHolder(View itemView) {
            super(itemView);

            stepNumber = itemView.findViewById(R.id.step_number_fragment_tv);
            shortDescription = itemView.findViewById(R.id.short_description_fragment_tv);
            lineUp = itemView.findViewById(R.id.line_up);
            lineDown = itemView.findViewById(R.id.line_down);
            circleStep = itemView.findViewById(R.id.step_circle);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int itemClicked = getAdapterPosition()-1;
            clickListener.onItemClicked(recipe.getSteps(), itemClicked);
        }
    }

    public void setRecipe(Recipes recipe){
        this.recipe = recipe;
        notifyDataSetChanged();
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }
}
