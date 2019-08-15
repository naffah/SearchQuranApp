package com.naffah.searchquranapp.Controllers.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.naffah.searchquranapp.Controllers.Activities.VersesList.VersesListActivity;

public class TranslationsDialog extends DialogFragment {

    String[] listItems = {"English", "Urdu"};
    String selectedItem;
    Context context;

    public TranslationsDialog(){

    }

    @SuppressLint("ValidFragment")
    public TranslationsDialog(Context context){
        this.context = context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        SharedPreferences pref = context.getSharedPreferences("MyPref", 0);
        final SharedPreferences.Editor editor = pref.edit();

        selectedItem = new String();  // Where we track the selected items
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Translation Language")
                .setItems(listItems, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        selectedItem = listItems[which];
                        if(selectedItem.equals("English")){
                            editor.remove("translation");
                            editor.putString("translation","database/english-translations/en.ahmedali.xml");
                            editor.apply();
                        }
                        else if(selectedItem.equals("Urdu")){
                            editor.remove("translation");
                            editor.putString("translation","database/english-translations/ur.ahmedali.xml");
                            editor.apply();
                        }
                    }
                });
        return builder.create();
    }
}
