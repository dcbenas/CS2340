package com.CeramicKoala.cs2340.model;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.CeramicKoala.cs2340.BuildConfig;

public interface LoginInterface {
	//TODO create classes for each account type
    
    /**
     * adds a new user to the database. 
     * @param user
     * @return User newly added user.If successful, returns
     * the newly added user. If unsuccessful, returns a User with all
     * null fields
     */
    public User addUser(User user);
    
    /**
     * updates user fields for a specific user
     * @param user
     * @return User new user if successful. returns null user
     * if unsuccessful
     */
    public User updateUser(User user);
    
    /**
     * deletes a user permanently
     * @param user
     * @return true if successful
     */
    public boolean deleteUser(User user);
    
    /**
     * retrieves a user
     * @param username
     * @return User the user with the input username if successful.
     * returns null user if unsuccessful
     */
    public User getUser(String username);
    
    /**
     * gets a list of all users
     * @return List<User> all users in database or empty list
     * if there are no users
     */
    public List<User> getAllUsers();
    
    /**
     * gets the number of rows in table
     * @return int size of table
     */
    public int getTableSize();
    
    /**
     * deletes all rows in the table
     * @return
     */
    public boolean resetDatabase();
    
    /**
     * gets table.toString, table size, and a list of
     * all usernames
     * @return String above information
     */
    public String getTableInfo();
    
   
}
