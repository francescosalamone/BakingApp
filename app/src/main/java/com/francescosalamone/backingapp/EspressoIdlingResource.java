package com.francescosalamone.backingapp;

import android.support.test.espresso.IdlingResource;

/**
 * Created by Francesco on 08/05/2018.
 */

public class EspressoIdlingResource {

    private static final String RESOURCE = "MAIN";

    public static DownloadProgressIdlingResource mCountingIdlingResource =
            new DownloadProgressIdlingResource(RESOURCE);

    public static void increment(){
        mCountingIdlingResource.increment();
    }

    public static void decrement(){
        mCountingIdlingResource.decrement();
    }

    public static IdlingResource getIdlingResource(){
        return mCountingIdlingResource;
    }

}
