package com.naffah.searchquranapp.Controllers.Adapters;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.room.Room;

public class ArabicSearchResultAdaptor extends RecyclerView.Adapter<ArabicSearchResultAdaptor.MyViewHolder> {

    private String url = "http://everyayah.com/data/Abdullah_Basfar_32kbps/00X00Y.mp3";
    private String initialUrl, finalUrl;
    boolean isPlaying = false;
    private MediaPlayer mediaPlayer;

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

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        myViewHolder.arabicText.setText(ayaList.get(i));
        myViewHolder.engText.setText(transList.get(i));

        final String Index = suraIndex.get(i) + ":" + ayaIndex.get(i);
        myViewHolder.index.setText(Index);

        myViewHolder.bookmarkedBtn.setVisibility(View.GONE);
        for(int j = 0; j < alreadyBookmarked.size(); j++){
            if(Index.equals(alreadyBookmarked.get(j).getVerseIndex())){
                myViewHolder.bookmarkedBtn.setVisibility(View.VISIBLE);
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
                myViewHolder.bookmarkedBtn.setVisibility(View.VISIBLE);
            }
        });


        myViewHolder.playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setInitialUrl(i);
                setFinalUrl(i);
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

    @Override
    public int getItemCount() {
        return ayaList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView arabicText, engText, index;
        private ImageButton bookmarkBtn, bookmarkedBtn, playBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            arabicText = itemView.findViewById(R.id.ayaTextArabic);
            engText = itemView.findViewById(R.id.ayaTextEn);
            index = itemView.findViewById(R.id.index);
            bookmarkBtn = itemView.findViewById(R.id.bookmarkVerseBtn);
            bookmarkedBtn = itemView.findViewById(R.id.bookmarkedBtn);
            playBtn = itemView.findViewById(R.id.playBtn);
        }
    }

    private void setInitialUrl(int i){

        if (Integer.parseInt(suraIndex.get(i)) < 10){
            initialUrl = url.replace("X",suraIndex.get(i));
        }
        else if(Integer.parseInt(suraIndex.get(i)) > 9 && Integer.parseInt(suraIndex.get(i)) < 100){
            initialUrl = url.replace("0X",suraIndex.get(i));
        }
        else {
            initialUrl = url.replace("00X",suraIndex.get(i));
        }
    }

    private void setFinalUrl(int i){

        if(Integer.parseInt(ayaIndex.get(i)) < 10) {
            finalUrl = initialUrl.replace("Y", ayaIndex.get(i));
        }
        else if(Integer.parseInt(ayaIndex.get(i)) > 9 && Integer.parseInt(ayaIndex.get(i)) < 100) {
            finalUrl = initialUrl.replace("0Y", ayaIndex.get(i));
        }
        else {
            finalUrl = initialUrl.replace("00Y", ayaIndex.get(i));
        }
    }
}


