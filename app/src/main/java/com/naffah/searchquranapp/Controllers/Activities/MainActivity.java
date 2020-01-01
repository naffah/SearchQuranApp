package com.naffah.searchquranapp.Controllers.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.room.Room;

import com.naffah.searchquranapp.Controllers.Activities.Bookmarks.BookmarksListActivity;
import com.naffah.searchquranapp.Controllers.Activities.SuraList.SuraListScrollable;
import com.naffah.searchquranapp.Models.ProjectDatabase;
import com.naffah.searchquranapp.Models.UserProfiling;
import com.naffah.searchquranapp.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String verseArabic;
    private String verseTranslation;
    private String wordFromUserProfiling;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView arabicText = findViewById(R.id.arabicVerse);
        final TextView translationText = findViewById(R.id.verseTranslation);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();

        String check = pref.getString("translation",null);
        if(check == null) {
            editor.putString("translation", "database/english-translations/en.ahmedali.xml");
            editor.apply();
        }

        // UserProfiling logic
        final ProjectDatabase projectDatabase = Room.databaseBuilder(getApplicationContext(), ProjectDatabase.class, "bookmarks_db").build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<UserProfiling> userList = projectDatabase.daoAccess().fetchUserProfiling();
                int size = userList.size();
                if(size == 0){
                    getVerseOfTheDay("life");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            arabicText.setText(verseArabic);
                            translationText.setText(verseTranslation);
                        }
                    });
                } else {
                    double randomDouble = Math.random();
                    randomDouble = randomDouble * size ;
                    int randomInt = (int) randomDouble;

                    for(int i=0; i<size; i++){
                        Log.d("Damion", userList.get(i).getWord());
                    }

                    wordFromUserProfiling = userList.get(randomInt).getWord();
                    getVerseOfTheDay(wordFromUserProfiling);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            arabicText.setText(verseArabic);
                            translationText.setText(verseTranslation);
                        }
                    });
                }
            }
        }).start();
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

    //For file reading
    ArrayList<String> ayaList = new ArrayList<>();
    ArrayList<String> transList = new ArrayList<>();
    ArrayList<String> suraIndex = new ArrayList<>();
    ArrayList<String> ayaIndex = new ArrayList<>();

    public void getVerseOfTheDay(String search){

        XmlPullParserFactory xmlFactoryObject;
        try {
            xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myparser = xmlFactoryObject.newPullParser();
            XmlPullParser myparser2 = xmlFactoryObject.newPullParser();

            InputStream in_s = getApplicationContext().getAssets().open("database/english-translations/en.ahmedali.xml");
            myparser.setInput(in_s, null);
            InputStream in_s2 = getApplicationContext().getAssets().open("database/quran-simple.xml");
            myparser2.setInput(in_s2, null);

            String englishVerse;
            String arabicVerse;

            String surahIndex = "";
            String ayatIndex;

            int event = myparser.getEventType();
            int event2 = myparser2.getEventType();

            while (event != XmlPullParser.END_DOCUMENT)  {
                String name=myparser.getName();
                switch (event){
                    case XmlPullParser.START_TAG:
                        if(name.equals("sura")){
                            surahIndex = myparser.getAttributeValue(null,"index");
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if(name.equals("aya")){
                            englishVerse = myparser.getAttributeValue(null,"text");
                            ayatIndex = myparser.getAttributeValue(null,"index");

                            if(englishVerse.contains(search)){
                                arabicVerse = myparser2.getAttributeValue(null,"text");

                                transList.add(englishVerse);
                                ayaList.add(arabicVerse);
                                suraIndex.add(surahIndex);
                                ayaIndex.add(ayatIndex);
                            }
                        }
                        break;
                }
                event = myparser.next();
                event2 = myparser2.next();
            }
            in_s.close();
            in_s2.close();

        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        //Get random index from finalized list of verses
        double randomDouble = Math.random();
        randomDouble = randomDouble * transList.size();
        int randomInt = (int) randomDouble;
        //random ends here

        //Setting verse of the day
        verseTranslation = transList.get(randomInt);
        verseArabic = ayaList.get(randomInt);

        //Removing comma at the end of sentence, if any exists
        if(verseTranslation.charAt(verseTranslation.length()-1) == ',')
            verseTranslation = verseTranslation.substring(0,verseTranslation.length()-1);
    }
}
