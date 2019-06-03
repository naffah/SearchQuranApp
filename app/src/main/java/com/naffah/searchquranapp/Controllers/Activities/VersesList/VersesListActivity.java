package com.naffah.searchquranapp.Controllers.Activities.VersesList;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.naffah.searchquranapp.Controllers.Adapters.VersesListAdapter;
import com.naffah.searchquranapp.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class VersesListActivity extends AppCompatActivity {

    public static final String XML_ROOT_STRING_1 = "quran";
    public static final String XML_ROOT_SEARCH_1 = "sura";

    String currentTag = null;

    private ArrayList<String> versesEn = new ArrayList<>();
    private ArrayList<String> versesArabic = new ArrayList<>();
    private ArrayList<String> versesNum = new ArrayList<>();

    private String suraName = null;
    private int versesCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verses_list);

        Intent suraListIntent = getIntent();
        suraName = (String) suraListIntent.getSerializableExtra("suraName");
        versesCount = (int) suraListIntent.getSerializableExtra("totalVerses");

        versesEn.clear();
        versesArabic.clear();
        versesNum.clear();

        String arabicInput = "database/quran-simple.xml";
        initVerses(arabicInput);
        String englishInput = "database/english-translations/en.ahmedali.xml";
        initVerses(englishInput);
        initVersesRecyclerView();
    }

    private void initVerses(String input) {

        int count = versesCount;
        XmlPullParserFactory versesParserFactory;

        try {
            versesParserFactory = XmlPullParserFactory.newInstance();
            versesParserFactory.setNamespaceAware(true);
            XmlPullParser versesParser = versesParserFactory.newPullParser();
            InputStream is = getAssets().open(input);

            versesParser.setInput(is, null);

            int eventType = versesParser.getEventType();

            // Loop through the xml document
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if (!versesParser.getName().equals(XML_ROOT_STRING_1) &&
                            versesParser.getName().equals(XML_ROOT_SEARCH_1)) {
                        currentTag = versesParser.getName();

                        String name = versesParser.getAttributeValue(null, "name");

                        if (suraName.equals(name)) {
                            while (count > 0) {
                                if (input.equals("database/quran-simple.xml")) {
                                    versesParser.nextTag();
                                    versesArabic.add(versesParser.getAttributeValue(null, "text"));
                                    versesParser.nextTag();
                                }
                                else if (input.equals("database/english-translations/en.ahmedali.xml")) {
                                    versesParser.nextTag();
                                    versesEn.add(versesParser.getAttributeValue(null, "text"));
                                    versesParser.nextTag();
                                }
                                if (versesArabic.size() <= versesCount)
                                {
                                    versesNum.add(versesParser.getAttributeValue(null, "index"));
                                }
                                count--;
                            }
                        }
                    }
                }
                else if(eventType == XmlPullParser.END_TAG) {
                    currentTag = null;
                }
                if (eventType == XmlPullParser.END_DOCUMENT)
                    break;
                eventType = versesParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initVersesRecyclerView() {
        RecyclerView rView = findViewById(R.id.verseListView);
        VersesListAdapter adapter = new VersesListAdapter(this.getApplicationContext(), versesEn, versesArabic, versesNum);
        rView.setAdapter(adapter);
        rView.setLayoutManager(new LinearLayoutManager(this));
    }
}
