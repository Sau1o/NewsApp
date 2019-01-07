package com.example.android.newsapp;

import android.content.Context;
import android.content.AsyncTaskLoader;

import java.util.List;

public class InfoNewsLoader extends AsyncTaskLoader <List<InfoNews>> {

    private static final String LOG_TAG = InfoNewsLoader.class.getName();

    private String mUrl;

    public InfoNewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<InfoNews> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        List<InfoNews> infoNews = QueryUtils.fetchInfoNewsData(mUrl);
        return infoNews;
    }
}
