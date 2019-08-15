package com.naffah.searchquranapp.Controllers.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.naffah.searchquranapp.Controllers.Activities.Bookmarks.BookmarksListActivity;
import com.naffah.searchquranapp.Controllers.Activities.SuraList.SuraListScrollable;
import com.naffah.searchquranapp.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();

        String check = pref.getString("translation",null);
        if(check == null) {
            editor.putString("translation", "database/english-translations/en.ahmedali.xml");
            editor.apply();
        }

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

        ImageButton bookmarksBtn = (ImageButton) findViewById(R.id.bookmarksBtn);
        bookmarksBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoBookmarksList = new Intent(getApplicationContext(), BookmarksListActivity.class);
                startActivity(gotoBookmarksList);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.translationOption:
                TranslationsDialog dialog =new TranslationsDialog(this);
                dialog.show(getSupportFragmentManager(),"TranslationsDialog");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
