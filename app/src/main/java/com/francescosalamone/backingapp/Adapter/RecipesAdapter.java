package com.francescosalamone.backingapp.Adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.francescosalamone.backingapp.DetailActivity;
import com.francescosalamone.backingapp.Model.Recipes;
import com.francescosalamone.backingapp.R;
import com.francescosalamone.backingapp.Utils.JsonUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Francesco on 05/04/2018.
 */

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder> {

    public static final String RECIPE = "recipe";
    public static final String BACKGROUND = "backgroundColor";
    public static final String TEXT_COLOR = "textColor";
    private List<Recipes> recipes = new ArrayList<>();
    //private RecipesItemsBinding mBinding;

    final private ItemClickListener clickListener;

    final Set<Target> protectedFromGarbageCollectorTargets = new HashSet<>();

    public RecipesAdapter(ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ItemClickListener{
        void onItemClick(int clickItemPosition);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.recipes_items;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Recipes recipe = recipes.get(position);
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                //mBinding.recipesImageIv.setImageBitmap(bitmap);
                holder.recipeImage.setImageBitmap(bitmap);

                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        Palette.Swatch swatch = palette.getDarkVibrantSwatch();
                        if(swatch != null){
                            //mBinding.recipesNameTv.setBackgroundColor(swatch.getRgb());
                            //mBinding.recipesNameTv.setTextColor(swatch.getBodyTextColor());
                            holder.recipeName.setBackgroundColor(swatch.getRgb());
                            holder.recipeName.setTextColor(swatch.getBodyTextColor());

                        }
                        protectedFromGarbageCollectorTargets.remove(this);
                    }
                });

            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                protectedFromGarbageCollectorTargets.remove(this);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        //mBinding = DataBindingUtil.bind(holder.itemView);
        if(recipe.getName().equals("")){
            //mBinding.recipesNameTv.setText(R.string.noNameRecipe);
            holder.recipeName.setText(R.string.noNameRecipe);
        } else {
            //mBinding.recipesNameTv.setText(recipe.getName());
            holder.recipeName.setText(recipe.getName());
        }

        protectedFromGarbageCollectorTargets.add(target);
        if(recipe.getImage().equals(JsonUtils.NO_URL_AVAILABLE)){
            Picasso.get()
                    .load(R.drawable.image_not_found)
                    .placeholder(R.drawable.ic_cached_black_24dp)
                    .error(R.drawable.ic_error_black_24dp)
                    .into(target);
        } else if(!recipe.getImage().equals("")){
            Picasso.get()
                    .load(recipe.getImage())
                    .placeholder(R.drawable.ic_cached_black_24dp)
                    .error(R.drawable.ic_error_black_24dp)
                    .into(target);
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


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView recipeImage;
        private TextView recipeName;

        public ViewHolder(View itemView) {
            super(itemView);
            recipeImage = itemView.findViewById(R.id.recipes_image_iv);
            recipeName = itemView.findViewById(R.id.recipes_name_tv);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clicked = getAdapterPosition();
            clickListener.onItemClick(clicked);

            launchDetailActivity(clicked, view.getContext(), recipeName);
        }
    }

    public void launchDetailActivity(int position, Context context, TextView recipeName){
        int background = ((ColorDrawable)recipeName.getBackground()).getColor();
        int textColor = recipeName.getCurrentTextColor();
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(RECIPE, recipes.get(position));
        intent.putExtra(BACKGROUND, background);
        intent.putExtra(TEXT_COLOR, textColor);
        context.startActivity(intent);
    }

    public void setRecipes(List<Recipes> recipes){
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    public void updatePicture(String url, int position){
        recipes.get(position).setImage(url);

        notifyItemChanged(position);
    }

    public List<Recipes> getRecipes() {

        return recipes;
    }

}
