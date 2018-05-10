package com.francescosalamone.backingapp;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;

/**
 * Created by Francesco on 08/05/2018.
 */

@RunWith(AndroidJUnit4.class)
public class StepActivityTest {
    private static final String CAKE_NAME = "Nutella Pie";
    private static final String FIRST_STEP_DESCRIPTION = "Recipe Introduction";
    private static final String SECOND_STEP_DESCRIPTION ="1. Preheat the oven to 350°F. Butter a 9\" deep dish pie pan.";

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule
            = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void registerIdlingResource(){
        IdlingRegistry.getInstance().register(EspressoIdlingResource.getIdlingResource());
    }

    @Test
    public void clickButtons_showSteps(){
        //After waiting for download check that the first item has name Nutella Pie
        onView(withId(R.id.recipes_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.recipe_name_fragment_tv)).check(matches(withText(CAKE_NAME)));

        onView(withId(R.id.short_steps_description_fragment_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        //Check that next button go in second step
        onView(withId(R.id.step_description_fragment_tv)).check(matches(withText(FIRST_STEP_DESCRIPTION)));
        onView(withId(R.id.next_step_bt)).perform(click());
        onView(withId(R.id.step_description_fragment_tv)).check(matches(withText(SECOND_STEP_DESCRIPTION)));

        //Check that prev button go in first step and it hasn't prev button
        onView(withId(R.id.prev_step_bt)).perform(click());
        onView(withId(R.id.step_description_fragment_tv)).check(matches(withText(FIRST_STEP_DESCRIPTION)));
        onView(withId(R.id.prev_step_bt)).check(matches(not(isDisplayed())));
    }

    @After
    public void unregisterIdlingResource(){
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.getIdlingResource());
    }
}
