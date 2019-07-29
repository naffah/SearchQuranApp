package com.naffah.searchquranapp.Controllers.Activities.Bookmarks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.naffah.searchquranapp.Controllers.Adapters.BookmarksListAdaptor;
import com.naffah.searchquranapp.Models.Bookmarks;
import com.naffah.searchquranapp.Models.BookmarksDatabase;
import com.naffah.searchquranapp.R;

import java.util.List;

import androidx.room.Room;

public class BookmarksListActivity extends AppCompatActivity {

    private static final String DATABASE_NAME = "bookmarks_db";
    private BookmarksDatabase bookmarksDatabase;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    List<Bookmarks> bookmarksList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks_list);

        recyclerView = (RecyclerView) findViewById(R.id.bookmarks_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        bookmarksDatabase = Room.databaseBuilder(getApplicationContext(), BookmarksDatabase.class, DATABASE_NAME).build();

        new Thread(new Runnable() {
            @Override
            public void run() {
                bookmarksList = bookmarksDatabase.daoAccess().fetchBookmarks();
                // specify an adapter (see also next example)
                mAdapter = new BookmarksListAdaptor(getApplicationContext(), bookmarksList);
                recyclerView.setAdapter(mAdapter);
            }
        }).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bookmarksDatabase.close();
    }
}