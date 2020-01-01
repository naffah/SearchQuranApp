package com.naffah.searchquranapp.Controllers.Activities.IndexSearch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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

    private String currentTag = null, verseString = null, translationString = null;

    private Spinner spinner;
    private Button searchBtn;
    private EditText verseNo;
    private String suraNo;

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
                    Intent showVerseFound = new Intent(getApplicationContext(), IndexSearchResultActivity.class);
                    showVerseFound.putExtra("verse", verseString);
                    showVerseFound.putExtra("translation", translationString);
                    showVerseFound.putExtra("verseNo", verseNo.getText().toString());
                    showVerseFound.putExtra("suraNo", suraNo);
                    startActivity(showVerseFound);
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
                        list.add(sura.getIndex() + "." + " " + sura.getSura());

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.my_spinner_style, list) {

                            public View getView(int position, View convertView, ViewGroup parent) {

                                View v = super.getView(position, convertView, parent);

                                ((TextView) v).setTextSize(24);

                                return v;

                            }

                            public View getDropDownView(int position, View convertView,ViewGroup parent) {

                                View v = super.getDropDownView(position, convertView, parent);

                                ((TextView) v).setGravity(Gravity.RIGHT);
                                ((TextView) v).setTextSize(24);

                                return v;

                            }

                        };
                        spinner.setAdapter(adapter);
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
        String placeholderText = spinner.getSelectedItem().toString();
        int spaceIndex = placeholderText.indexOf(" ");
        Toast.makeText(this, "Space index: " + spaceIndex, Toast.LENGTH_LONG).show();
        String spinnerText = placeholderText.substring(spaceIndex + 1);
        int verseNum = Integer.parseInt(verseNo.getText().toString());
        SurahModel sura = new SurahModel();

        for (int i = 0; i < suraList.size(); i++) {
            sura = suraList.get(i);
            if (sura.getSura().equals(spinnerText)) {
                int verses = Integer.parseInt(sura.getVerses());
                if (verseNum < 0) {
                    Toast.makeText(getApplicationContext(), "Verse number cannot be negative.", Toast.LENGTH_LONG).show();
                    return false;
                }
                else if (verseNum == 0) {
                    Toast.makeText(getApplicationContext(), "Verse number cannot be zero.", Toast.LENGTH_LONG).show();
                    return false;
                }
                else if (verses >= verseNum) {
                    searchVerseByNumber(sura.getSura(), verseNum, verses);
                    suraNo = sura.getIndex();
                    return true;
                }
                else if (verses < verseNum) {
                    Toast.makeText(getApplicationContext(), "Number of verses greater than " + verses + " verses.", Toast.LENGTH_LONG).show();
                    return false;
                }
            }
        }
        return false;
    }

    private void searchVerseByNumber(String sura, int verseNum, int verses) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        XmlPullParserFactory suraParserFactory, transParserFactory;

        try {
            suraParserFactory = XmlPullParserFactory.newInstance();
            suraParserFactory.setNamespaceAware(true);
            XmlPullParser suraParser = suraParserFactory.newPullParser();

            transParserFactory = XmlPullParserFactory.newInstance();
            transParserFactory.setNamespaceAware(true);
            XmlPullParser transParser = transParserFactory.newPullParser();

            InputStream is = getAssets().open("database/quran-simple.xml");
            InputStream trans = getAssets().open(pref.getString("translation","database/english-translations/en.ahmedali.xml"));

            suraParser.setInput(is, null);
            transParser.setInput(trans, null);

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

            //TODO:This one for translation verse search.
            int eventType2 = transParser.getEventType();

            index = null;
            verseFound = false;

            // Loop through the xml document
            while (eventType2 != XmlPullParser.END_DOCUMENT) {
                if (eventType2 == XmlPullParser.START_TAG) {
                    if (!transParser.getName().equals(XML_ROOT_STRING_1) &&
                            transParser.getName().equals(XML_ROOT_SEARCH_1)) {
                        currentTag = transParser.getName();

                        String suraName = transParser.getAttributeValue(null, "name");

                        if (suraName.equals(sura)) {
                            while (!verseFound) {
                                currentTag = transParser.getName();
                                eventType2 = transParser.nextTag();
                                index = transParser.getAttributeValue(null, "index");
                                if (Integer.parseInt(index) == verseNum) {
                                    translationString = transParser.getAttributeValue(null, "text");
                                    verseFound = true;
                                }
                                if (eventType2 == XmlPullParser.END_DOCUMENT)
                                    break;
                            }
                        }
                    }
                }
                else if(eventType2 == XmlPullParser.END_TAG) {
                    currentTag = null;
                }
                if (eventType2 == XmlPullParser.END_DOCUMENT)
                    break;
                eventType2 = transParser.next();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}