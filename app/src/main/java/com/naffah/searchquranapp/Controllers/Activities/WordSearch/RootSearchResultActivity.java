package com.naffah.searchquranapp.Controllers.Activities.WordSearch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

public class RootSearchResultActivity extends AppCompatActivity {

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
    SharedPreferences pref;
    String position = null;
    String arrOfStr[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root_search_result);
        textView = (TextView) findViewById(R.id.textView);

        pref = getApplicationContext().getSharedPreferences("MyPref", 0);

        ayaList = new ArrayList<>();
        transList = new ArrayList<>();
        suraIndex = new ArrayList<>();
        ayaIndex = new ArrayList<>();

        Intent verse = getIntent();
        searchString = verse.getStringExtra("searchWord");

        found = func1(searchString);

        if(found){
            if(position != null) {
                arrOfStr = position.split(" ", 0);

                for(int i = 0; i < arrOfStr.length; i++) {
                    String arrofIndices[] = arrOfStr[i].split(":", 0);
                    suraIndex.add(arrofIndices[0]);
                    ayaIndex.add(arrofIndices[1]);
                }

                func2(suraIndex,ayaIndex);
                //textView.setText("Word Found");
                textView.setVisibility(View.GONE);
                recyclerView = (RecyclerView) findViewById(R.id.root_search_result_recycler_view);

                // use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                recyclerView.setHasFixedSize(true);

                // use a linear layout manager
                layoutManager = new LinearLayoutManager(this);
                recyclerView.setLayoutManager(layoutManager);

                // specify an adapter (see also next example)
                mAdapter = new ArabicSearchResultAdaptor(getApplicationContext(), ayaList, transList, suraIndex, ayaIndex);
                recyclerView.setAdapter(mAdapter);
                setTitle("Search Results: " + mAdapter.getItemCount());
            }
        }
        else{
            textView.setText("Word not found");
        }
    }

    public boolean func1(String search){
        boolean returnValue = false;

        XmlPullParserFactory xmlFactoryObject = null;
        try {
            xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myparser = xmlFactoryObject.newPullParser();

            InputStream in_s = getApplicationContext().getAssets().open("database/root-words.xml");
            myparser.setInput(in_s, null);

            boolean flag = false;
            int event = myparser.getEventType();

            while (event != XmlPullParser.END_DOCUMENT && !flag)  {

                String name=myparser.getName();
                switch (event){
                    case XmlPullParser.START_TAG:
                        if((name).equals("word")){
                            position = myparser.getAttributeValue(null,"position");
                        }
                        break;
                    case XmlPullParser.TEXT:
                        if((myparser.getText()).equals(search)){
                            flag = true;
                            returnValue = true;
                        }
                        break;
                }
                event = myparser.next();
            }
            in_s.close();

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnValue;
    }

    public void func2(ArrayList suraIndex, ArrayList ayaIndex){

        XmlPullParserFactory xmlFactoryObject = null;
        try {
            xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myparser = xmlFactoryObject.newPullParser();
            XmlPullParser myparser2 = xmlFactoryObject.newPullParser();

            InputStream in_s = getApplicationContext().getAssets().open("database/quran-simple.xml");
            myparser.setInput(in_s, null);
            InputStream in_s2 = getApplicationContext().getAssets().open
                    (pref.getString("translation", "database/english-translations/en.ahmedali.xml"));
            myparser2.setInput(in_s2, null);

            String aya;
            String aya2;
            String surahIndex = "";
            String ayatIndex;

            int event = myparser.getEventType();
            int event2 = myparser2.getEventType();

            boolean flag1 = false;
            boolean flag2 = false;
            int j = 0;

            while (event != XmlPullParser.END_DOCUMENT)  {
                String name=myparser.getName();
                switch (event){
                    case XmlPullParser.START_TAG:
                        if(name.equals("sura")){
                            flag1 = false;
                            surahIndex = myparser.getAttributeValue(null,"index");
                            if(suraIndex.size() > j && suraIndex.get(j).equals(surahIndex)){
                                flag1 = true;
                            }
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if(name.equals("aya")){

                            ayatIndex = myparser.getAttributeValue(null,"index");
                            if(flag1 && ayaIndex.size() > j && ayaIndex.get(j).equals(ayatIndex)){
                                Log.i("Stefan",suraIndex.get(j) + ":" + ayaIndex.get(j));
                                flag2 = true;
                            }

                            if(flag1 && flag2){
                                aya = myparser.getAttributeValue(null,"text");
                                aya2 = myparser2.getAttributeValue(null,"text");
                                ayaList.add(aya);
                                transList.add(aya2);

                                Log.i("Stefan",ayaList.get(j));
                                j++;
                                //flag1 = false;
                                flag2 = false;
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
    }
}

