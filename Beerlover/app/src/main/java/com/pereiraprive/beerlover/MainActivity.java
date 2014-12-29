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

    // Needed variables for this view
    private ListView fiveLastBeers;
    private DownloadTask dlTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content of the main activity
        setContentView(R.layout.activity_main);

        // Retrieves the ListView
        fiveLastBeers = (ListView) findViewById(R.id.fiveLastBeers);

        // Fills the 5 last discoveries
        fillView();
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
    private void fillView() {

        // Local objects
        String webContent = "Toto", stringJson;
        DownloadTask dlTask;
        JSONObject webJson;
        ArrayAdapter<String> randomAdapter;
        ArrayList<String> randomList;

        // Create a DownloadTask & downloads content
        dlTask = new DownloadTask();
        dlTask.execute("GET", "http://binouze.fabrigli.fr/countries.json");

        // Retrieves the content
        try {
            webContent = dlTask.get();
        }
        catch(InterruptedException | ExecutionException e) {}

        // Creates the JSON Object
        stringJson = new String();

        // Fills the JSON Object
        try {
            webJson = new JSONObject(webContent);
        }
        catch(JSONException e){
            // Nothing
        }

        // Parse the JSON
        randomList = new ArrayList<String>();
        randomList.add("Truc");
        randomList.add("Machin");
        randomList.add("Chose");
        randomList.add("Biniou");

        randomAdapter = new ArrayAdapter<String>(this, R.layout.random_list, randomList);

        fiveLastBeers.setAdapter(randomAdapter);

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