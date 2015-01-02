package com.pereiraprive.beerlover;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class BeerList extends ActionBarActivity {

    // Needed objects for this view
    private String currentSort = "categories";
    private ArrayList<Integer> beerID, beerTypeID, beerOriginID, categoryID;
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
        DownloadTask dlTask = new DownloadTask();
        JSONObject jsonObject = null;
        JSONArray jsonArray = null;
        String webContent = new String();
        ExpandableListAdapter expandableListAdapter;
        List<String> viewParents;
        List<ArrayList<String>> parentsList = null;
        ArrayList<String> tmpList;
        HashMap<String, List<String>> viewChildren;
        int tmpId;

        // Creates a new categoryID List
        categoryID = new ArrayList<Integer>();

        // Retrieves the ExpendableListView view
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);

        // Downloads the beers to fill the Expandable Listview
        dlTask.execute("GET", "http://binouze.fabrigli.fr/" + currentSort + ".json", "TXT");

        try {
            webContent = dlTask.get();
        }
        catch(InterruptedException | ExecutionException e) {}

        try {
            jsonArray = new JSONArray(webContent);
        }
        catch (JSONException e){}

        // Creates the parents and children lists
        parentsList = new ArrayList<>();
        viewParents = new ArrayList<>();
        viewChildren = new HashMap<>();

        // Fills the parents thanks to the previously downloaded JSON
        try {

            for(int i = 0 ; i < jsonArray.length() ; i++) {

                // Takes the next JSON Object
                jsonObject = jsonArray.getJSONObject(i);

                // Retrieves its name & ID ; and puts it in the list
                viewParents.add(jsonObject.getString("name"));
                categoryID.add(jsonObject.getInt("id"));

                // Creates a new List corresponding to the parent & adds it to the List of List
                tmpList = new ArrayList<String>();
                parentsList.add(tmpList);

            }

        }

        catch (JSONException e) {}

        // Fills the children thanks to the global JSON
        for(int i = 0 ; i < categoryID.size() ; i++) {

            // Gets the id of the category
            tmpId = categoryID.get(i);

            // Gets the parents-associated list
            tmpList = parentsList.get(i);

            // Retrieves all the beers from that category
            for(int j = 0 ; j < beerID.size() ; j++) {

                // If the given beer belongs to the category, adds it in the list
                if(beerID.get(j) == tmpId) {
                    tmpList.add(beerName.get(j));
                }
            }
        }

        // Finally, populates the HashMap
        for(int i = 0 ; i < viewParents.size() ; i++)
            viewChildren.put(viewParents.get(i), parentsList.get(i));


        // And now, sets the ListAdapter
        expandableListAdapter = new CustomExpListAdapter(this, viewParents, viewChildren);
        expandableListView.setAdapter(expandableListAdapter);

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
        String jsonString = new String();

        // Creates the new beer objects
        beerID = new ArrayList<Integer>();
        beerTypeID = new ArrayList<Integer>();
        beerOriginID = new ArrayList<Integer>();
        beerName = new ArrayList<String>();

        // Creates a new download task with the appropriate random value
        dlTask = new DownloadTask();

        // Retrieves all the beers
        dlTask.execute("GET", "http://binouze.fabrigli.fr/bieres.json", "BTXT");

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

}