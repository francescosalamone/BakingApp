package com.francescosalamone.backingapp;

import android.support.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Francesco on 08/05/2018.
 */

public final class DownloadProgressIdlingResource implements IdlingResource {

    private final String mResourceName;
    private final AtomicInteger counter = new AtomicInteger(0);
    private volatile ResourceCallback mCallback;

    public DownloadProgressIdlingResource(String mResourceName) {
        this.mResourceName = mResourceName;
    }

    @Override
    public String getName() {
        return mResourceName;
    }

    @Override
    public boolean isIdleNow() {
        return counter.get() == 0;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.mCallback = callback;
    }

    public void increment(){
        counter.getAndIncrement();
    }

    public void decrement(){
        int counterVal = counter.decrementAndGet();
        if(counterVal == 0){
            if(null != mCallback){
                mCallback.onTransitionToIdle();
            }
        }

        if(counterVal < 0){
            throw new IllegalArgumentException("counter corrupted");
        }
    }
}
