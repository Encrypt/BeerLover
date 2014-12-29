package com.pereiraprive.beerlover;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadTask extends AsyncTask<String, Void, String> {

    // Class variable
    private String webMethod;

    // Method doInBackground (called once .execute() is called)
    @Override
    protected String doInBackground(String... args) {
        try {

            // Retrieves the web method (GET or POST)
            webMethod = args[0];

            // Returns the downloaded content
            return downloadUrl(args[1]);

        } catch (IOException e) {
            return "Error while trying to download content";
        }

    }

    // Method downloading
    private String downloadUrl(String myURL) throws IOException {
        InputStream is = null;

        try {
            URL url = new URL(myURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(webMethod);
            conn.connect();
            int response = conn.getResponseCode();
            is = conn.getInputStream();

            // Returns the content downloaded
            return readIt(is, 700);
        }

        finally {
            if (is != null)  {
                is.close();
            }
        }

    }

    // Method to read "len" length of the content
    private String readIt(InputStream stream, int len) throws IOException {
        Reader reader;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

}
