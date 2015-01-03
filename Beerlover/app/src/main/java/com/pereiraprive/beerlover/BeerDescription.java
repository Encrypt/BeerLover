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

import org.json.JSONArray;
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
    private int beerID, userID;
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

        // Sets the bookmark if the beer has bee bookmarked
        setBookmark();

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
        dlTask.execute("GET", "http://binouze.fabrigli.fr/bieres/" + beerID + ".json", "TXT");

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
        dlTask.execute("GET", "http://binouze.fabrigli.fr" + pictureString, "IMG");

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

    // Sets the bookmark if the user is identified
    private void setBookmark() {

        System.out.println("setBookmark ! / isUserAuth = " + isUserAuth);
        tryToAuthUser();

        // If the user is auth, does the job
        if(isUserAuth) {

            // Local objects
            JSONArray jsonArray;
            JSONObject jsonObject;
            DownloadTask dlTask;
            String webContent = new String();
            int bookmarkValueTmp = 0;

            // Retrieves the bookmarked beers
            dlTask = new DownloadTask();
            dlTask.execute("GET", "http://binouze.fabrigli.fr/notes.json?user[id=" + userID +"&user[token=" + userToken, "TXT");
            try {
                webContent = dlTask.get();
            }
            catch(InterruptedException | ExecutionException e) {}

            System.out.println(webContent);

            // Tries to find the current beer
            try {

                jsonArray = new JSONArray(webContent);

                for(int i = 0 ; i < jsonArray.length() ; i++) {

                    // Takes the next JSON Object
                    jsonObject = jsonArray.getJSONObject(i);

                    // If it's the current beer, saves the bookmark value
                    if(jsonObject.getInt("biere_id") == beerID)
                        bookmarkValueTmp = jsonObject.getInt("biere_id");

                }

            }
            catch(JSONException e) {}

            // Finally, sets the bookmark
            if(bookmarkValueTmp == 0) {
                isBookmarked = false;
                bookmarkButton.setBackgroundResource(R.drawable.empty_star);
            }

            else {
                isBookmarked = true;
                bookmarkButton.setBackgroundResource(R.drawable.filled_star);
            }

        }

    }

    // Method to try to auth the user
    private void tryToAuthUser() {

        // Needed objects
        JSONObject tokenJson = null;
        String tmpString;

        // Retrieves the file
        FileInputStream saveFile = null;
        boolean fileExists = true;

        try {
            saveFile = openFileInput("BeerloverToken.txt");
        }
        catch(FileNotFoundException e){
            fileExists = false;
        }

        // Test if the file exists.
        if(fileExists) {

            // Changes the value of the isUserAuth boolean
            isUserAuth = true;

            // Retrieves the saved data
            tmpString = "";
            int c;

            try {
                while ((c = saveFile.read()) != -1) {
                    tmpString = tmpString + Character.toString((char) c);
                }
            }
            catch (IOException e) {}

            // Retrieves the ID and token from the JSON
            try {
                tokenJson = new JSONObject(tmpString);
                userID = tokenJson.getInt("id");
                userToken = tokenJson.getString("token");
            }
            catch(JSONException e) {}

        }

    }

    // Method called once the button has been pressed
    private void bookmarkClick() {

        // Local objects
        DownloadTask dlTask = new DownloadTask();
        String content;
        JSONObject json;

        // Checks if the user has an account on the server
        if(!isUserAuth) {

            // Launches the UserAuth activity
            Intent i = new Intent(getApplicationContext(), UserAuth.class);
            startActivity(i);

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
                    json = setMarkAs(0);
                    content = json.toString();
                    dlTask.execute("POST", "http://binouze.fabrigli.fr/notes.json", "JSON", content);
                    Toast.makeText(getBaseContext(),"Favori bien supprimé !", Toast.LENGTH_SHORT).show();
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
                    json = setMarkAs(5);
                    content = json.toString();
                    dlTask.execute("POST", "http://binouze.fabrigli.fr/notes.json", "JSON", content);
                    Toast.makeText(getBaseContext(),"Favori bien ajouté !", Toast.LENGTH_SHORT).show();
                }
                catch (JSONException e) {}
            }

        }

    }

    // Converts the mark to a JSON object
    public JSONObject setMarkAs(int value) throws JSONException {

        JSONObject json = new JSONObject();
        JSONObject userJson = new JSONObject();
        JSONObject noteJson = new JSONObject();
        noteJson.put("biere_id", beerID);
        noteJson.put("value", value);
        userJson.put("id", userID);
        userJson.put("token", userToken);
        json.put("note", noteJson);
        json.put("user", userJson);
        return json;
    }

}