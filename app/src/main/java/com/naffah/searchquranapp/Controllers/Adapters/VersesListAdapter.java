package com.naffah.searchquranapp.Controllers.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.naffah.searchquranapp.Models.Bookmarks;
import com.naffah.searchquranapp.Models.BookmarksDatabase;
import com.naffah.searchquranapp.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.room.Room;

public class VersesListAdapter extends RecyclerView.Adapter<VersesListAdapter.ViewHolder> {

    private String url = "http://everyayah.com/data/Abdullah_Basfar_32kbps/00X00Y.mp3";
    private String initialUrl, finalUrl;
    boolean isPlaying = false;
    private MediaPlayer mediaPlayer;

    private static final String DATABASE_NAME = "bookmarks_db";
    private BookmarksDatabase bookmarksDatabase;
    private List<Bookmarks> alreadyBookmarked;

    private String suraNum = null;
    private ArrayList<String> versesEn = new ArrayList<>();
    private ArrayList<String> versesArabic = new ArrayList<>();
    private ArrayList<String> versesNum = new ArrayList<>();
    private Context mContext;

    public VersesListAdapter(Context mContext, String suraNum, ArrayList<String> versesEn, ArrayList<String> versesArabic, ArrayList<String> versesNum) {
        this.suraNum = suraNum;
        this.versesEn = versesEn;
        this.versesArabic = versesArabic;
        this.versesNum = versesNum;
        this.mContext = mContext;

        bookmarksDatabase = Room.databaseBuilder(mContext, BookmarksDatabase.class, DATABASE_NAME).build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                alreadyBookmarked = bookmarksDatabase.daoAccess().fetchBookmarks();
            }
        }).start();
    }

    @NonNull
    @Override
    public VersesListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_verses_list_item, parent, false);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        VersesListAdapter.ViewHolder holder = new VersesListAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final VersesListAdapter.ViewHolder viewHolder, final int i) {
        SharedPreferences pref = mContext.getSharedPreferences("MyPref", 0);
        String check = pref.getString("translation",null);
        if(check != null && check.contains("ur.")){
            viewHolder.versesEn.setGravity(Gravity.RIGHT);
            viewHolder.versesEn.setTextSize(20);
        }

        viewHolder.versesEn.setText(versesEn.get(i));
        viewHolder.versesArabic.setText(versesArabic.get(i));
        viewHolder.versesNum.setText(versesNum.get(i));

        final String Index = suraNum + ":" + versesNum.get(i);

        viewHolder.bookmarkedBtn.setVisibility(View.GONE);
        for(int j = 0; j < alreadyBookmarked.size(); j++){
            if(Index.equals(alreadyBookmarked.get(j).getVerseIndex())){
                viewHolder.bookmarkedBtn.setVisibility(View.VISIBLE);
            }
        }

        viewHolder.bookmarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mContext,versesArabic.get(i),Toast.LENGTH_SHORT).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Bookmarks bookmark =new Bookmarks();
                        bookmark.setVerseIndex(Index);
                        bookmark.setVerseArabic(versesArabic.get(i));
                        bookmark.setVerseEnglish(versesEn.get(i));
                        bookmarksDatabase.daoAccess ().insertSingleBookmark (bookmark);
                    }
                }).start();
                viewHolder.bookmarkedBtn.setVisibility(View.VISIBLE);
            }
        });

        viewHolder.bookmarkedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<Bookmarks> bookmarksList = bookmarksDatabase.daoAccess().fetchBookmarks();
                        for(int j = 0; j < bookmarksList.size(); j++) {
                            if(Index.equals(bookmarksList.get(j).getVerseIndex())) {
                                bookmarksDatabase.daoAccess().deleteBookmark(bookmarksList.get(j));
                            }
                        }
                    }
                }).start();
                viewHolder.bookmarkedBtn.setVisibility(View.GONE);
            }
        });

        setInitialUrl();
        viewHolder.playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        return versesEn.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView versesEn, versesArabic, versesNum;
        private ImageButton bookmarkBtn, bookmarkedBtn, playBtn;
        private ConstraintLayout versesLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            versesEn = itemView.findViewById(R.id.verseTextEn);
            versesArabic = itemView.findViewById(R.id.verseTextArabic);
            versesNum = itemView.findViewById(R.id.verseNumber);
            bookmarkBtn = itemView.findViewById(R.id.bookmarkVerseBtn);
            bookmarkedBtn = itemView.findViewById(R.id.bookmarkedBtn);
            playBtn = itemView.findViewById(R.id.playBtn);
            versesLayout = itemView.findViewById(R.id.verseItemLayout);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private void setInitialUrl(){

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

    private void setFinalUrl(int i){

        if(Integer.parseInt(versesNum.get(i)) < 10) {
            finalUrl = initialUrl.replace("Y", versesNum.get(i));
        }
        else if(Integer.parseInt(versesNum.get(i)) > 9 && Integer.parseInt(versesNum.get(i)) < 100) {
            finalUrl = initialUrl.replace("0Y", versesNum.get(i));
        }
        else {
            finalUrl = initialUrl.replace("00Y", versesNum.get(i));
        }
    }
}
