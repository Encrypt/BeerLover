package com.pereiraprive.beerlover;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;


public class UserAuth extends ActionBarActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the "beer_list" content
        setContentView(R.layout.beer_description);

        // Retrieves the objects


        // Adds a Listener to the star


        // Fills the objects (beer name, description, origin...)

    }

    public void PostUserToken(View v) {

        String webContent = "Toto";
        String token = null;
        // Create a DownloadTask
        DownloadTask dltask = new DownloadTask();

        // Downloads the content we want & gets the result
        dltask.execute("POST", "http://binouze.fabrigli.fr/user.json");

        // Retrieves the result
        try {
            webContent = dltask.get();
        } catch (InterruptedException e) {

        } catch (ExecutionException e) {

        }
    }
}
