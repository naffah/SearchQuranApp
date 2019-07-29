package com.naffah.searchquranapp.Controllers.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.naffah.searchquranapp.Models.Bookmarks;
import com.naffah.searchquranapp.Models.BookmarksDatabase;
import com.naffah.searchquranapp.R;

import java.util.ArrayList;
import java.util.List;

import androidx.room.Room;

public class ArabicSearchResultAdaptor extends RecyclerView.Adapter<ArabicSearchResultAdaptor.MyViewHolder> {

    ArrayList<String> ayaList;
    ArrayList<String> transList;
    ArrayList<String> suraIndex;
    ArrayList<String> ayaIndex;

    private static final String DATABASE_NAME = "bookmarks_db";
    private BookmarksDatabase bookmarksDatabase;
    private List<Bookmarks> alreadyBookmarked;

    public ArabicSearchResultAdaptor(Context context,ArrayList<String> ayaList, ArrayList<String> transList,
                                     ArrayList<String> suraIndex, ArrayList<String> ayaIndex){
        this.ayaList = ayaList;
        this.transList = transList;
        this.suraIndex = suraIndex;
        this.ayaIndex = ayaIndex;

        bookmarksDatabase = Room.databaseBuilder(context, BookmarksDatabase.class, DATABASE_NAME).build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                alreadyBookmarked = bookmarksDatabase.daoAccess().fetchBookmarks();
            }
        }).start();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.arabic_search_list_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        myViewHolder.arabicText.setText(ayaList.get(i));
        myViewHolder.engText.setText(transList.get(i));

        final String Index = suraIndex.get(i) + ":" + ayaIndex.get(i);
        myViewHolder.index.setText(Index);

        for(int j = 0; j < alreadyBookmarked.size(); j++){
            if(Index.equals(alreadyBookmarked.get(j).getVerseIndex())){
                myViewHolder.bookmarkBtn.setVisibility(View.GONE);
            }
        }

        myViewHolder.bookmarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mContext,versesArabic.get(i),Toast.LENGTH_SHORT).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Bookmarks bookmark =new Bookmarks();
                        bookmark.setVerseIndex(Index);
                        bookmark.setVerseArabic(ayaList.get(i));
                        bookmark.setVerseEnglish(transList.get(i));
                        bookmarksDatabase.daoAccess ().insertSingleBookmark (bookmark);
                    }
                }).start();
                myViewHolder.bookmarkBtn.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ayaList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView arabicText, engText, index;
        private ImageButton bookmarkBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            arabicText = itemView.findViewById(R.id.ayaTextArabic);
            engText = itemView.findViewById(R.id.ayaTextEn);
            index = itemView.findViewById(R.id.index);
            bookmarkBtn = itemView.findViewById(R.id.bookmarkVerseBtn);
        }
    }
}


