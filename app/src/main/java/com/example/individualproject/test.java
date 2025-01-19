package com.example.individualproject;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class test {
    ListView listView;
    public test() {

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }
    }
