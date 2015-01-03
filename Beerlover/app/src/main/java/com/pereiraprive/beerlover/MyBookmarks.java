package com.pereiraprive.beerlover;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MyBookmarks extends ActionBarActivity {

    // Class variables
    private String userToken = null;
    private int userID;
    private boolean isUserAuth = false;
    private ArrayList<Integer> bookmarksListID = new ArrayList<Integer>();
    private ArrayList<String> bookmarksListName = new ArrayList<String>();
    private ListView allBookmarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content of the main activity
        setContentView(R.layout.bookmarks);

        // Retrieves the ListView & User bookmarks
        allBookmarks = (ListView) findViewById(R.id.allBookmarks);

    }

    // Method called once the user sees this view
    @Override
    protected void onResume() {
        super.onResume();

        // Auths the user
        if(!isUserAuth)
            authUser();

        // Empties the ArrayList
        bookmarksListID.clear();
        bookmarksListName.clear();

        // Fills the view
        fillView();

    }

    // Method to fill the view with the bookmarks
    private void fillView() {

        // Local objects
        DownloadTask dlTask;
        String webContent = new String();
        JSONObject jsonObject = null, tmpJson = null;
        JSONArray jsonArray = null;
        ArrayAdapter<String> bookmarksAdapter;
        boolean alreadyInList = false;

        // Retrieves the bookmarked beers
        dlTask = new DownloadTask();
        dlTask.execute("GET", "http://binouze.fabrigli.fr/notes.json?user[id=" + userID +"&user[token=" + userToken, "TXT");
        try {
            webContent = dlTask.get();
        }
        catch(InterruptedException | ExecutionException e) {}

        // Parse the favs
        try {
            jsonArray = new JSONArray(webContent);

            for(int i = 0 ; i < jsonArray.length() ; i++) {

                // Takes the next JSON Object
                jsonObject = jsonArray.getJSONObject(i);

                // Goes through the actual ArrayList looking for duplicates
                for(int j = 0 ; j < bookmarksListID.size() ; j++) {

                    // If the beer is already in the list...
                    if(bookmarksListID.get(j) == jsonObject.getInt("biere_id")) {

                        // ...sets the beer as already in the list...
                        alreadyInList = true;

                        // ...and removes it if the bookmark has been removed
                        if(jsonObject.getInt("value") == 0)
                            bookmarksListID.remove(j);

                    }
                }

                // Else, it is not already in the list and bookmarked
                if(!alreadyInList && jsonObject.getInt("value") == 5)
                    bookmarksListID.add(jsonObject.getInt("biere_id"));

                // Reset the boolean to false
                alreadyInList = false;
            }
        }
        catch(JSONException e) {}

        // Then retrieves the name of the favs
        for(int i = 0 ; i < bookmarksListID.size() ; i++) {

            // Creates a new download task & retrieves the associated beer
            dlTask = new DownloadTask();
            dlTask.execute("GET", "http://binouze.fabrigli.fr/bieres/" + bookmarksListID.get(i) +".json", "TXT");

            try {
                webContent = dlTask.get();
            }
            catch(InterruptedException | ExecutionException e) {}

            // Fills the JSON Object & adds the beer name to the ArrayList
            try {
                jsonObject = new JSONObject(webContent);
                bookmarksListName.add((String) jsonObject.get("name"));
            }
            catch(JSONException e) {}

        }

        // Finally, sets the List in the ListView
        bookmarksAdapter = new ArrayAdapter<String>(this, R.layout.simple_list, bookmarksListName);
        allBookmarks.setAdapter(bookmarksAdapter);

        // And adds a click listener
        allBookmarks.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            // Method called once the bookmark button has been pressed
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                describeBeer(position);
            }
        });

    }

    // Method called when the users clicks on a Bookmarked Beer
    private void describeBeer(int itemClicked) {

        // Create a new intent & passes the beer ID
        Intent intent = new Intent(getApplicationContext(),BeerDescription.class);
        intent.putExtra("beerID", bookmarksListID.get(itemClicked));

        // Starts the activity
        startActivity(intent);

    }

    // Method to authenticate the user
    private void authUser() {

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

        // Test if the file exists. If not, launch the UserAuth activity
        if(!fileExists) {

            // Launch the UserAuth activity
            Intent i = new Intent(getApplicationContext(), UserAuth.class);
            startActivity(i);
            finish();

        }

        else {

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
}
