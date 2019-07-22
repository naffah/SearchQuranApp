package com.naffah.searchquranapp.Controllers.Activities.WordSearch;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.naffah.searchquranapp.Controllers.Adapters.RootWordListAdaptor;
import com.naffah.searchquranapp.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class SearchByRootWordActivity extends AppCompatActivity {

    ArrayList<String> rootList;
    String tag;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_root_word);

        rootList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.root_words_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new RightToLeftGridLayoutManager(this,3);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new RootWordListAdaptor(this,rootList);
        recyclerView.setAdapter(mAdapter);
    }

    public void ButtonClicked(View view) {

        Button button;

        switch(view.getId())
        {
            case R.id.aBtn:
            // handle button A click;
                rootList.clear();

                button = (Button) findViewById(R.id.aBtn);
                tag = (String) button.getHint();
                func(tag);

                mAdapter.notifyDataSetChanged();
                break;
            case R.id.bBtn:
            // handle button B click;
                rootList.clear();

                button = (Button) findViewById(R.id.bBtn);
                tag = (String) button.getHint();
                func(tag);

                mAdapter.notifyDataSetChanged();
                break;
            case R.id.tBtn:
                // handle button B click;
                rootList.clear();

                button = (Button) findViewById(R.id.tBtn);
                tag = (String) button.getHint();
                func(tag);

                mAdapter.notifyDataSetChanged();
                break;
            case R.id.thBtn:
                // handle button B click;
                rootList.clear();

                button = (Button) findViewById(R.id.thBtn);
                tag = (String) button.getHint();
                func(tag);

                mAdapter.notifyDataSetChanged();
                break;
            case R.id.jBtn:
                // handle button B click;
                rootList.clear();

                button = (Button) findViewById(R.id.jBtn);
                tag = (String) button.getHint();
                func(tag);

                mAdapter.notifyDataSetChanged();
                break;
            case R.id.hBtn:
                // handle button B click;
                rootList.clear();

                button = (Button) findViewById(R.id.hBtn);
                tag = (String) button.getHint();
                func(tag);

                mAdapter.notifyDataSetChanged();
                break;
            case R.id.khBtn:
                // handle button B click;
                rootList.clear();

                button = (Button) findViewById(R.id.khBtn);
                tag = (String) button.getHint();
                func(tag);

                mAdapter.notifyDataSetChanged();
                break;
            case R.id.dBtn:
                // handle button B click;
                rootList.clear();

                button = (Button) findViewById(R.id.dBtn);
                tag = (String) button.getHint();
                func(tag);

                mAdapter.notifyDataSetChanged();
                break;
            case R.id.dhBtn:
                // handle button B click;
                rootList.clear();

                button = (Button) findViewById(R.id.dhBtn);
                tag = (String) button.getHint();
                func(tag);

                mAdapter.notifyDataSetChanged();
                break;
            case R.id.rBtn:
                // handle button B click;
                rootList.clear();

                button = (Button) findViewById(R.id.rBtn);
                tag = (String) button.getHint();
                func(tag);

                mAdapter.notifyDataSetChanged();
                break;
            case R.id.zBtn:
                // handle button B click;
                rootList.clear();

                button = (Button) findViewById(R.id.zBtn);
                tag = (String) button.getHint();
                func(tag);

                mAdapter.notifyDataSetChanged();
                break;
            case R.id.sBtn:
                // handle button B click;
                rootList.clear();

                button = (Button) findViewById(R.id.sBtn);
                tag = (String) button.getHint();
                func(tag);

                mAdapter.notifyDataSetChanged();
                break;
            case R.id.shBtn:
                // handle button B click;
                rootList.clear();

                button = (Button) findViewById(R.id.shBtn);
                tag = (String) button.getHint();
                func(tag);

                mAdapter.notifyDataSetChanged();
                break;
            case R.id.sdBtn:
                // handle button B click;
                rootList.clear();

                button = (Button) findViewById(R.id.sdBtn);
                tag = (String) button.getHint();
                func(tag);

                mAdapter.notifyDataSetChanged();
                break;
            case R.id.zdBtn:
                // handle button B click;
                rootList.clear();

                button = (Button) findViewById(R.id.zdBtn);
                tag = (String) button.getHint();
                func(tag);

                mAdapter.notifyDataSetChanged();
                break;
            case R.id.teBtn:
                // handle button B click;
                rootList.clear();

                button = (Button) findViewById(R.id.teBtn);
                tag = (String) button.getHint();
                func(tag);

                mAdapter.notifyDataSetChanged();
                break;
            case R.id.zeBtn:
                // handle button B click;
                rootList.clear();

                button = (Button) findViewById(R.id.zeBtn);
                tag = (String) button.getHint();
                func(tag);

                mAdapter.notifyDataSetChanged();
                break;
            case R.id.ainBtn:
                // handle button B click;
                rootList.clear();

                button = (Button) findViewById(R.id.ainBtn);
                tag = (String) button.getHint();
                func(tag);

                mAdapter.notifyDataSetChanged();
                break;
            case R.id.ghBtn:
                // handle button B click;
                rootList.clear();

                button = (Button) findViewById(R.id.ghBtn);
                tag = (String) button.getHint();
                func(tag);

                mAdapter.notifyDataSetChanged();
                break;
            case R.id.fBtn:
                // handle button B click;
                rootList.clear();

                button = (Button) findViewById(R.id.fBtn);
                tag = (String) button.getHint();
                func(tag);

                mAdapter.notifyDataSetChanged();
                break;
            case R.id.qBtn:
                // handle button B click;
                rootList.clear();

                button = (Button) findViewById(R.id.qBtn);
                tag = (String) button.getHint();
                func(tag);

                mAdapter.notifyDataSetChanged();
                break;
            case R.id.kBtn:
                // handle button B click;
                rootList.clear();

                button = (Button) findViewById(R.id.kBtn);
                tag = (String) button.getHint();
                func(tag);

                mAdapter.notifyDataSetChanged();
                break;
            case R.id.lBtn:
                // handle button B click;
                rootList.clear();

                button = (Button) findViewById(R.id.lBtn);
                tag = (String) button.getHint();
                func(tag);

                mAdapter.notifyDataSetChanged();
                break;
            case R.id.mBtn:
                // handle button B click;
                rootList.clear();

                button = (Button) findViewById(R.id.mBtn);
                tag = (String) button.getHint();
                func(tag);

                mAdapter.notifyDataSetChanged();
                break;
            case R.id.nBtn:
                // handle button B click;
                rootList.clear();

                button = (Button) findViewById(R.id.nBtn);
                tag = (String) button.getHint();
                func(tag);

                mAdapter.notifyDataSetChanged();
                break;
            case R.id.heBtn:
                // handle button B click;
                rootList.clear();

                button = (Button) findViewById(R.id.heBtn);
                tag = (String) button.getHint();
                func(tag);

                mAdapter.notifyDataSetChanged();
                break;
            case R.id.wBtn:
                // handle button B click;
                rootList.clear();

                button = (Button) findViewById(R.id.wBtn);
                tag = (String) button.getHint();
                func(tag);

                mAdapter.notifyDataSetChanged();
                break;
            case R.id.yBtn:
                // handle button B click;
                rootList.clear();

                button = (Button) findViewById(R.id.yBtn);
                tag = (String) button.getHint();
                func(tag);

                mAdapter.notifyDataSetChanged();
                break;
            default:
                throw new RuntimeException("Unknown button ID");
        }
    }

    public boolean func(String search){
        boolean returnValue = false;

        XmlPullParserFactory xmlFactoryObject = null;
        try {
            xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myparser = xmlFactoryObject.newPullParser();

            InputStream in_s = getApplicationContext().getAssets().open("database/root-words.xml");
            myparser.setInput(in_s, null);

            boolean flag1 = false;
            boolean flag2 = false;

            int event = myparser.getEventType();

            while (event != XmlPullParser.END_DOCUMENT)  {

                String name=myparser.getName();
                switch (event){
                    case XmlPullParser.START_TAG:
                        if(name.equals(search)){
                            flag1 = true;
                            returnValue = true;
                        }
                        if(name.equals("word")){
                            flag2 = true;
                        }
                        break;

                    case XmlPullParser.TEXT:
                        if(flag1 && flag2){
                            rootList.add(myparser.getText());
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if(name.equals(search)){
                            flag1 = false;
                        }
                        if(name.equals("word")){
                            flag2 = false;
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

    public class RightToLeftGridLayoutManager extends GridLayoutManager {

        public RightToLeftGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        public RightToLeftGridLayoutManager(Context context, int spanCount) {
            super(context, spanCount);
        }

        public RightToLeftGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
            super(context, spanCount, orientation, reverseLayout);
        }

        @Override
        protected boolean isLayoutRTL(){
            return true;
        }
    }
}

