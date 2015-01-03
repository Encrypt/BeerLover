package com.pereiraprive.beerlover;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.concurrent.ExecutionException;

public class UserAuth extends ActionBarActivity {

    private EditText email, nickname;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Sets the "beer_list" content
        setContentView(R.layout.user_auth);

        // Retrieves the objects
        email = (EditText) findViewById(R.id.email);
        nickname = (EditText) findViewById(R.id.nickname);

    }

    // Method called once the "send" button has been pressed
    public void Send(View v) {

        // Local objects
        String myEmail, pseudo, content = null;
        JSONObject json;

        // Retrieves the values of email and nickname
        myEmail = email.getText().toString();
        pseudo = nickname.getText().toString();

        try {
            json = ConvertToJson(myEmail, pseudo);
            content = json.toString();
        }
        catch (JSONException e){}

        // Finalizes the registering
        finalizeSignUp(content);

    }

    // Method to create a JSON
    public JSONObject ConvertToJson (String email, String nickname) throws JSONException{

        JSONObject json = new JSONObject();
        JSONObject userJson = new JSONObject();
        userJson.put("nickname", nickname);
        userJson.put("email", email);
        json.put("user",userJson);
        return json;
    }

    // Gets the token generated by the server
    public void finalizeSignUp(String content) {

        // Local objects
        String webContent = null;
        JSONObject jsonObject = null;
        boolean registrationOK = true;

        // Create a DownloadTask
        DownloadTask dlTask = new DownloadTask();

        // Downloads the content we want & gets the result
        dlTask.execute("POST", "http://binouze.fabrigli.fr/users.json", "JSON", content);

        // Retrieves the result
        try {
            webContent = dlTask.get();
        } catch (InterruptedException | ExecutionException e) {}

        // Tests if the id and token are retrievable
        try {
            jsonObject = new JSONObject(webContent);
            jsonObject.getInt("id");
            jsonObject.getString("token");
        }
        catch (JSONException e) {
            registrationOK = false;
        }

        // If the registration is OK,
        if(!registrationOK) {

            // Tells the user that something went wrong
            Toast.makeText(getBaseContext(),"ERREUR : un des identifiants existe déjà sur le serveur. Réessayez.", Toast.LENGTH_LONG).show();

        }

        // Else, everything happened correctly
        else {

            // Saves the token in memory
            try {

                FileOutputStream fOut = openFileOutput("BeerloverToken.txt", MODE_PRIVATE);
                fOut.write(webContent.getBytes());
                fOut.close();

            }

            catch (FileNotFoundException e) {}
            catch (IOException e) {}

            // Shows to the user everything is all right
            Toast.makeText(getBaseContext(), "Compte bien renseigné !", Toast.LENGTH_SHORT).show();

            // Finishes the activity
            finish();
        }
    }
}
