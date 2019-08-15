package com.naffah.searchquranapp.Controllers.Activities.IndexSearch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.naffah.searchquranapp.Models.Bookmarks;
import com.naffah.searchquranapp.Models.BookmarksDatabase;
import com.naffah.searchquranapp.R;

import java.io.IOException;
import java.util.List;

import androidx.room.Room;

public class IndexSearchResultActivity extends AppCompatActivity {

    private String url = "http://everyayah.com/data/Abdullah_Basfar_32kbps/00X00Y.mp3";
    private String initialUrl, finalUrl;
    boolean isPlaying = false;

    private static final String DATABASE_NAME = "bookmarks_db";
    private BookmarksDatabase bookmarksDatabase;
    private List<Bookmarks> alreadyBookmarked;
    private ImageButton bookmarkBtn, bookmarkedBtn, playBtn;
    private MediaPlayer mediaPlayer;
    private String index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_search_result);

        TextView indexText = (TextView) findViewById(R.id.indexTextView);
        TextView verseText = (TextView) findViewById(R.id.verseTextView);
        TextView transText = (TextView) findViewById(R.id.translationTextView);
        bookmarkBtn = (ImageButton) findViewById(R.id.bookmarkVerseBtn);
        bookmarkedBtn = (ImageButton) findViewById(R.id.bookmarkedBtn);
        playBtn = (ImageButton) findViewById(R.id.playBtn);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        String check = pref.getString("translation",null);
        if(check != null && check.contains("ur.")){
            transText.setGravity(Gravity.RIGHT);
            transText.setTextSize(20);
        }

        bookmarksDatabase = Room.databaseBuilder(getApplicationContext(), BookmarksDatabase.class, DATABASE_NAME).build();

        bookmarkedBtn.setVisibility(View.GONE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                alreadyBookmarked = bookmarksDatabase.daoAccess().fetchBookmarks();

                for(int j = 0; j < alreadyBookmarked.size(); j++){
                    if(index.equals(alreadyBookmarked.get(j).getVerseIndex())){
                        bookmarkedBtn.setVisibility(View.VISIBLE);
                    }
                }
            }
        }).start();

        Intent verse = getIntent();

        String suraNo = verse.getStringExtra("suraNo");
        String verseNo = verse.getStringExtra("verseNo");
        index = suraNo + ":" + verseNo;
        indexText.setText(index);

        final String verseStr = (String) verse.getSerializableExtra("verse");
        verseText.setText(verseStr);
        verseText.append("o  ");

        final String transStr = (String) verse.getSerializableExtra("translation");
        transText.setText(transStr);

        bookmarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mContext,versesArabic.get(i),Toast.LENGTH_SHORT).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Bookmarks bookmark =new Bookmarks();
                        bookmark.setVerseIndex(index);
                        bookmark.setVerseArabic(verseStr);
                        bookmark.setVerseEnglish(transStr);
                        bookmarksDatabase.daoAccess ().insertSingleBookmark (bookmark);
                    }
                }).start();
                bookmarkedBtn.setVisibility(View.VISIBLE);
            }
        });

        bookmarkedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<Bookmarks> bookmarksList = bookmarksDatabase.daoAccess().fetchBookmarks();
                        for(int i = 0; i < bookmarksList.size(); i++) {
                            if(index.equals(bookmarksList.get(i).getVerseIndex())) {
                                bookmarksDatabase.daoAccess().deleteBookmark(bookmarksList.get(i));
                            }
                        }
                    }
                }).start();
                bookmarkedBtn.setVisibility(View.GONE);
            }
        });

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        setInitialUrl(suraNo);
        setFinalUrl(verseNo);

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isPlaying && mediaPlayer!= null) {
                    try {
                        mediaPlayer.setDataSource(finalUrl);
                        mediaPlayer.prepareAsync();
                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                mp.start();
                            }
                        });
                        isPlaying = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                isPlaying = false;
                mp.reset();
            }
        });
    }

    private void setInitialUrl(String suraNum){

        if (Integer.parseInt(suraNum) < 10){
            initialUrl = url.replace("X",suraNum);
        }
        else if(Integer.parseInt(suraNum) > 9 && Integer.parseInt(suraNum) < 100){
            initialUrl = url.replace("0X",suraNum);
        }
        else {
            initialUrl = url.replace("00X",suraNum);
        }
    }

    private void setFinalUrl(String verseNum){

        if(Integer.parseInt(verseNum) < 10) {
            finalUrl = initialUrl.replace("Y", verseNum);
        }
        else if(Integer.parseInt(verseNum) > 9 && Integer.parseInt(verseNum) < 100) {
            finalUrl = initialUrl.replace("0Y", verseNum);
        }
        else {
            finalUrl = initialUrl.replace("00Y", verseNum);
        }
    }
}
