package com.naffah.searchquranapp.Controllers.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.naffah.searchquranapp.Models.Bookmarks;
import com.naffah.searchquranapp.Models.BookmarksDatabase;
import com.naffah.searchquranapp.R;

import java.util.List;

import androidx.room.Room;

public class BookmarksListAdaptor extends RecyclerView.Adapter<BookmarksListAdaptor.MyViewHolder> {

    static final String DATABASE_NAME = "bookmarks_db";
    BookmarksDatabase bookmarksDatabase;
    List<Bookmarks> bookmarksList;

    public BookmarksListAdaptor(Context context, List<Bookmarks> bookmarksList){
        this.bookmarksList = bookmarksList;
        bookmarksDatabase = Room.databaseBuilder(context, BookmarksDatabase.class, DATABASE_NAME).build();
    }

    @NonNull
    @Override
    public BookmarksListAdaptor.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bookmark_list_item, viewGroup, false);
        BookmarksListAdaptor.MyViewHolder holder = new BookmarksListAdaptor.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final BookmarksListAdaptor.MyViewHolder myViewHolder, final int i) {
        myViewHolder.verseArabic.setText(bookmarksList.get(i).getVerseArabic());
        myViewHolder.verseEnglish.setText(bookmarksList.get(i).getVerseEnglish());
        myViewHolder.index.setText(bookmarksList.get(i).getVerseIndex());

        myViewHolder.removeBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        bookmarksDatabase.daoAccess().deleteBookmark(bookmarksList.get(i));
                    }
                }).start();
                myViewHolder.innerLayout.setVisibility(View.GONE);
                myViewHolder.outerLayout.setPadding(0,0,0,0);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookmarksList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView index, verseArabic, verseEnglish;
        ImageButton removeBookmark;
        ConstraintLayout innerLayout, outerLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            index = itemView.findViewById(R.id.index);
            verseArabic = itemView.findViewById(R.id.ayaTextArabic);
            verseEnglish = itemView.findViewById(R.id.ayaTextEn);
            removeBookmark = itemView.findViewById(R.id.removeBookmarkBtn);
            innerLayout = itemView.findViewById(R.id.innerLayout);
            outerLayout = itemView.findViewById(R.id.outerLayout);
        }
    }
}
