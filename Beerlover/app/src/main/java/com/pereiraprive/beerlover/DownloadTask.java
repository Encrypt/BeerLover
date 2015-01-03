package com.pereiraprive.beerlover;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadTask extends AsyncTask<String, Void, String> {

    // Class variable
    private String webMethod, myURL, postContent, fileType;

    // Method doInBackground (called once .execute() is called)
    @Override
    protected String doInBackground(String... args) {
        try {

            // Retrieves the web method (GET or POST)
            webMethod = args[0];

            // Retrieves the URL
            myURL = args[1];

            // Retrieves the type
            fileType = args[2];

            // Retrieves the POST content if there is some
            if(args.length == 4)
                postContent = args[3];

            // Returns the downloaded content
            return downloadUrl();

        } catch (IOException e) {
            return "Error while trying to download content";
        }

    }

    // Method downloading
    private String downloadUrl() throws IOException {

        // Method objects
        InputStream is = null;
        String result = "Erreur";

        try {

            URL url = new URL(myURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Sets the request method
            conn.setRequestMethod(webMethod);

            // Set the content type (json) in case of user management
            if(fileType.equals("JSON"))
                conn.setRequestProperty("Content-Type", "application/json");

            // Connects
            conn.connect();

            // If the method is post, send data
            if(webMethod.equals("POST")) {

                // Creates an OutputStream
                DataOutputStream wr = new DataOutputStream(conn.getOutputStream());

                // Sends data
                wr.writeBytes(postContent);
                wr.flush();
                wr.close();
            }

            int response = conn.getResponseCode();
            is = conn.getInputStream();

            // Checks if the file is a picture (filetype = IMG)
            if(fileType.equals("IMG")) {

                // Local objects
                Bitmap bitmapBeer;
                String stringBeer;
                byte[] byteBeer;
                ByteArrayOutputStream byteOS;

                // Encodes the picture
                bitmapBeer = BitmapFactory.decodeStream(is);
                byteOS = new ByteArrayOutputStream();
                bitmapBeer.compress(Bitmap.CompressFormat.PNG, 100, byteOS);
                byteBeer = byteOS.toByteArray();
                stringBeer = Base64.encodeToString(byteBeer, Base64.DEFAULT);

                result = stringBeer;
            }

            // Else, it's simply text, returns the content downloaded
            else if(fileType.equals("TXT") || fileType.equals("JSON"))
                result = readText(is, 1000);

            else if(fileType.equals("BTXT"))
                result = readText(is, 20000);

        }

        finally {
            if (is != null)  {
                is.close();
            }
        }

        return result;

    }

    // Method to read "len" length of the content
    private String readText(InputStream stream, int len) throws IOException {

        // Creates a buffered reader (which is more convenient to the previous byte usage)
        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"), len);

        // Reads the line (the JSON) and returns the string
        String string = br.readLine();

        return string;

    }

}
