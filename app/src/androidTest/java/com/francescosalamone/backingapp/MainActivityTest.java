package com.francescosalamone.backingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResourceTimeoutException;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;


/**
 * Created by Francesco on 07/05/2018.
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    private static final String CAKE_NAME = "Nutella Pie";
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule
            = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void registerIdlingResource(){
        IdlingRegistry.getInstance().register(EspressoIdlingResource.getIdlingResource());
    }

    @Test
    public void clickRecipe_openRecipeDetail(){

        //After waiting for download check that the first item has name Nutella Pie
        onView(withId(R.id.recipes_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.recipe_name_fragment_tv)).check(matches(withText(CAKE_NAME)));
    }

    @After
    public void unregisterIdlingResource(){
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.getIdlingResource());
    }
}
