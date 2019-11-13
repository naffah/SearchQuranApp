package com.naffah.searchquranapp.Controllers.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.naffah.searchquranapp.Controllers.Activities.WordSearch.RootSearchResultActivity;
import com.naffah.searchquranapp.R;

import java.util.ArrayList;

public class RootWordListAdaptor extends RecyclerView.Adapter<RootWordListAdaptor.MyViewHolder> {

    ArrayList<String> rootList;
    Context context;

    public RootWordListAdaptor(Context context, ArrayList<String> rootList){
        this.context = context;
        this.rootList = rootList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.root_word_list_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        myViewHolder.textView.setText(rootList.get(i));

        myViewHolder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,rootList.get(i),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, RootSearchResultActivity.class);
                intent.putExtra("searchWord",rootList.get(i));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return rootList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ConstraintLayout constraintLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.rootText);
            constraintLayout = itemView.findViewById(R.id.rootLayout);
        }
    }
}

