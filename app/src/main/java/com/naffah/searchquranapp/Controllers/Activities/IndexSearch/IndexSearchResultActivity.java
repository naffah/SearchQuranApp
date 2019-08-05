package com.naffah.searchquranapp.Controllers.Activities.IndexSearch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.naffah.searchquranapp.Models.Bookmarks;
import com.naffah.searchquranapp.Models.BookmarksDatabase;
import com.naffah.searchquranapp.R;

import java.util.List;

import androidx.room.Room;

public class IndexSearchResultActivity extends AppCompatActivity {

    private static final String DATABASE_NAME = "bookmarks_db";
    private BookmarksDatabase bookmarksDatabase;
    private List<Bookmarks> alreadyBookmarked;
    private ImageButton bookmarkBtn, bookmarkedBtn;
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
    }
}
