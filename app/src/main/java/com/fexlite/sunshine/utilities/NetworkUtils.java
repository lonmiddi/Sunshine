package com.fexlite.sunshine.utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class NetworkUtils {

    final static String BASE_URL = "http://api.openweathermap.org/data/2.5/weather";

    final static String PARAM_QUERY = "q";
    final static String APP_ID = "appid";
    final static String appid = "8009a39e68505a876f37a6fecaa23f1d";

    public static URL buildUrl(String weatherDataQuery)
    {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_QUERY, weatherDataQuery)
                .appendQueryParameter(APP_ID, appid).build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        }catch (MalformedURLException e)
        {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException
    {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try
        {
            InputStream in = urlConnection.getInputStream();
            Scanner nippy = new Scanner(in);
            nippy.useDelimiter("\\A");

            boolean hasInput = nippy.hasNext();
            if(hasInput)
            {
            return nippy.next();
            }else
                {
                    return  null;
                }
        }finally {
            urlConnection.disconnect();
        }
    }
}
