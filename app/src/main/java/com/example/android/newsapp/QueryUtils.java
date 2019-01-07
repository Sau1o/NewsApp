package com.example.android.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getSimpleName();
    private static final int READ_TIMEOUT = 10000;

    private QueryUtils() {
    }

    public static List<InfoNews> extractFeatureFromJson(String infoNewsJSON) {

        String author = " ";

        if (TextUtils.isEmpty(infoNewsJSON)) {
            return null;
        }

        List<InfoNews> infoNews = new ArrayList<>();

        try {

            JSONObject baseJsonResponse = new JSONObject(infoNewsJSON);

            JSONObject response = baseJsonResponse.getJSONObject("response");

            JSONArray infoNewsArray = response.getJSONArray("results");

            for (int i = 0; i < infoNewsArray.length(); i++){

                JSONObject results = infoNewsArray.getJSONObject(i);

                String title = results.getString("webTitle");
                String section = results.getString("sectionId");
                String url = results.getString("webUrl");
                String date = results.getString("webPublicationDate");

                JSONArray tags = results.getJSONArray("tags");

                if (tags.isNull(0)){
                    Log.i("null","entro aqui");
                    author = " ";
                }else {

                    JSONObject tag = tags.getJSONObject(0);
                    String firstName = tag.getString("firstName");
                    String lastName = tag.getString("lastName");
                    author = firstName + " " + lastName;
                }

            InfoNews infoNew = new InfoNews(title,section,author,date,url);
            infoNews.add(infoNew);
        }

    } catch (JSONException e) {

    }
        return infoNews;
}

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(READ_TIMEOUT /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the newsapp JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public static List<InfoNews> fetchInfoNewsData(String requestUrl) {

        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<InfoNews> infoNews = extractFeatureFromJson(jsonResponse);

        return infoNews;
    }

}

