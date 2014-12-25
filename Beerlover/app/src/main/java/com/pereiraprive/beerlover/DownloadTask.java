package com.pereiraprive.beerlover;

import android.os.AsyncTask;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadTask extends AsyncTask<String, Void, String> {

    // Class variable
    String downloadedContent;
    TextView textElement;

    // Method calling downloadUrl
    @Override
    protected String doInBackground(String... urls) {
        try {
            return downloadUrl(urls[0]);
        } catch (IOException e) {
            return "Error";
        }
    }
/*
    // Method calling
    @Override
    protected void onPostExecute(String result) {

        // Creates the JSON Object
        JSONObject myJsonObject;
        String stringJson = new String();

        // Fills the JSON Object
        try {
            myJsonObject = new JSONObject(result);
            stringJson = myJsonObject.toString();
        }
        catch(JSONException e){
            // Nothing
        }

        // Sets the text
        textElement.setText(stringJson);

    }
*/
    private String downloadUrl(String myURL) throws IOException {
        InputStream is = null;

        try {
            URL url = new URL(myURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int response = conn.getResponseCode();
            is = conn.getInputStream();

            // Convert the InputStream into a string
            downloadedContent = readIt(is, 700);

            // Returns sucess
            return "success";
        }

        finally {
            if (is != null)  {
                is.close();
            }
        }

    }

    // Method to read "len" length of the content
    private String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    // Method to get the (plain) downloaded content in plain text
    public String getPlainContent() {

        return downloadedContent;
    }

}
