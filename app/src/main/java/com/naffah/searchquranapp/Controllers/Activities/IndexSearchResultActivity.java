package com.naffah.searchquranapp.Controllers.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.naffah.searchquranapp.R;

public class IndexSearchResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_search_result);

        TextView verseText = (TextView) findViewById(R.id.verseTextView);

        Intent verse = getIntent();

        String verseStr = (String) verse.getSerializableExtra("verse");

        verseText.setText(verseStr);
        verseText.append("o  ");
    }
}
