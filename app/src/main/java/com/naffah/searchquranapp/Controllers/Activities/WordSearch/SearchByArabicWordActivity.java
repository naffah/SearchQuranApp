package com.naffah.searchquranapp.Controllers.Activities.WordSearch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.naffah.searchquranapp.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.text.Normalizer;
import java.util.ArrayList;

public class SearchByArabicWordActivity extends AppCompatActivity {

    String searchString;
    String hasDiacritics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_arabic_word);

        final EditText searchText = (EditText) findViewById(R.id.arabicText);
        Button searchBtn = (Button) findViewById(R.id.searchButton);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchString = searchText.getText().toString();
                String noDiacritics = Normalizer.normalize(searchString, Normalizer.Form.NFKD).replaceAll("\\p{M}", "");

                if (searchString.equals(noDiacritics)) {
                    hasDiacritics = "no";
                }
                else if (!searchString.equals(noDiacritics)) {
                    hasDiacritics = "yes";
                }

                Intent intent=new Intent(SearchByArabicWordActivity.this, ArabicSearchResultActivity.class);
                intent.putExtra("searchWord",searchString);
                intent.putExtra("hasDiacritics", hasDiacritics);
                startActivity(intent);
            }
        });
    }
}
