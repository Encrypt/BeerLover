package com.pereiraprive.beerlover;

import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.view.View.OnClickListener;

public class BeerDescription extends ActionBarActivity {

    // Needed variables for this view
    private boolean isBookmarked = false;
    private ImageButton bookmarkButton = (ImageButton) findViewById(R.id.starButton);

    // Method called once the Activity is launched
    protected void onCreate() {

        // Set the "beer_list" content
        setContentView(R.layout.beer_list);
    }

    // Method called once the button has been pressed
    private void bookmarkClick() {


    }
    /*
    bookmarkButton.setOnClickListener(new View.OnClickListener() {

        // Method called once the bookmark button has been pressed
        @Override
        public void onClick(View v) {
            myFancyMethod(v);
        }
    });
    */
}
