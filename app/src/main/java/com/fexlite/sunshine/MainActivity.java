package com.fexlite.sunshine;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fexlite.utilities.NetworkUtils;

import com.fexlite.sunshine.R;

import java.io.IOException;
import java.net.URL;

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

    }

    private void makeSearchQuery()
    {
    String githubQuery  = mSearchBox.getText().toString();
    URL githubSearchUrl = NetworkUtils.buildUrl(githubQuery);
    mUrlTextView.setText(githubSearchUrl.toString());


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
                mSearchResultsTextView.setText(result);
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
