package com.naffah.searchquranapp.Controllers.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.naffah.searchquranapp.Controllers.Activities.VersesList.VersesListActivity;
import com.naffah.searchquranapp.Models.Bookmarks;
import com.naffah.searchquranapp.Models.ProjectDatabase;
import com.naffah.searchquranapp.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import androidx.room.Room;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class BookmarksListAdaptor extends RecyclerView.Adapter<BookmarksListAdaptor.MyViewHolder> {

    static final String XML_ROOT_STRING_1 = "quran";
    static final String XML_ROOT_STRING_2 = "suras";
    static final String XML_ROOT_SEARCH_1 = "sura";
    String suraNum, suraName, suraNameEn;
    String currentTag = null;
    int suraAyasTotal;

    static final String DATABASE_NAME = "bookmarks_db";
    ProjectDatabase projectDatabase;
    List<Bookmarks> bookmarksList;
    Context context;

    public BookmarksListAdaptor(Context context, List<Bookmarks> bookmarksList){
        this.context = context;
        this.bookmarksList = bookmarksList;
        projectDatabase = Room.databaseBuilder(context, ProjectDatabase.class, DATABASE_NAME).build();
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
                        projectDatabase.daoAccess().deleteBookmark(bookmarksList.get(i));
                    }
                }).start();
                myViewHolder.innerLayout.setVisibility(View.GONE);
                myViewHolder.outerLayout.setPadding(0,0,0,0);
            }
        });
        myViewHolder.innerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String index = bookmarksList.get(i).getVerseIndex();
                final String[] arrOfStr = index.split(":", 2);
                int position = Integer.parseInt(arrOfStr[1]);
                initSuraDetails(arrOfStr[0]);

                Intent gotoVersesList = new Intent(context, VersesListActivity.class);
                gotoVersesList.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                gotoVersesList.putExtra("suraNum",suraNum);
                gotoVersesList.putExtra("suraName", suraName);
                gotoVersesList.putExtra("suraNameEn", suraNameEn);
                gotoVersesList.putExtra("totalVerses", suraAyasTotal);
                gotoVersesList.putExtra("layoutPosition", position - 1);
                context.startActivity(gotoVersesList);
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

    private void initSuraDetails(String num) {
        XmlPullParserFactory suraParserFactory;

        try {
            suraParserFactory = XmlPullParserFactory.newInstance();
            suraParserFactory.setNamespaceAware(true);
            XmlPullParser suraParser = suraParserFactory.newPullParser();
            InputStream is = context.getAssets().open("database/quran-metadata.xml");

            suraParser.setInput(is, null);

            int eventType = suraParser.getEventType();

            // Loop through the xml document
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if (!suraParser.getName().equals(XML_ROOT_STRING_1) &&
                            !suraParser.getName().equals(XML_ROOT_STRING_2) &&
                            suraParser.getName().equals(XML_ROOT_SEARCH_1)) {
                        currentTag = suraParser.getName();

                        suraNum = suraParser.getAttributeValue(null,"index");
                        if(suraNum.equals(num)) {
                            suraName = suraParser.getAttributeValue(null, "name");
                            suraNameEn = suraParser.getAttributeValue(null, "tname");
                            suraAyasTotal = Integer.parseInt(suraParser.getAttributeValue(null, "ayas"));
                            break;
                        }
                    }
                }
                else if(eventType == XmlPullParser.END_TAG) {
                    currentTag = null;
                }
                if (eventType == XmlPullParser.END_DOCUMENT)
                    break;
                eventType = suraParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
