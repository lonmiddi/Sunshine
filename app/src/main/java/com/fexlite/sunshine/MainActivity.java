package com.fexlite.sunshine;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;


import com.fexlite.sunshine.utilities.NetworkUtils;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private EditText mSearchBox;
    private TextView mUrlTextView;
    private TextView mSearchResultsTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchBox = (EditText) findViewById(R.id.urlentered);
        mUrlTextView = (TextView) findViewById(R.id.info);
        mSearchResultsTextView = (TextView) findViewById(R.id.results);

    }

    private void makeSearchQuery()
    {
    String weatherQuery  = mSearchBox.getText().toString();
    URL weatherSearchUrl = NetworkUtils.buildUrl(weatherQuery);

    new GitHubQueryTask().execute(weatherSearchUrl);

    }

    public class GitHubQueryTask extends AsyncTask<URL, Void, String>
    {
        @Override
        protected String doInBackground (URL... urls)
        {
            URL searchUrls = urls[0];
            String githubSearchResults = null;
            try{
                githubSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrls);
            }catch(IOException e){
                e.printStackTrace();
            }
            return githubSearchResults;
        }

        @Override
        protected void onPostExecute (String result)
        {
            if(result != null && !result.equals(""))
            {
                try {
                    JSONObject root = new JSONObject(result);
                    JSONObject coord = root.getJSONObject("coord");
                    String longitude = coord.getString("lon");
                    String latitude  = coord.getString("lat");
                    JSONArray weather = root.getJSONArray("weather");
                    JSONObject Array1 = weather.getJSONObject(0);
                    String main = Array1.getString("main");
                    String description = Array1.getString("description");
                    JSONObject main2 = root.getJSONObject("main");
                    Double temp = (main2.getDouble("temp") - 273.15);
                    DecimalFormat df = new DecimalFormat("###.##");
                    String truncTemp = df.format(temp);

                    JSONObject sys = root.getJSONObject("sys");
                    long sunriseRaw = sys.getLong("sunrise");
                    Date date = new java.util.Date(sunriseRaw*1000L);
                    SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm:ss z");
                    String sunrise = sdf.format(date);

                    long sunsetRaw = sys.getLong("sunset");
                    Date date2 = new java.util.Date(sunsetRaw*1000L);
                    SimpleDateFormat sdf2 = new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm:ss z");
                    String sunset = sdf2.format(date2);

                    String country_code = sys.getString("country");
                    mSearchResultsTextView.setText("Main: "+main+"\nDescription: "+description+"\n Temperatrure: "+truncTemp+"\nSunrise: "+sunrise+"\nSunset: "+sunset);
                    mUrlTextView.setText(country_code);
                }catch(JSONException e)
                {
                    e.printStackTrace();
                }


            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
     getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_search) {
          makeSearchQuery();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
