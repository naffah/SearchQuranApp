package com.naffah.searchquranapp.Controllers.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.naffah.searchquranapp.Controllers.Activities.IndexSearch.SearchByIndexActivity;
import com.naffah.searchquranapp.R;

public class SearchMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_main);

        Button searchByVerseNo = (Button) findViewById(R.id.searchByVerseBtn);
        searchByVerseNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoSearchByVerse = new Intent(getApplicationContext(), SearchByIndexActivity.class);
                startActivity(gotoSearchByVerse);
            }
        });
    }
}
