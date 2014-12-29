package com.pereiraprive.beerlover;

import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class BeerDescription extends ActionBarActivity {

    // Needed variables for this view
    private boolean isBookmarked = true;
    private TextView name, description, origin, drinker;
    private ImageButton bookmarkButton;
    private ImageView beerImage;

    // Method called once the Activity is launched
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the "beer_list" content
        setContentView(R.layout.beer_description);

        // Retrieves the objects
        name = (TextView) findViewById(R.id.beerName);
        description = (TextView) findViewById(R.id.beerDescription);
        origin = (TextView) findViewById(R.id.beerOrigin);
        drinker = (TextView) findViewById(R.id.beerDrinker);
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
        fillBearInfo();

    }

    // Method called to retrieve and set the different elements in teh view
    private void fillBearInfo() {

        // Local objects
        String webContent = null;
        JSONObject webJson = null, originJson = null;
        DownloadTask dlTask;
        String nameString = new String();
        String descriptionString = new String();
        String originString = new String();
        String drinkerString = new String();

        // Creates a new download task with the appropriate random value
        dlTask = new DownloadTask();

        // Retrieves the associated beer
        dlTask.execute("GET", "http://binouze.fabrigli.fr/bieres/5.json");

        try {
            webContent = dlTask.get();
        }
        catch(InterruptedException | ExecutionException e) {}

        // Fills the JSON Object
        try {
            webJson = new JSONObject(webContent);
        }
        catch(JSONException e){
            // Nothing
        }

        // Retrieves the info
        try {
            nameString = (String) webJson.get("name");
            descriptionString = (String) webJson.get("description");
            drinkerString = (String) webJson.get("buveur");
            originJson = (JSONObject) webJson.get("country");
            originString = (String) originJson.get("name");

        }
        catch(JSONException e) {System.out.println(e);}

        // Fills the TextViews
        name.setText(nameString);
        description.setText("Description : " + descriptionString);
        origin.setText("Origine : " + originString);
        drinker.setText("Buveur : " + drinkerString);

    }

    // Method called once the button has been pressed
    private void bookmarkClick() {

        // If the beer is bookmarked
        if (isBookmarked) {

            // Changes the bookmark value
            isBookmarked = false;

            // Changes the background image
            bookmarkButton.setBackgroundResource(R.drawable.empty_star);

            // Removes the bookmark in the database
            // TODO


        }

        // Else, the beer is not bookmarked
        else {

            // Changes the bookmark value
            isBookmarked = true;

            // Changes the background image
            bookmarkButton.setBackgroundResource(R.drawable.filled_star);

            // Adds the bookmark in the database
            // TODO

        }

    }

}