package com.pereiraprive.beerlover;

import android.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.os.Bundle;

public class BeerList extends ActionBarActivity {

    // Method called once the Activity is launched
    protected void onCreate() {

        // Set the "beer_list" content
        setContentView(R.layout.beer_list);
    }
}