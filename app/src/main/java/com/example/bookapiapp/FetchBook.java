//You are now ready to connect to the internet and use the Books API.
// In this task you create a new AsyncTask subclass called FetchBook to handle connecting to the network.

package com.example.bookapiapp;

import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

//Async Task Parameter (input-parameter type, a progress-indicator type, and a result type).
//String because the query is a string, Void because there is no progress indicator, and String because the JSON response is a string
public class FetchBook extends AsyncTask<String, Void, String> {

//    To display the results in the TextView objects in MainActivity, you must have access to those text views inside the AsyncTask.
//    Create WeakReference member variables for references to the two text views that show the results.
    private WeakReference<TextView> mTitleText;
    private WeakReference<TextView> mAuthorText;


    
    FetchBook(TextView titleText, TextView authorText){
        this.mTitleText=new WeakReference<TextView>(titleText);
        this.mAuthorText=new WeakReference<TextView>(authorText);
    }
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            //obtain the JSON array of items from the result string.
            JSONObject jsonObject=new JSONObject(s);

            JSONArray itemsArray=jsonObject.getJSONArray("items");
//Iterate through the itemsArray array, checking each book for title and author information
            int i=0;
            String title=null;
            String authors=null;

            while (i<itemsArray.length()&& (authors==null &&title==null)){
//                Get the current item information
                JSONObject book=itemsArray.getJSONObject(i);
                JSONObject volumeInfo=book.getJSONObject("volumeInfo");

//                Try to get the title and author from the current item
//                catch if either field is emptu and move on
                try{
                    title=volumeInfo.getString("title");
                    authors=volumeInfo.getString("authors");
                }catch (Exception e){
                    e.printStackTrace();
                }
//                Move to the next item
                i++;
            }
            if (title != null && authors != null) {
                mTitleText.get().setText(title);
                mAuthorText.get().setText(authors);
            }else {
                mTitleText.get().setText(R.string.no_results);
                mAuthorText.get().setText("");
            }

        } catch (JSONException e) {
            // If onPostExecute does not receive a proper JSON string,
            // update the UI to show failed results.
            mTitleText.get().setText(R.string.no_results);
            mAuthorText.get().setText("");
            e.printStackTrace();
        }
    }

//    Network connectivity can be be sluggish, which can make your app erratic or slow.
    @Override
    protected String doInBackground(String... strings) {
//        passing in the search term that you obtained from the params argument passed in by the system.
        return NetworkUtils.getBookInfo(strings[0]);
    }
}
