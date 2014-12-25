package com.pereiraprive.beerlover;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import android.content.Intent;

public class MainActivity extends ActionBarActivity {

    // Needed variables for this view
    private ListView fiveLastBeers;

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

        // Creates a toast to test
        Toast.makeText(getApplicationContext(), "THE GAME", Toast.LENGTH_SHORT).show();
    }

    // Method top fill the 5 last beer discoveries
    private void fillView() {

        // TODO
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
