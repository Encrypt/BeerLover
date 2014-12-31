package com.pereiraprive.beerlover;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class BeerList extends ActionBarActivity {

    // Needed variables for this view
    private String currentSort = "Type";
    private ExpandableListView expandableListView;

    // Method called once the Activity is launched
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // Set the "beer_list" content
        setContentView(R.layout.beer_list);
        // Retrieves the ExpendableListView view
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_beerlist, menu);
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

    public JSONObject DownloadJsonType (String type){


        DownloadTask dltask = new DownloadTask();
        JSONObject categorie_json=null;
        String cat=null;

        // download the files to fill the Expandable listview
        dltask.execute("GET", "http://binouze.fabrigli.fr/" +type+ ".json" ,"text");
        try {

            cat = dltask.get();
        }
        catch(InterruptedException | ExecutionException e) {}

        try{
            categorie_json = new JSONObject(cat);
        }
        catch (JSONException e){}

        return categorie_json;
    }
}