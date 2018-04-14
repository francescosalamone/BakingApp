package com.francescosalamone.backingapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Francesco on 05/04/2018.
 */

public class Recipes implements Parcelable {
    private String name;
    private List<Ingredients> ingredients;
    private List<Steps> steps;
    private int servings; //number of portions
    private String image;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredients> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredients> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Steps> getSteps() {
        return steps;
    }

    public void setSteps(List<Steps> steps) {
        this.steps = steps;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeList(this.ingredients);
        dest.writeList(this.steps);
        dest.writeInt(this.servings);
        dest.writeString(this.image);
    }

    public Recipes() {
    }

    protected Recipes(Parcel in) {
        this.name = in.readString();
        this.ingredients = new ArrayList<Ingredients>();
        in.readList(this.ingredients, Ingredients.class.getClassLoader());
        this.steps = new ArrayList<Steps>();
        in.readList(this.steps, Steps.class.getClassLoader());
        this.servings = in.readInt();
        this.image = in.readString();
    }

    public static final Parcelable.Creator<Recipes> CREATOR = new Parcelable.Creator<Recipes>() {
        @Override
        public Recipes createFromParcel(Parcel source) {
            return new Recipes(source);
        }

        @Override
        public Recipes[] newArray(int size) {
            return new Recipes[size];
        }
    };

    public String getIngredientsListAsText(){
        StringBuilder ingredientsAsText = new StringBuilder();
        for (int i=0; i<ingredients.size(); i++){
            if(!ingredients.get(i).getIngredient().equals("")){
                ingredientsAsText.append(ingredients.get(i).getIngredient());
                if(i < ingredients.size()-1){
                    ingredientsAsText.append(", ");
                }
            }
        }
        return ingredientsAsText.toString();
    }
}
