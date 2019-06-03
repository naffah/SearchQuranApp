package com.naffah.searchquranapp.Controllers.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.naffah.searchquranapp.R;

import java.util.ArrayList;

public class VersesListAdapter extends RecyclerView.Adapter<VersesListAdapter.ViewHolder> {

    public static final String TAG = "VersesListAdapter";

    private ArrayList<String> versesEn = new ArrayList<>();
    private ArrayList<String> versesArabic = new ArrayList<>();
    private ArrayList<String> versesNum = new ArrayList<>();
    private Context mContext;

    public VersesListAdapter(Context mContext, ArrayList<String> versesEn, ArrayList<String> versesArabic, ArrayList<String> versesNum) {
        this.versesEn = versesEn;
        this.versesArabic = versesArabic;
        this.versesNum = versesNum;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public VersesListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_verses_list_item, parent, false);
        VersesListAdapter.ViewHolder holder = new VersesListAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull VersesListAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.versesEn.setText(versesEn.get(i));
        viewHolder.versesArabic.setText(versesArabic.get(i));
        viewHolder.versesNum.setText(versesNum.get(i));
    }

    @Override
    public int getItemCount() {
        return versesEn.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView versesEn, versesArabic, versesNum;
        private ConstraintLayout versesLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            versesEn = itemView.findViewById(R.id.verseTextEn);
            versesArabic = itemView.findViewById(R.id.verseTextArabic);
            versesNum = itemView.findViewById(R.id.verseNumber);

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
