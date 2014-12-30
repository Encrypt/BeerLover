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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
    private String content = null;

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
                            content = json.toString();
                        }
                        catch (JSONException e){
                        }
                    Toast.makeText(getApplication().getApplicationContext(),"Conversion Well Done !"+ content,Toast.LENGTH_LONG).show();
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


    private void SaveInMemory(String nom_Fichier,String mon_Text) {
        BufferedWriter writer = null;
        try {
            File dir = getDir("ToutMesFichiers",MODE_PRIVATE);
            File new_file = new File(dir.getAbsolutePath() + File.separator + nom_Fichier);
            new_file.createNewFile();
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new_file)));
            writer.write(mon_Text);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void PostUserToken(JSONObject json) {

        String token = null;
        // Create a DownloadTask
        DownloadTask dltask = new DownloadTask();

        // Downloads the content we want & gets the result
        // TODO
        dltask.execute("POST", "http://binouze.fabrigli.fr/user.json");

        // Retrieves the result
        try {
            token = dltask.get();
            System.out.println(token);

        } catch (InterruptedException e) {

        } catch (ExecutionException e) {

        }
    }
}
