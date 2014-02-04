package com.example.cs2340project;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class AccountOpenHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 3;
	private static final String DATABASE_NAME = "CeramicKoala";
    private static final String LOGIN_TABLE = "login";
    private static final String KEY_ID = "id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    

    AccountOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    	final String LOGIN_TABLE_CREATE =
                "CREATE TABLE IF NOT EXISTS " + LOGIN_TABLE + 
                "(" + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_USERNAME + " TEXT, "
                + KEY_PASSWORD + " TEXT);";
    	
        db.execSQL(LOGIN_TABLE_CREATE);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	final String upgrade = 
    			"DROP TABLE IF EXISTS " + LOGIN_TABLE + ";";
    	db.execSQL(upgrade);
    	onCreate(db);
    }
    
    /**
     * creates a new row in table and adds user 
     * username and password
     * @param user contains username and password
     * @return true if successful
     */
    public boolean addUser(User user) {
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	ContentValues values = new ContentValues();
    	values.put(KEY_USERNAME, user.getUsername());
    	values.put(KEY_PASSWORD, user.getPassword());
    	
    	long success = db.insert(LOGIN_TABLE, null, values);
    	db.close();
    	return (success != -1);
    }
    
    /**
     * updates user information (username or password)
     * user Id cannot change and is therefore used to 
     * query the database.
     * @param user with updated information
     * @return true if successful
     */
    public boolean updateUser(User user) {
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	ContentValues values = new ContentValues();
    	values.put(KEY_PASSWORD, user.getUsername());
    	values.put(KEY_PASSWORD, user.getPassword());
    	
    	String where = KEY_ID + "=?";
    	String[] whereArgs = {String.valueOf(user.getId())}; 
    	
    	int success = db.update(LOGIN_TABLE, values, where, whereArgs);
    	return (success > 0);
    }
    
    /**
     * deletes a user from the db based on user Id
     * @param user the user to be deleted
     * @return true if successful
     */
    public boolean deleteUser(User user) {
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	String where = KEY_ID + "=?";
    	String[] whereArgs = {String.valueOf(user.getId())};
    	
    	int success = db.delete(LOGIN_TABLE, where, whereArgs);
    	return (success > 0);
    }
    
    /**
     * searches table for user and returns User
     * @param username of the user to search for
     * @return User with username that was passed in
     */
    public User getUser(String username) {
    	SQLiteDatabase db = this.getReadableDatabase();
    	String[] columns = {KEY_USERNAME, KEY_PASSWORD, KEY_ID};
    	String selection = KEY_USERNAME + "=" + "'" + username + "'";
    	
    	//Cursor cursor = db.rawQuery("SELECT * FROM login WHERE username = '" , selectionArgs)
    	Cursor cursor = db.query(false, LOGIN_TABLE, columns, selection, null, null, null, null, null, null);
    	if (cursor.getCount() != 0) {
    		cursor.moveToFirst();
    		Log.d("cursor cols", String.valueOf(cursor.getColumnCount()));
    		Log.d("cursor rows", String.valueOf(cursor.getCount()));
    		User user = new User(
        			cursor.getString(cursor.getColumnIndex(KEY_USERNAME)), 
        			cursor.getString(cursor.getColumnIndex(KEY_PASSWORD)));
        	user.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
        	
        	return user;
    	} else {
    		Log.d("error", "no account with that username");
    		return new User("null", "null");
    	}
    }
    
    /**
     * returns a list of all users currently in the table
     * in order of id (which correlates to date created)
     * @return list of all users
     */
    public List<User> getAllUsers() {
    	SQLiteDatabase db = this.getReadableDatabase();
    	String[] columns = {KEY_USERNAME, KEY_PASSWORD};
    	String orderBy = KEY_ID + " ASC";
    	
    	Cursor cursor = db.query(LOGIN_TABLE, columns, null, null, null, null, orderBy);
    	if (cursor.getCount() != 0) cursor.moveToFirst();
    	
    	List<User> userList = new ArrayList<User>();
    	do {
    		User user = new User(
    				cursor.getString(cursor.getColumnIndex(KEY_USERNAME)), 
    				cursor.getString(cursor.getColumnIndex(KEY_PASSWORD)));
    		userList.add(user);
    	} while (cursor.moveToNext());
    	
    	return userList;
    }
    
    public int resetDatabase() {
    	SQLiteDatabase db = this.getWritableDatabase();
    	return db.delete(LOGIN_TABLE, null, null);
    }


}
