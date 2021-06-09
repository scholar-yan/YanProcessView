package com.yan.android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        YanProcessView ypv = findViewById(R.id.ypv);
        List<String> list = new ArrayList<>();
        list.add("Tab1");
        list.add("Tab2");
        list.add("Tab3");
        list.add("Tab4");
        list.add("Tab5");
        list.add("Tab6");
        list.add("Tab7");
        list.add("Tab8");
        ypv.setData(list, 2);
    }
}