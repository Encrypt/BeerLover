package com.pereiraprive.beerlover;

import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.content.Intent;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends ActionBarActivity {

    // Class objects
    int randomNumbers[] = new int[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content of the main activity
        setContentView(R.layout.activity_main);

        // Fills the random list
        fillRandomList();
        Button buttonOpenDialog = (Button)findViewById(R.id.about);
        buttonOpenDialog.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                OpenDialog();
            }
        });

    }

    void OpenDialog(){
        CreditDialogFragment myDialogFragment = CreditDialogFragment.newInstance();
        myDialogFragment.show(getFragmentManager(), "Credits");
    }

    public void cancelClicked() {
        Toast.makeText(MainActivity.this,
                "Cancel Clicked!",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Gets the item clicked ID
        int id = item.getItemId();

        // Calls the about method
        if (id == R.id.about) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Method to go to the list of beers
    public void displayBeerList(View v) {

        // Displays the list of the beers
        Intent i = new Intent(getApplicationContext(), BeerList.class);
        startActivity(i);
    }

    // Method to go to the bookmarked beers
    public void displayBookmarks(View v){

        // Displays the list of the beers
        Intent i = new Intent(getApplicationContext(), MyBookmarks.class);
        startActivity(i);

    }

    // Method called when the users presses the "about" button
    public void about() {


    }

    // Method top fill the 5 last beer discoveries
    private void fillRandomList() {

        // Local objects
        int randomNumber;
        Boolean alreadyExists;
        String webContent = null;
        JSONObject webJson = null;
        DownloadTask dlTask;
        ArrayAdapter<String> randomAdapter;
        ArrayList<String> randomList = new ArrayList<String>();
        ListView fiveRandomBeers = (ListView) findViewById(R.id.fiveRandomBeers);
        String randomBeerName = new String();

        // Generates random numbers (o avoid duplicates)
        for(int i = 0; i < 5 ; i++) {

            // Reset the boolean
            alreadyExists = false;

            do {

                // Generates a random number
                randomNumber = 1 + (int)(Math.random() * (151 - 1));

                // Checks if it already exists in the list
                for(int j = 0 ; j <= i ; j++) {

                    if(randomNumbers[j] == randomNumber)
                        alreadyExists = true;

                }

            } while(alreadyExists);

            // Else, adds the number in the list
            randomNumbers[i] = randomNumber;

        }

        // Populates the ListView
        for(int i = 0 ; i < 5 ; i++) {

            // Creates a new download task with the appropriate random value
            dlTask = new DownloadTask();
            randomNumber = randomNumbers[i];

            // Retrieves the associated beer
            dlTask.execute("GET", "http://binouze.fabrigli.fr/bieres/" + randomNumber +".json", "TXT");

            try {
                webContent = dlTask.get();
            }
            catch(InterruptedException | ExecutionException e) {}

            // Fills the JSON Object & retrieves the beer name
            try {
                webJson = new JSONObject(webContent);
                randomBeerName = (String) webJson.get("name");
            }
            catch(JSONException e){
                // Nothing
            }

            // Adds the beer to the list
            randomList.add(randomBeerName);

        }

        // Finally, sets the List in the ListView
        randomAdapter = new ArrayAdapter<String>(this, R.layout.simple_list, randomList);
        fiveRandomBeers.setAdapter(randomAdapter);

        // And adds a Click Listener
        fiveRandomBeers.setOnItemClickListener(new OnItemClickListener() {

            // Method called once the bookmark button has been pressed
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                describeBeer(position);
            }
        });

    }

    // Method to display the description of a random beer
    private void describeBeer(int itemClicked) {

        // Create a new intent & passes the beer ID
        Intent intent = new Intent(getApplicationContext(),BeerDescription.class);
        intent.putExtra("beerID", randomNumbers[itemClicked]);

        // Starts the activity
        startActivity(intent);

    }


}