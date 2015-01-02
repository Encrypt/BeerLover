package com.pereiraprive.beerlover;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
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
    private List<String> viewParents;
    private HashMap<String, List<String>> viewChildren;
    private CustomExpListAdapter expandableListAdapter;

    // Method called once the Activity is launched
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the "beer_list" content
        setContentView(R.layout.beer_list);

        new LoadingBeerList();

        // Immediately starts downloading the complete JSON
        downloadAllBeers();

        // Retrieves the ExpendableListView view
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);

        // Fill the ExpendableView
        fillView();

        // ListView on child click listener
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                // Returns the beer description
                describeBeer(viewChildren.get(viewParents.get(groupPosition)).get(childPosition));

                return false;
            }
        });
    }

    // Creates the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_beerlist, menu);
        return true;
    }

    // Called once a button has been clicked in the ActionBar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Gets the id
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.filter) {
            return true;
        }

        else {

            // Sets the appropriate sorting type
            if (id == R.id.sortType)
                currentSort = "categories";

            if (id == R.id.sortCountry)
                currentSort = "countries";

            // Notifies the Adapter to refresh itself
            expandableListView.setAdapter(new CustomExpListAdapter(this, new ArrayList<String>(), new HashMap<String, List<String>>()));

            // Re-completes the view
            fillView();

        }

        return super.onOptionsItemSelected(item);
    }

    // Method to display a beer
    private void describeBeer(String clickedBeer) {

        // Goes through the list of beers
        int i = 0;
        while(!beerName.get(i).equals(clickedBeer))
            i++;

        // Then retrieves its ID
        int clickedBeerId = beerID.get(i);

        // Create a new intent & passes the beer ID
        Intent intent = new Intent(getApplicationContext(),BeerDescription.class);
        intent.putExtra("beerID", clickedBeerId);

        // Starts the activity
        startActivity(intent);

    }

    // Method to fill the expendable View
    private void fillView() {

        // Local objects
        DownloadTask dlTask = new DownloadTask();
        JSONObject jsonObject = null;
        JSONArray jsonArray = null;
        String webContent = new String();
        List<ArrayList<String>> parentsList = null;
        ArrayList<String> tmpList;
        int tmpId;

        // Creates a new categoryID List
        categoryID = new ArrayList<Integer>();

        // Downloads the beers category to fill the Expandable ListView
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

        // Adds the "Uncategorized" category
        viewParents.add("Non classé");
        categoryID.add(-1);
        tmpList = new ArrayList<String>();
        parentsList.add(tmpList);

        // Retrieves the kind of filter to apply
        ArrayList<Integer> filterApplied;
        if(currentSort.equals("categories"))
            filterApplied = beerTypeID;
        else
            filterApplied = beerOriginID;

        // Fills the children thanks to the global JSON
        for(int i = 0 ; i < categoryID.size() ; i++) {

            // Gets the id of the category
            tmpId = categoryID.get(i);

            // Gets the parents-associated list
            tmpList = parentsList.get(i);

            // Retrieves all the beers from that category
            for(int j = 0 ; j < beerID.size() ; j++) {

                // System.out.println("Valeur de beerID : " + beerID.get(j) + " / Valeur de tmpId : " + tmpId);

                // If the given beer belongs to the category, adds it in the list (depends on the category)
                if(filterApplied.get(j) == tmpId)
                    tmpList.add(beerName.get(j));

            }
        }

        // Finally, populates the HashMap
        for(int i = 0 ; i < viewParents.size() ; i++)
            viewChildren.put(viewParents.get(i), parentsList.get(i));


        // And now, sets the ListAdapter
        expandableListAdapter = new CustomExpListAdapter(this, viewParents, viewChildren);
        expandableListView.setAdapter(expandableListAdapter);

    }

    // Downloads the complete JSON and puts it in the variable
    private void downloadAllBeers() {

        // Local objects
        DownloadTask dlTask;
        JSONArray jsonArray = null;
        JSONObject jsonObject = null, jsonOrigin;
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

        // Retrieves the JSONArray
        try {
            jsonArray = new JSONArray(jsonString);
        }
        catch(JSONException e) {}

        // Pareses the beers
        for(int i = 0 ; i < jsonArray.length() ; i++) {

            // Takes the next JSON Object
            try {
                jsonObject = jsonArray.getJSONObject(i);
            }
            catch (JSONException e) {}

            // Fills the Array
            int beerIdTmp, beerTypeIdTmp, beerOriginTmp;
            String beerNameTmp;

            // Retrieves the name, category and ID of the beer to put it in the ArrayLists
            try {
                beerIdTmp = jsonObject.getInt("id");
            }
            catch(JSONException e) {
                beerIdTmp = -1;
            }

            try {
                beerTypeIdTmp = jsonObject.getInt("category_id");
            }
            catch(JSONException e) {
                beerTypeIdTmp = -1;
            }

            try {
                beerNameTmp = jsonObject.getString("name");
            }
            catch(JSONException e) {
                beerNameTmp = "Sans nom";
            }

            try {
                jsonOrigin = jsonObject.getJSONObject("country");
                beerOriginTmp = jsonOrigin.getInt("id");
            }
            catch(JSONException e) {
                beerOriginTmp = -1;
            }

            // Copies it in the ArrayLists
            beerID.add(beerIdTmp);
            beerTypeID.add(beerTypeIdTmp);
            beerName.add(beerNameTmp);
            beerOriginID.add(beerOriginTmp);

        }

    }

}