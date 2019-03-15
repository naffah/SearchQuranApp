package com.naffah.searchquranapp.Controllers.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.naffah.searchquranapp.Models.SurahModel;
import com.naffah.searchquranapp.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SearchByIndexActivity extends AppCompatActivity {

    public static final String XML_ROOT_STRING_1 = "quran";
    public static final String XML_ROOT_STRING_2 = "suras";
    public static final String XML_ROOT_SEARCH_1 = "sura";

    private List<SurahModel> suraList = new ArrayList<>();
    private List<String> list = new ArrayList<>();

    private String currentTag = null, verseString = null;

    private Spinner spinner;
    private Button searchBtn;
    private EditText verseNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verse_index);

        spinner = (Spinner) findViewById(R.id.surahDropdown);
        verseNo = (EditText) findViewById(R.id.verseText);

        searchBtn = (Button) findViewById(R.id.searchButton);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check = validateVerse();
                if (check) {
                    Toast.makeText(getApplicationContext(), verseString, Toast.LENGTH_SHORT).show();
                }
                else {
                    return;
                }
            }
        });

        parseSuraMetadataXML();
    }

    private void parseSuraMetadataXML() {
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

                        SurahModel sura = new SurahModel();
                        sura.setSura(suraParser.getAttributeValue(null, "name"));
                        sura.setIndex(suraParser.getAttributeValue(null, "index"));
                        sura.setVerses(suraParser.getAttributeValue(null, "ayas"));

                        suraList.add(sura);
                        list.add(sura.getSura());

                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                                android.R.layout.simple_spinner_item, list);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(dataAdapter);
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
    }

    private boolean validateVerse(){
        String spinnerText = spinner.getSelectedItem().toString();
        int verseNum = Integer.parseInt(verseNo.getText().toString());
        SurahModel sura = new SurahModel();

        for (int i = 0; i < suraList.size(); i++) {
            sura = suraList.get(i);
            if (sura.getSura().equals(spinnerText)) {
                int verses = Integer.parseInt(sura.getVerses());
                if (verses >= verseNum) {
                    searchVerseByNumber(sura.getSura(), verseNum, verses);
                    return true;
                }
                else if (verses < verseNum) {
                    Intent showVerseFound = new Intent(getApplicationContext(), IndexSearchResultActivity.class);
                    showVerseFound.putExtra("verse", verseString);
                    startActivity(showVerseFound);

                    return false;
                }
            }
        }
        return false;
    }

    private void searchVerseByNumber(String sura, int verseNum, int verses) {
        XmlPullParserFactory suraParserFactory;

        try {
            suraParserFactory = XmlPullParserFactory.newInstance();
            suraParserFactory.setNamespaceAware(true);
            XmlPullParser suraParser = suraParserFactory.newPullParser();
            InputStream is = getAssets().open("database/quran-simple.xml");

            suraParser.setInput(is, null);

            int eventType = suraParser.getEventType();

            String index = null;
            boolean verseFound = false;

            // Loop through the xml document
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if (!suraParser.getName().equals(XML_ROOT_STRING_1) &&
                            suraParser.getName().equals(XML_ROOT_SEARCH_1)) {
                        currentTag = suraParser.getName();

                        String suraName = suraParser.getAttributeValue(null, "name");

                        if (suraName.equals(sura)) {
                            while (!verseFound) {
                                currentTag = suraParser.getName();
                                eventType = suraParser.nextTag();
                                index = suraParser.getAttributeValue(null, "index");
                                if (Integer.parseInt(index) == verseNum) {
                                    verseString = suraParser.getAttributeValue(null, "text");
                                    verseFound = true;
                                }
                                if (eventType == XmlPullParser.END_DOCUMENT)
                                    break;
                            }
                        }
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
    }
}