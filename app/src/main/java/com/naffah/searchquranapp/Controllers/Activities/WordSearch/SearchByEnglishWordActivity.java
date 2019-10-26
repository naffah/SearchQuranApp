package com.naffah.searchquranapp.Controllers.Activities.WordSearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.naffah.searchquranapp.R;

public class SearchByEnglishWordActivity extends AppCompatActivity {

    String searchString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_english_word);

        final EditText searchText = (EditText) findViewById(R.id.engText);
        Button searchBtn = (Button) findViewById(R.id.searchButton);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchString = searchText.getText().toString();

                Intent intent=new Intent(SearchByEnglishWordActivity.this, EnglishSearchResultActivity.class);
                intent.putExtra("searchWord",searchString);
                startActivity(intent);
            }
        });
    }
}
