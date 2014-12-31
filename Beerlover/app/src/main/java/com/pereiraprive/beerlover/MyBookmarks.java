package com.pereiraprive.beerlover;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MyBookmarks extends ActionBarActivity {

    // Class variables
    private String userToken = null;
    private boolean isUserAuth = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content of the main activity
        setContentView(R.layout.activity_main);
        // TODO : Créer un style

    }

    // Method called once the user sees this view
    protected void OnResume() {

        // Auths the user
        if(!isUserAuth)
            authUser();

    }

    // Method to authenticate the user
    public void authUser() {

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

        }

        else {

            // Changes the value of the isUserAuth boolean
            isUserAuth = true;

            // Retrieves its token
            userToken = "";
            int c;

            try {
                while ((c = saveFile.read()) != -1) {
                    userToken = userToken + Character.toString((char) c);
                }
            } catch (IOException e) {}
        }
    }
}
