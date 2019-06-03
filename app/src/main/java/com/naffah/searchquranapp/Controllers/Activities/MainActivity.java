package com.naffah.searchquranapp.Controllers.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.naffah.searchquranapp.Controllers.Activities.IndexSearch.SearchByIndexActivity;
import com.naffah.searchquranapp.Controllers.Activities.SuraList.SuraListScrollable;
import com.naffah.searchquranapp.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton suraList = (ImageButton) findViewById(R.id.suraListBtn);
        suraList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoSuraList = new Intent(getApplicationContext(), SuraListScrollable.class);
                startActivity(gotoSuraList);
            }
        });

        ImageButton searchBtn = (ImageButton) findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoSearchByVerse = new Intent(getApplicationContext(), SearchMainActivity.class);
                startActivity(gotoSearchByVerse);
            }
        });
    }
}
