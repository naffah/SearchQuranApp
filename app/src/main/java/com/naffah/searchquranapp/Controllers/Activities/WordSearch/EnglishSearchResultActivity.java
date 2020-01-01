package com.naffah.searchquranapp.Controllers.Activities.WordSearch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.naffah.searchquranapp.Controllers.Adapters.ArabicSearchResultAdaptor;
import com.naffah.searchquranapp.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import ca.rmen.porterstemmer.PorterStemmer;

public class EnglishSearchResultActivity extends AppCompatActivity  {

    ArrayList<String> ayaList;
    ArrayList<String> transList;
    ArrayList<String> suraIndex;
    ArrayList<String> ayaIndex;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    String searchString = null;
    String searchString2 = null;
    String wordForUserProfiling = "";
    TextView textView;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_english_search_result);
        textView = (TextView) findViewById(R.id.notFound);

        pref = getApplicationContext().getSharedPreferences("MyPref", 0);

        ayaList = new ArrayList<>();
        transList = new ArrayList<>();
        suraIndex = new ArrayList<>();
        ayaIndex = new ArrayList<>();

        Intent verse = getIntent();
        searchString = verse.getStringExtra("searchWord");

        if(searchString.length() > 0 && searchString.charAt(searchString.length()-1) == ' ') {
            //remove char at last index
            searchString = searchString.substring(0,searchString.length()-1);
        }
        //make a copy
        searchString2 = searchString;
        //Stem the word
        searchString = rootWordExtraction(searchString);

        //if word is a part of English dictionary or if it is a single word
        if(func(searchString)){
            setWordForUserProfiling(searchString);
            initializeRecyclerView();
        }
        else{
            //if word is not part of Eng dictionary or is not single
            if(func(searchString2)){
                setWordForUserProfiling(searchString2);
                initializeRecyclerView();
            }
            else {
                textView.setVisibility(View.VISIBLE);
            }
        }
    }

    public String rootWordExtraction(String search){
        PorterStemmer porterStemmer = new PorterStemmer();
        String stem = porterStemmer.stemWord(search);
        return stem;
    }

    public void initializeRecyclerView(){
        textView.setVisibility(View.GONE);
        recyclerView = (RecyclerView) findViewById(R.id.english_search_result_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new ArabicSearchResultAdaptor(getApplicationContext(), ayaList, transList, suraIndex, ayaIndex, wordForUserProfiling);
        recyclerView.setAdapter(mAdapter);
        setTitle("Search Results: " + mAdapter.getItemCount());
    }

    public void setWordForUserProfiling(String word){
        int count = 0;
        //counting number of spaces in string to determine number of words
        for(int i=0; i<word.length(); i++){
            if(word.charAt(i) == ' ')
                count++;
        }
        if(count == 0){
            wordForUserProfiling = word;
        }
    }

    public boolean func(String search){

        boolean returnValue = false;
        XmlPullParserFactory xmlFactoryObject = null;
        try {
            xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myparser = xmlFactoryObject.newPullParser();
            XmlPullParser myparser2 = xmlFactoryObject.newPullParser();

            InputStream in_s = getApplicationContext().getAssets().open("database/english-translations/en.ahmedali.xml");
            myparser.setInput(in_s, null);
            InputStream in_s2 = getApplicationContext().getAssets().open("database/quran-simple.xml");
            myparser2.setInput(in_s2, null);

            String aya;
            String aya2;
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
                            aya = myparser.getAttributeValue(null,"text");
                            ayatIndex = myparser.getAttributeValue(null,"index");

                            if(aya.contains(search)){

                                aya2 = myparser2.getAttributeValue(null,"text");
                                transList.add(aya);
                                ayaList.add(aya2);
                                suraIndex.add(surahIndex);
                                ayaIndex.add(ayatIndex);
                                returnValue = true;
                            }
                        }
                        break;
                }
                event = myparser.next();
                event2 = myparser2.next();
            }
            in_s.close();
            in_s2.close();

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnValue;
    }
}
