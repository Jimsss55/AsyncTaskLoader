//connecting to the internet in a helper class called NetworkUtils.
package com.example.bookapiapp;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtils {
    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();
    // Base URL for Books API.
    private static final String BOOK_BASE_URL =  "https://www.googleapis.com/books/v1/volumes?";
    // Parameter for the search string.
    private static final String QUERY_PARAM = "q";
    // Parameter that limits search results.
    private static final String MAX_RESULTS = "maxResults";
    // Parameter to filter by print type.
    private static final String PRINT_TYPE = "printType";

//    method takes the search term as a String parameter and
//    returns the JSON String response from the API you examined earlier.


    static String getBookInfo(String queryString){
        //      variables for connecting to the internet,
        HttpURLConnection urlConnection=null;

        // reading the incoming data,
        BufferedReader reader=null;

//  and holding the response string.
        String bookJSONString=null;

        try{
            Uri builtURI = Uri.parse(BOOK_BASE_URL).buildUpon()
                    .appendQueryParameter(QUERY_PARAM, queryString)
                    .appendQueryParameter(MAX_RESULTS, "10")
                    .appendQueryParameter(PRINT_TYPE, "books")
                    .build();

//            convert your URI to a URL object:
            URL requestURL = new URL(builtURI.toString());

//             Make the request
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

//            response from the connection

            // Get the InputStream.
            InputStream inputStream = urlConnection.getInputStream();

// Create a buffered reader from that input stream.
            reader = new BufferedReader(new InputStreamReader(inputStream));

// Use a StringBuilder to hold the incoming response.
            StringBuilder builder = new StringBuilder();

//Read the input line-by-line into the string while there is still input:
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                // Since it's JSON, adding a newline isn't necessary (it won't
                // affect parsing) but it does make debugging a *lot* easier
                // if you print out the completed buffer for debugging.
                builder.append("\n");
            }
            if (builder.length() == 0) {
                // Stream was empty. No point in parsing.
                return null;
            }
            bookJSONString = builder.toString();

        }catch (IOException e){
            e.printStackTrace();
        }
//         close both the connection and the BufferedReader
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Log.d(LOG_TAG, bookJSONString);
        }

        return bookJSONString;
    }
}
