package com.pereiraprive.beerlover;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class BeerDescription extends ActionBarActivity {

    // Needed variables for this view
    private boolean isBookmarked = false, isUserAuth = false;
    private TextView name, description, origin, drinker;
    private ImageButton bookmarkButton;
    private ImageView beerImage;
    private int beerID;
    private String userToken;

    // Method called once the Activity is launched
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieves the beer ID
        Bundle bundle = getIntent().getExtras();
        beerID = bundle.getInt("beerID");

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
        JSONObject webJson = null, tmpJson = null;
        DownloadTask dlTask;
        String nameString = new String();
        String descriptionString = new String();
        String originString = new String();
        String drinkerString = new String();
        String pictureString = new String();
        Bitmap pictureBitmap = null;

        // Creates a new download task with the appropriate random value
        dlTask = new DownloadTask();

        // Retrieves the associated beer
        dlTask.execute("GET", "http://binouze.fabrigli.fr/bieres/" + beerID + ".json");

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

            // Easily accessible info
            nameString = (String) webJson.get("name");
            descriptionString = (String) webJson.get("description");
            drinkerString = (String) webJson.get("buveur");

            // Country and picture
            tmpJson = (JSONObject) webJson.get("country");
            originString = (String) tmpJson.get("name");

            tmpJson = (JSONObject) webJson.get("image");
            tmpJson = (JSONObject) tmpJson.get("image");
            tmpJson = (JSONObject) tmpJson.get("thumb");
            pictureString = (String) tmpJson.get("url");

        }
        catch(JSONException e) {}

        // Gets the picture
        dlTask = new DownloadTask();
        dlTask.execute("GET", "http://binouze.fabrigli.fr" + pictureString);

        try {
            webContent = dlTask.get();
        }
        catch(InterruptedException | ExecutionException e) {}

        // Decodes the picture
        try{
            byte[] encodeByte=Base64.decode(webContent, Base64.DEFAULT);
            pictureBitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

        }catch(Exception e){}

        // Fills the TextViews & picture
        name.setText(nameString);
        description.setText("Description : " + descriptionString);
        origin.setText("Origine : " + originString);
        drinker.setText("Buveur : " + drinkerString);
        beerImage.setImageBitmap(pictureBitmap);

    }

    // Method called once the button has been pressed
    private void bookmarkClick() {


        DownloadTask dltask = new DownloadTask();
        String content;
        JSONObject json;


        // Checks if the user has an account on the server
        if(!isUserAuth) {

            // Retrieves the file
            FileInputStream saveFile = null;
            boolean fileExists = true;

            try {
                saveFile = openFileInput("BeerloverToken.txt");
            }
            catch(FileNotFoundException e){
                fileExists = false;
            }

            // Test if the file exists. If not, launch the UserAuth activity
            if(!fileExists) {

                // Launch the UserAuth activity
                Intent i = new Intent(getApplicationContext(), UserAuth.class);
                startActivity(i);

            }

            else {

                // Changes the value of the isUserAuth boolean
                isUserAuth = true;

                // Retrieves its token
                userToken = "";
                int c;

                try {
                    while ((c = saveFile.read()) != -1) {
                        userToken = userToken + Character.toString((char) c);
                    }
                }
                catch (IOException e) {}

                // Calls the bookmarkClick method again
                bookmarkClick();

            }

        }

        else {

            // If the beer is bookmarked
            if (isBookmarked) {

                // Changes the bookmark value
                isBookmarked = false;

                // Changes the background image
                bookmarkButton.setBackgroundResource(R.drawable.empty_star);

                // Removes the bookmark in the database

                try {
                    json = ConvertToJson(0);
                    content = json.toString();
                    dltask.execute("POST", "http://binouze.fabrigli.fr/notes.json",content);
                    Toast.makeText(getBaseContext(),"La bière a été retiré des favoris", Toast.LENGTH_SHORT).show();
                }
                catch (JSONException e){}

            }

            // Else, the beer is not bookmarked
            else {

                // Changes the bookmark value
                isBookmarked = true;

                // Changes the background image
                bookmarkButton.setBackgroundResource(R.drawable.filled_star);

                // Adds the bookmark in the database

                try {
                    json = ConvertToJson(5);
                    content = json.toString();
                    dltask.execute("POST", "http://binouze.fabrigli.fr/notes.json",content);
                    Toast.makeText(getBaseContext(),"La bière a été ajouté aux favoris", Toast.LENGTH_SHORT).show();
                }
                catch (JSONException e) {}
            }

        }

    }

    // Converts the mark to a JSON object
    public JSONObject ConvertToJson (int value) throws JSONException {

        JSONObject json = new JSONObject();
        JSONObject userJson = new JSONObject();
        JSONObject noteJson = new JSONObject();
        noteJson.put("biere_id", beerID);
        noteJson.put("value", value);
        userJson.put("id", 1);
        userJson.put("token", userToken);
        json.put("user", userJson);
        json.put("note",noteJson);
        return json;
    }

}