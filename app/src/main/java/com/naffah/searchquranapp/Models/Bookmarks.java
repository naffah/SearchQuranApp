package com.naffah.searchquranapp.Models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Bookmarks {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id = 0;
    private String verseIndex;
    private String verseArabic;
    private String verseEnglish;

    public Bookmarks(){
    }

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public String getVerseIndex() {
        return verseIndex;
    }

    public void setVerseIndex(String verseIndex) {
        this.verseIndex = verseIndex;
    }

    public String getVerseArabic() {
        return verseArabic;
    }

    public void setVerseArabic(String verseArabic) {
        this.verseArabic = verseArabic;
    }

    public String getVerseEnglish() {
        return verseEnglish;
    }

    public void setVerseEnglish(String verseEnglish) {
        this.verseEnglish = verseEnglish;
    }
}
