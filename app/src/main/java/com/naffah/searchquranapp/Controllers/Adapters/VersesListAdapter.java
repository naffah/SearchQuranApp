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
import android.widget.Toast;

import com.naffah.searchquranapp.Models.Bookmarks;
import com.naffah.searchquranapp.Models.BookmarksDatabase;
import com.naffah.searchquranapp.R;

import java.util.ArrayList;
import java.util.List;

import androidx.room.Room;

public class VersesListAdapter extends RecyclerView.Adapter<VersesListAdapter.ViewHolder> {

    public static final String TAG = "VersesListAdapter";

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
        VersesListAdapter.ViewHolder holder = new VersesListAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final VersesListAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.versesEn.setText(versesEn.get(i));
        viewHolder.versesArabic.setText(versesArabic.get(i));
        viewHolder.versesNum.setText(versesNum.get(i));

        final String Index = suraNum + ":" + versesNum.get(i);

        for(int j = 0; j < alreadyBookmarked.size(); j++){
            if(Index.equals(alreadyBookmarked.get(j).getVerseIndex())){
                viewHolder.bookmarkBtn.setVisibility(View.GONE);
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
                viewHolder.bookmarkBtn.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return versesEn.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView versesEn, versesArabic, versesNum;
        private ImageButton bookmarkBtn;
        private ConstraintLayout versesLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            versesEn = itemView.findViewById(R.id.verseTextEn);
            versesArabic = itemView.findViewById(R.id.verseTextArabic);
            versesNum = itemView.findViewById(R.id.verseNumber);
            bookmarkBtn = itemView.findViewById(R.id.bookmarkVerseBtn);
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
}
