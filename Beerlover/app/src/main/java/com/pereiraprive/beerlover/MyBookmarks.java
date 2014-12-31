package com.pereiraprive.beerlover;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class MyBookmarks extends ActionBarActivity {

    // Class variables
    private String userToken = null;
    private int userID;
    private boolean isUserAuth = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content of the main activity
        setContentView(R.layout.bookmarks);

    }

    // Method called once the user sees this view
    @Override
    protected void onResume() {
        super.onResume();

        // Auths the user
        if(!isUserAuth)
            authUser();

        // Fills the view
        fillView();

    }

    // Method to fill the view with the bookmarks
    private void fillView() {

        // Local objects
        ListView allBookmarks;
        DownloadTask dlTask;
        String webContent = new String();
        JSONObject bookmarksJson = null, tmpJson = null;

        // Retrieves the ListView & User bookmarks
        allBookmarks = (ListView) findViewById(R.id.allBookmarks);

        // Retrieves the bookmarked beers
        dlTask = new DownloadTask();
        dlTask.execute("GET", "http://binouze.fabrigli.fr/notes.json?user[id=" + userID +"&user[token=" + userToken, "TXT");
        try {
            webContent = dlTask.get();
        }
        catch(InterruptedException | ExecutionException e) {}

        // Parse the favs
        try {
            bookmarksJson = new JSONObject(webContent);

            // TODO : Parsing du JSON

        }
        catch(JSONException e) {}

        System.out.println(webContent);

    }

    // Method to authenticate the user
    public void authUser() {

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
