package com.naffah.searchquranapp.Controllers.Activities.SuraList;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;

import com.naffah.searchquranapp.Controllers.Adapters.SuraListAdapter;
import com.naffah.searchquranapp.Models.SurahModel;
import com.naffah.searchquranapp.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class SuraListScrollable extends AppCompatActivity {

    public static final String XML_ROOT_STRING_1 = "quran";
    public static final String XML_ROOT_STRING_2 = "suras";
    public static final String XML_ROOT_SEARCH_1 = "sura";

    String currentTag = null;

    private ArrayList<String> suraNum = new ArrayList<>();
    private ArrayList<String> suraNamesEn = new ArrayList<>();
    private ArrayList<String> suraNamesArabic = new ArrayList<>();
    private ArrayList<Integer> suraAyasTotal = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sura_list_scrollable);

        initSuraNames();
    }

    private void initSuraNames() {
        XmlPullParserFactory suraParserFactory;

        try {
            suraParserFactory = XmlPullParserFactory.newInstance();
            suraParserFactory.setNamespaceAware(true);
            XmlPullParser suraParser = suraParserFactory.newPullParser();
            InputStream is = getAssets().open("database/quran-metadata.xml");

            suraParser.setInput(is, null);

            int eventType = suraParser.getEventType();

            // Loop through the xml document
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if (!suraParser.getName().equals(XML_ROOT_STRING_1) &&
                            !suraParser.getName().equals(XML_ROOT_STRING_2) &&
                            suraParser.getName().equals(XML_ROOT_SEARCH_1)) {
                        currentTag = suraParser.getName();

                        suraNum.add(suraParser.getAttributeValue(null,"index"));
                        suraNamesArabic.add(suraParser.getAttributeValue(null, "name"));
                        suraNamesEn.add(suraParser.getAttributeValue(null, "tname"));
                        suraAyasTotal.add(Integer.parseInt(suraParser.getAttributeValue(null, "ayas")));
                    }
                }
                else if(eventType == XmlPullParser.END_TAG) {
                    currentTag = null;
                }
                if (eventType == XmlPullParser.END_DOCUMENT)
                    break;
                eventType = suraParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        initSuraRecyclerView();
    }

    private void initSuraRecyclerView() {
        RecyclerView rView = findViewById(R.id.suraListView);
        SuraListAdapter adapter = new SuraListAdapter(getApplicationContext(), suraNum, suraNamesEn, suraNamesArabic, suraAyasTotal);
        rView.setAdapter(adapter);
        rView.setLayoutManager(new LinearLayoutManager(this));
    }
}
