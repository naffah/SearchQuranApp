package com.naffah.searchquranapp.Controllers.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

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
    }

    public void SuraListBtnClicked(View view) {
        Intent gotoSuraList = new Intent(getApplicationContext(), SuraListScrollable.class);
        startActivity(gotoSuraList);
    }

    public void SearchBtnClicked(View view) {
        Intent gotoSearchByVerse = new Intent(getApplicationContext(), SearchMainActivity.class);
        startActivity(gotoSearchByVerse);
    }

    public void BookmarksBtnClicked(View view) {
        Intent gotoBookmarksList = new Intent(getApplicationContext(), BookmarksListActivity.class);
        startActivity(gotoBookmarksList);
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
        if (item.getItemId() == R.id.translationOption) {
            TranslationsDialog dialog = new TranslationsDialog(this);
            dialog.show(getSupportFragmentManager(), "TranslationsDialog");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
