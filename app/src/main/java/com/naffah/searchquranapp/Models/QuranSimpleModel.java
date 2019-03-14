package com.naffah.searchquranapp.Models;

public class QuranSimpleModel {
    private String suraTag = null;
    private String ayaTag = null;
    private int index = 0;
    private String name = null;
    private String text = null;
    private String bismillah = null;

    public String getSuraTag() {
        return suraTag;
    }

    public void setSuraTag(String suraTag) {
        this.suraTag = suraTag;
    }

    public String getAyaTag() {
        return ayaTag;
    }

    public void setAyaTag(String ayaTag) {
        this.ayaTag = ayaTag;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getBismillah() {
        return bismillah;
    }

    public void setBismillah(String bismillah) {
        this.bismillah = bismillah;
    }
}
