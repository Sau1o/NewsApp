package com.example.android.newsapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.ArrayList;

public class InfoNewsAdapter extends ArrayAdapter<InfoNews>{

    private static final String DATE_SEPARATOR = "T";

    public InfoNewsAdapter(Activity context, ArrayList<InfoNews> infoNews) {
        super(context, 0, infoNews);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        InfoNews currentInfoNews = getItem(position);

        String title = currentInfoNews.getTitle();

        String section = currentInfoNews.getSection();

        String author = currentInfoNews.getAuthor();

        String date = currentInfoNews.getDate();

        if (date.contains(DATE_SEPARATOR)) {
            String[] parts = date.split(DATE_SEPARATOR);
            date = parts[0];
        }

        TextView titleView = listItemView.findViewById(R.id.title);
        titleView.setText(title);

        TextView sectionView = listItemView.findViewById(R.id.section);
        sectionView.setText(section);

        TextView authorView = listItemView.findViewById(R.id.author);
        authorView.setAllCaps(true);
        authorView.setText(author);

        TextView dateView = listItemView.findViewById(R.id.date);
        dateView.setText(date);

        return listItemView;
    }
}
