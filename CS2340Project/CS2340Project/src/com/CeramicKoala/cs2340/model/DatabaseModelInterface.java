package com.CeramicKoala.cs2340.model;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.CeramicKoala.cs2340.BuildConfig;

public interface DatabaseModelInterface {
	//TODO create classes for each account type
    
    
    public User addUser(User user);
    
    public boolean updateUser(User user);
    
    public boolean deleteUser(User user);
    
    public User getUser(String username);
    
    public List<User> getAllUsers();
    
    public int getTableSize();
    
    public boolean resetDatabase();
    
    public String getTableInfo();
   
}
