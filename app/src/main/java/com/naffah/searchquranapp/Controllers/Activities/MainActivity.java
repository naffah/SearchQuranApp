package com.naffah.searchquranapp.Controllers.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.naffah.searchquranapp.Controllers.Activities.Bookmarks.BookmarksListActivity;
import com.naffah.searchquranapp.Controllers.Activities.SuraList.SuraListScrollable;
import com.naffah.searchquranapp.R;

public class MainActivity extends AppCompatActivity {

    ImageView bgapp, clover;
    LinearLayout textsplash, texthome, menus;
    Animation frombottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frombottom = AnimationUtils.loadAnimation(this, R.anim.frombottom);

        bgapp = findViewById(R.id.bgapp);
        clover = findViewById(R.id.clover);
        textsplash = findViewById(R.id.textsplash);
        texthome = findViewById(R.id.texthome);
        menus = findViewById(R.id.menus);

        bgapp.animate().translationY(-1400).setDuration(800).setStartDelay(300);
        clover.animate().alpha(0).setDuration(800).setStartDelay(600);
        textsplash.animate().translationY(140).alpha(0).setDuration(800).setStartDelay(300);

        texthome.startAnimation(frombottom);
        menus.startAnimation(frombottom);

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
