package com.pereiraprive.beerlover;

import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.view.View.OnClickListener;

public class BeerDescription extends ActionBarActivity {

    // Needed variables for this view
    private boolean isBookmarked = true;
    private String description, origin, drinker;
    private ImageButton bookmarkButton;
    private ImageView beerImage;

    // Method called once the Activity is launched
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the "beer_list" content
        setContentView(R.layout.beer_description);

        // Retrieves the objects
        bookmarkButton = (ImageButton) findViewById(R.id.starButton);
        beerImage = (ImageView) findViewById(R.id.beerImage);

        // Adds a Listener to the star
        bookmarkButton.setOnClickListener(new View.OnClickListener() {

            // Method called once the bookmark button has been pressed
            @Override
            public void onClick(View v) {
                bookmarkClick();
            }
        });

        // Fills the objects (beer name, description, origin...)
        fillView();

    }

    // Method called to retrieve and set the different elements in teh view
    private void fillView() {
        beerImage.setImageResource(R.drawable.no_image);

    }

    // Method called once the button has been pressed
    private void bookmarkClick() {

        // If the beer is bookmarked
        if (isBookmarked) {

            // Changes the background image
            bookmarkButton.setBackgroundResource(R.drawable.empty_star);

            // Removes the bookmark in the database
            // TODO


        }

    }

}