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

/**
 * This class interfaces with the SQLite db and handles account CRUD.
 * Currently supported actions: add new user (checks if user already exists),
 * update user, delete user, get user, get all users, 
 * reset database (for dev purposes only).
 * @author Benjamin Newcomer
 * @version 1.0
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class AccountOpenHelper extends SQLiteOpenHelper {

	//info specific to SQLite database and table
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
     * username and password. Only adds user if user
     * does not already exist
     * @param user contains username and password
     * @return User that has been added
     */
    public User addUser(User user) {
    	boolean userAlreadyExists = checkUserAlreadyExists(user);
    	
    	//DEBUG
    	if (BuildConfig.DEBUG) {
        	Log.d("AccountOpenHelper.addUser.user_already_exists", String.valueOf(userAlreadyExists));
    	}
    	
    	if (userAlreadyExists) {
    		User noUser = new User(null, null);
    		user.setId(-1);
    		return noUser;
    	} else {
    		//add new user
    		SQLiteDatabase db = this.getWritableDatabase();
        	ContentValues values = new ContentValues();
        	values.put(KEY_USERNAME, user.getUsername());
        	values.put(KEY_PASSWORD, user.getPassword());
        	
        	long success = db.insert(LOGIN_TABLE, null, values);
        	db.close();
        	if (success != -1) {
        		return user;
        	} else {
        		User noUser = new User(null, null);
        		user.setId(-1);
        		return noUser;
        	}
    	}
    	
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
    	values.put(KEY_USERNAME, user.getUsername());
    	values.put(KEY_PASSWORD, user.getPassword());
    	
    	//update user if new username is not already in use
    	if (checkUserAlreadyExists(user)) {
    		return false;
    	} else {
    		String where = KEY_ID + "=?";
        	String[] whereArgs = {String.valueOf(user.getId())};
        	
        	int success = db.update(LOGIN_TABLE, values, where, whereArgs);
        	return (success > 0);
    	}
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
     * If user does not exist, a null user with Id -1
     * is returned
     */
    public User getUser(String username) {
    	SQLiteDatabase db = this.getReadableDatabase();
    	String[] columns = {KEY_USERNAME, KEY_PASSWORD, KEY_ID};
    	String selection = KEY_USERNAME + "=" + "'" + username + "'";
    	
    	//Cursor cursor = db.rawQuery("SELECT * FROM login WHERE username = '" , selectionArgs)
    	Cursor cursor = db.query(LOGIN_TABLE, columns, selection, null, null, null, null);
    	if (cursor.getCount() != 0) {
    		cursor.moveToFirst();
    		
    		//DEBUG
    		if (BuildConfig.DEBUG) {
    			Log.d("AccountOpenHelper.getUser.cursor_cols", String.valueOf(cursor.getColumnCount()));
        		Log.d("AccountOpenHelper.getUser.cursor_rows", String.valueOf(cursor.getCount()));
    		}
    		
    		//populate user with info from db
    		User user = new User(
        			cursor.getString(cursor.getColumnIndex(KEY_USERNAME)), 
        			cursor.getString(cursor.getColumnIndex(KEY_PASSWORD)));
        	user.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
        	
        	return user;
    	} else {
    		User noUser = new User(null, null);
    		noUser.setId(-1);
    		
    		return noUser;
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
    	
    	List<User> userList = new ArrayList<User>();
    	if (cursor.getCount() != 0) {
    		cursor.moveToFirst();
        	
        	do {
        		User user = new User(
        				cursor.getString(cursor.getColumnIndex(KEY_USERNAME)), 
        				cursor.getString(cursor.getColumnIndex(KEY_PASSWORD)));
        		userList.add(user);
        	} while (cursor.moveToNext());
    	}
    	
    	return userList;
    }
    
    /**
     * gets number of rows for login table
     * @return number of rows. return -1
     * if error occurred
     */
    public int getTableSize() {
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.rawQuery("SELECT count (*) FROM " + LOGIN_TABLE + ";", null);
    	if (cursor != null) {
    		cursor.moveToFirst();
        	return cursor.getInt(0);
    	} else {
    		return -1;
    	}
    }
    
    /**
     * DEBUG
     * deletes all rows in login table. For development use only
     * should be removed before deployment
     * @return true if successful, false if unsuccessful
     * or if not in debug mode
     */
    public boolean resetDatabase() {
    	//DEBUG
    	if (BuildConfig.DEBUG) {
    		SQLiteDatabase db = this.getWritableDatabase();
        	return (db.delete(LOGIN_TABLE, null, null) > 0);
    	} else {
    		return false;
    	}
    }

    /**
     * checks database to see if username already exists
     * @param user
     * @return
     */
    private boolean checkUserAlreadyExists(User user) {
    	User dbUser = getUser(user.getUsername());
    	//if user exists, user will have an Id other than -1
    	return (dbUser.getId() != -1);
    }
    
    /**
     * gives info about the db login table
     * info: size, list of usernames
     * @return string containing info
     */
    public String getTableInfo() {
    	StringBuffer info = new StringBuffer();
    	info.append("table info: \n");
    	info.append(this.getReadableDatabase().toString() + "\n");
    	
    	//get table size
    	info.append("size: " + getTableSize() + "\n");
    	
    	//get all usernames
    	info.append("users: ");
    	List<User> users = getAllUsers();
    	for (User user : users) {
    		info.append(user.getUsername() + ", ");
    	}
    	
    	return info.toString();
    }
    
}
