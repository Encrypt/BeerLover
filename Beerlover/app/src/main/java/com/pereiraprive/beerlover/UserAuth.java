package com.pereiraprive.beerlover;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;


public class UserAuth extends ActionBarActivity {

    private Button envoi;
    private TextView message;
    private EditText email, nickname;
    private String my_email= null,pseudo = null, token = null;
    private JSONObject json = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the "beer_list" content
        setContentView(R.layout.user_auth);

        // Retrieves the objects

        envoi = (Button) findViewById(R.id.button_send);
        message = (TextView) findViewById(R.id.message);
        email = (EditText) findViewById(R.id.email);
        nickname = (EditText) findViewById(R.id.nickname);


        // if click, email and nickname are send to the server and we receive a token
        envoi.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                      //  Log.v("EditText", email.getText().toString());
                      //  Log.v("EditText", nickname.getText().toString());
                        // get the strings written in the app

                        my_email = new String(email.getText().toString());
                        pseudo = new String(nickname.getText().toString());
                        try {
                            json = CovertToJson(my_email, pseudo);
                        }
                        catch (JSONException e){
                        }
                    Toast.makeText(getApplication().getApplicationContext(),"Conersion Well Done !",Toast.LENGTH_LONG).show();
                    }

                });
    }

    public JSONObject CovertToJson (String email, String nickname) throws JSONException{

        JSONObject json = new JSONObject();
        JSONObject userJson = new JSONObject();
        userJson.put("email", email);
        userJson.put("nickname", nickname);
        json.put("user",userJson);

        return json;
    }

    public void SaveInMemory(View v){

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
