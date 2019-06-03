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

import com.naffah.searchquranapp.Controllers.Activities.VersesList.VersesListActivity;
import com.naffah.searchquranapp.R;

import java.util.ArrayList;

public class SuraListAdapter extends RecyclerView.Adapter<SuraListAdapter.ViewHolder> {

    public static final String TAG = "SuraListAdapter";

    private ArrayList<String> suraNamesEn = new ArrayList<>();
    private ArrayList<String> suraNamesArabic = new ArrayList<>();
    private ArrayList<Integer> suraAyasTotal = new ArrayList<>();
    private Context mContext;

    public SuraListAdapter(Context mContext, ArrayList<String> suraNamesEn, ArrayList<String> suraNamesArabic, ArrayList<Integer> suraAyasTotal) {
        this.suraNamesEn = suraNamesEn;
        this.suraNamesArabic = suraNamesArabic;
        this.suraAyasTotal = suraAyasTotal;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_sura_list_scrollable_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.suraItemEn.setText(suraNamesEn.get(i));
        viewHolder.suraItemArabic.setText(suraNamesArabic.get(i));

        viewHolder.suraLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, suraNamesEn.get(i) + " " + suraNamesArabic.get(i), Toast.LENGTH_SHORT).show();
//                Intent gotoVersesList = new Intent(mContext, VersesListActivity.class);
//                gotoVersesList.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                gotoVersesList.putExtra("suraName", suraNamesArabic.get(i));
//                gotoVersesList.putExtra("totalVerses", suraAyasTotal.get(i));
//                mContext.startActivity(gotoVersesList);
            }
        });
    }

    @Override
    public int getItemCount() {
        return suraNamesEn.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView suraItemEn, suraItemArabic;
        private ConstraintLayout suraLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            suraItemEn = itemView.findViewById(R.id.suraNameTextEn);
            suraItemArabic = itemView.findViewById(R.id.suraNameTextArabic);

            suraLayout = itemView.findViewById(R.id.suraItemLayout);
        }
    }

}
