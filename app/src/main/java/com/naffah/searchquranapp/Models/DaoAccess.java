package com.naffah.searchquranapp.Models;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface DaoAccess {
    //Methods for Bookmarks Table
    @Insert
    void insertSingleBookmark (Bookmarks bookmark);
    @Insert
    void insertMultipleBookmarks (List<Bookmarks> bookmarksList);
    @Query("SELECT * FROM Bookmarks")
    List<Bookmarks> fetchBookmarks();
    @Query("SELECT * FROM Bookmarks WHERE id = :bookmarksId")
    Bookmarks fetchBookmarksbyId (int bookmarksId);
    @Update
    void updateBookmark (Bookmarks bookmark);
    @Delete
    void deleteBookmark (Bookmarks bookmark);
    @Query("DELETE FROM Bookmarks")
    void nukeTable();
    
    //Methods for UserProfiling Table
    @Insert
    void insertUserProfiling (UserProfiling userprofiling);
    @Query("SELECT * FROM UserProfiling")
    List<UserProfiling> fetchUserProfiling();
    @Query("DELETE FROM UserProfiling WHERE id = :id")
    void deleteByUserId(int id);
}
