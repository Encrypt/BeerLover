package com.pereiraprive.beerlover;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.content.Intent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content of the main activity
        setContentView(R.layout.activity_main);

        // Fills the random list
        fillRandomList();
    }

    // Method to go to the list of beers
    public void displayBeerList(View v) {

        // Displays the list of the beers
        Intent i = new Intent(getApplicationContext(), BeerList.class);
        startActivity(i);
    }

    // Method to go to the bookmarked beers
    public void displayBookmarks(View v){


    }

    // Method top fill the 5 last beer discoveries
    private void fillRandomList() {

        // Local objects
        int randomNumber, j;
        int randomNumbers[] = new int[5];
        Boolean alreadyExists = false;
        String webContent = null;
        JSONObject webJson = null;
        DownloadTask dlTask;
        ArrayAdapter<String> randomAdapter;
        ArrayList<String> randomList = new ArrayList<String>();
        ListView fiveRandomBeers = (ListView) findViewById(R.id.fiveRandomBeers);
        String randomBeerName = new String();

        // Generates random numbers
        for(int i = 1; i <= 5 ; i++) {

            // Reset boolean
            alreadyExists = true;

            while(alreadyExists) {

            }
            // Generates a random number
            randomNumber = 1 + (int)(Math.random() * (151 - 1));
        }

        // Populates the ListView
        for(int i = 1 ; i <= 5 ; i++) {

            // Creates a new download task
            dlTask = new DownloadTask();

            // Retrieves the associated beer
            dlTask.execute("GET", "http://binouze.fabrigli.fr/bieres/" + randomNumber +".json");

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

            // Retrieves the beer name
            try {
                randomBeerName = (String) webJson.get("name");
            }
            catch(JSONException e) {}

            // Adds the beer to the list
            randomList.add(randomBeerName);

        }

        // Finally, sets the List in the ListView
        randomAdapter = new ArrayAdapter<String>(this, R.layout.random_list, randomList);
        fiveRandomBeers.setAdapter(randomAdapter);

    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/
}