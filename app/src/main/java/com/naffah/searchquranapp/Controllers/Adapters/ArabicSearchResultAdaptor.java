package com.naffah.searchquranapp.Controllers.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.naffah.searchquranapp.R;

import java.util.ArrayList;

public class ArabicSearchResultAdaptor extends RecyclerView.Adapter<ArabicSearchResultAdaptor.MyViewHolder> {

    ArrayList<String> ayaList;
    ArrayList<String> transList;
    ArrayList<String> suraIndex;
    ArrayList<String> ayaIndex;

    public ArabicSearchResultAdaptor(ArrayList<String> ayaList, ArrayList<String> transList,
                                     ArrayList<String> suraIndex, ArrayList<String> ayaIndex){
        this.ayaList = ayaList;
        this.transList = transList;
        this.suraIndex = suraIndex;
        this.ayaIndex = ayaIndex;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.arabic_search_list_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.arabicText.setText(ayaList.get(i));
        myViewHolder.engText.setText(transList.get(i));

        myViewHolder.index.setText(suraIndex.get(i));
        myViewHolder.index.append(" : " + ayaIndex.get(i));
    }

    @Override
    public int getItemCount() {
        return ayaList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView arabicText;
        private  TextView engText;
        private TextView index;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            arabicText = itemView.findViewById(R.id.ayaTextArabic);
            engText = itemView.findViewById(R.id.ayaTextEn);
            index = itemView.findViewById(R.id.index);
        }
    }
}


