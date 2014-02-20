package com.CeramicKoala.cs2340.model;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.CeramicKoala.cs2340.BuildConfig;

public interface DatabaseModelInterface {
    
    
    public User addUser(User user);
    
    @Override
    public boolean updateUser(User user);
    
    @Override
    public boolean deleteUser(User user);
    
    @Override
    public User getUser(String username);
    
    @Override
    public List<User> getAllUsers();
    
    @Override
    public int getTableSize();
    
    @Override
    public boolean resetDatabase();
    
    @Override
    public String getTableInfo();
   
}
