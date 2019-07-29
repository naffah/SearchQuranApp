package com.naffah.searchquranapp.Controllers.Activities.WordSearch;

import android.content.Intent;
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

public class ArabicSearchResultActivity extends AppCompatActivity {

    ArrayList<String> ayaList;
    ArrayList<String> transList;
    ArrayList<String> suraIndex;
    ArrayList<String> ayaIndex;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    boolean found;
    String searchString = null;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arabic_search_result);
        textView = (TextView) findViewById(R.id.notFound);

        ayaList = new ArrayList<>();
        transList = new ArrayList<>();
        suraIndex = new ArrayList<>();
        ayaIndex = new ArrayList<>();

        Intent verse = getIntent();
        searchString = verse.getStringExtra("searchWord");

        found = func(searchString);

        if(found){
            textView.setVisibility(View.GONE);
            recyclerView = (RecyclerView) findViewById(R.id.arabic_search_result_recycler_view);

            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            recyclerView.setHasFixedSize(true);

            // use a linear layout manager
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);

            // specify an adapter (see also next example)
            mAdapter = new ArabicSearchResultAdaptor(getApplicationContext(), ayaList, transList, suraIndex, ayaIndex);
            recyclerView.setAdapter(mAdapter);
        }
        else{
            textView.setVisibility(View.VISIBLE);
        }
    }

    public boolean func(String search){
        boolean returnValue = false;

        XmlPullParserFactory xmlFactoryObject = null;
        try {
            xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myparser = xmlFactoryObject.newPullParser();
            XmlPullParser myparser2 = xmlFactoryObject.newPullParser();
            XmlPullParser myparser3 = xmlFactoryObject.newPullParser();

            InputStream in_s = getApplicationContext().getAssets().open("database/quran-clean.xml");
            myparser.setInput(in_s, null);
            InputStream in_s2 = getApplicationContext().getAssets().open("database/quran-simple.xml");
            myparser2.setInput(in_s2, null);
            InputStream in_s3 = getApplicationContext().getAssets().open("database/english-translations/en.ahmedali.xml");
            myparser3.setInput(in_s3, null);

            String aya;
            String aya2;
            String aya3;
            String surahIndex = "";
            String ayatIndex;

            int event = myparser.getEventType();
            int event2 = myparser2.getEventType();
            int event3 = myparser3.getEventType();

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
                                aya3 = myparser3.getAttributeValue(null,"text");
                                ayaList.add(aya2);
                                transList.add(aya3);
                                suraIndex.add(surahIndex);
                                ayaIndex.add(ayatIndex);
                                returnValue = true;
                            }
                        }
                        break;
                }
                event = myparser.next();
                event2 = myparser2.next();
                event3 = myparser3.next();
            }
            in_s.close();
            in_s2.close();
            in_s3.close();

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnValue;
    }
}
