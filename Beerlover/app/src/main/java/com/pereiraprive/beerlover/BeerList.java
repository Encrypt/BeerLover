package com.pereiraprive.beerlover;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class BeerList extends ActionBarActivity {

    // Needed objects for this view
    private String currentSort = "Type";
    private ArrayList<Integer> beerID, beerTypeID, beerOriginID;
    private ArrayList<String> beerName;
    private ExpandableListView expandableListView;

    // Method called once the Activity is launched
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Immediately starts downloading the complete JSON
        downloadAllBeers();

        // Set the "beer_list" content
        setContentView(R.layout.beer_list);

        // Fill the ExpendableView
        fillView();

    }

    // Method to fill the expendable View
    private void fillView() {

        // Local objects
        JSONObject jsonCategories = null;

        // Retrieves the ExpendableListView view
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);

        // Retrieves the categories
        jsonCategories = DownloadCategory();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_beerlist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Gets the id
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.filter) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Downloads the complete JSON and puts it in the variable
    private void downloadAllBeers() {

        // Local objects
        DownloadTask dlTask;
        JSONArray jsonArray;
        JSONObject jsonObject, jsonOrigin;
        String jsonString = null;

        // Creates a new download task with the appropriate random value
        dlTask = new DownloadTask();

        // Retrieves all the beers
        dlTask.execute("GET", "http://binouze.fabrigli.fr/bieres/bieres.json", "TXT");

        try {
            jsonString = dlTask.get();
        }
        catch(InterruptedException | ExecutionException e) {}

        // Parse the beers
        try {

            jsonArray = new JSONArray(jsonString);

            for(int i = 0 ; i < jsonArray.length() ; i++) {

                // Takes the next JSON Object
                jsonObject = jsonArray.getJSONObject(i);

                // Retrieves the name, category and ID of the beer to put it in the ArrayLists
                beerID.add(jsonObject.getInt("biere_id"));
                beerTypeID.add(jsonObject.getInt("category_id"));
                beerName.add(jsonObject.getString("name"));

                // Retrieves the ID of the origin
                jsonOrigin = jsonObject.getJSONObject("country");
                beerOriginID.add(jsonOrigin.getInt("id"));

            }
        }

        catch(JSONException e) {}

    }

    // Method to download the JSON associated to the category
    private JSONObject DownloadCategory() {

        DownloadTask dlTask = new DownloadTask();
        JSONObject jsonCategory = null;
        String webContent = null;

        // Downloads the beers to fill the Expandable Listview
        dlTask.execute("GET", "http://binouze.fabrigli.fr/" + currentSort + ".json" ,"text");

        try {

            webContent = dlTask.get();
        }
        catch(InterruptedException | ExecutionException e) {}

        try{
            jsonCategory = new JSONObject(webContent);
        }
        catch (JSONException e){}

        return jsonCategory;
    }
}