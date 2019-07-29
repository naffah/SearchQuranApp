package com.naffah.searchquranapp.Models;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Bookmarks.class}, version = 1)
public abstract class BookmarksDatabase extends RoomDatabase {
    public abstract DaoAccess daoAccess() ;
}
