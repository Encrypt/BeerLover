package com.pereiraprive.beerlover;


import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import java.util.concurrent.ExecutionException;


public class UserAuth extends ActionBarActivity {

    private EditText email, nickname;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the "beer_list" content
        setContentView(R.layout.user_auth);

        // Retrieves the objects
        email = (EditText) findViewById(R.id.email);
        nickname = (EditText) findViewById(R.id.nickname);

    }

    // Method called once the "send" button has been pressed
    public void Send(View v) {

        // Local objects
        String myEmail, pseudo, content;
        JSONObject json;

        myEmail = email.getText().toString();
        pseudo = nickname.getText().toString();

        try {
            json = ConvertToJson(myEmail, pseudo);
            content = json.toString();
            PostUserToken(content);
        }
        catch (JSONException e){}

        // Ends the view
        finish();
    }

    // Method to create a JSON
    public JSONObject ConvertToJson (String email, String nickname) throws JSONException{

        JSONObject json = new JSONObject();
        JSONObject userJson = new JSONObject();
        userJson.put("nickname", nickname);
        userJson.put("email", email);
        json.put("user",userJson);

        System.out.println(json);

        return json;
    }

    // Gets the token generated by the server
    public void PostUserToken(String content) {

        String token = null;

        // Create a DownloadTask
        DownloadTask dlTask = new DownloadTask();

        // Downloads the content we want & gets the result
        dlTask.execute("POST", "http://binouze.fabrigli.fr/users.json", "JSON", content);

        // Retrieves the result
        try {
            token = dlTask.get();
            System.out.println(token);
            SaveJsonInMemory(token);

        } catch (InterruptedException | ExecutionException e) {}

        System.out.println("Token : " + token);
    }

    // Saves the token in the phone memory
    private void SaveJsonInMemory(String stringJson) {

        // Saves the token in memory
        try {
            FileOutputStream fOut = openFileOutput("BeerloverToken.txt", MODE_PRIVATE);
            fOut.write(stringJson.getBytes());
            fOut.close();
        }
        catch(FileNotFoundException e) {}
        catch (IOException e) {}

        // Shows to the user everything is all right
        Toast.makeText(getBaseContext(),"Compte bien renseigné !", Toast.LENGTH_SHORT).show();

    }

}
