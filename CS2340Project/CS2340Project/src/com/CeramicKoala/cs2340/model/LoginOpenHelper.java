package com.CeramicKoala.cs2340.model;

import java.util.ArrayList;
import java.util.List;

import com.CeramicKoala.cs2340.BuildConfig;
import com.CeramicKoala.cs2340.R;

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
public class LoginOpenHelper extends SQLiteOpenHelper implements DatabaseOpenHelper<User> {
	
	//info specific to SQLite database and table
	static final int DATABASE_VERSION = 5;
	static final String DATABASE_NAME = "CeramicKoala";
	//table holding login info
    static final String LOGIN_TABLE = "login";
    static final String KEY_ID = "userId";
    static final String KEY_USERNAME = "username";
    static final String KEY_PASSWORD = "password";
    static final String KEY_FULL_NAME = "fullName";

    

    public LoginOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    	//create login table if not exists
    	final String LOGIN_TABLE_CREATE =
                "CREATE TABLE IF NOT EXISTS " + LOGIN_TABLE + 
                "(" + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_USERNAME + " TEXT, "
                + KEY_PASSWORD + " TEXT, "
                + KEY_FULL_NAME + " TEXT);";
    	
        db.execSQL(LOGIN_TABLE_CREATE);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	final String upgrade = 
    			"DROP TABLE IF EXISTS " + LOGIN_TABLE + ";";
    	db.execSQL(upgrade);
    	onCreate(db);
    }
    
    @Override
    public User addElement(User user) throws DatabaseException {
    	if (checkUserAlreadyExists(user)) throw new DatabaseException("user already exists"); 
    	
		//add new user
		SQLiteDatabase db = this.getWritableDatabase();
    	ContentValues values = new ContentValues();
    	values.put(KEY_FULL_NAME, user.getFullName());
    	values.put(KEY_USERNAME, user.getUsername());
    	values.put(KEY_PASSWORD, user.getPassword());
    	
    	long success = db.insert(LOGIN_TABLE, null, values);
    	db.close();
    	if (success != -1) {
    		return user;
    	} else {
    		User noUser = new User(null, null, null);
    		user.setId(-1);
    		return noUser;
    	}
    	
    }
    
    @Override
    public User updateElement(User user) {
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	ContentValues values = new ContentValues();
    	values.put(KEY_FULL_NAME, user.getFullName());
    	values.put(KEY_USERNAME, user.getUsername());
    	values.put(KEY_PASSWORD, user.getPassword());
    	
    	//update user if new username is not already in use
    	if (checkUserAlreadyExists(user)) {
    		return new User(null, null, null);
    	} else {
    		String where = KEY_ID + "=?";
        	String[] whereArgs = {String.valueOf(user.getId())};
        	
        	int success = db.update(LOGIN_TABLE, values, where, whereArgs);
        	if (success >0) {
        		//return newly updated user
        		try {
                	return getElementByName(user.getUsername());
        		} catch (DatabaseException e) {
        			return new User(null, null, null);
        		}
        	} else {
        		return new User(null, null, null);
        	}
    	}
    }
    
    @Override
    public boolean deleteElement(User user) {
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	String where = KEY_ID + "=?";
    	String[] whereArgs = {String.valueOf(user.getId())};
    	
    	int success = db.delete(LOGIN_TABLE, where, whereArgs);
    	return (success > 0);
    }
    
    @Override
    public User getElementByName(String username) throws DatabaseException {
    	SQLiteDatabase db = this.getReadableDatabase();
    	String[] columns = {KEY_FULL_NAME, KEY_USERNAME, KEY_PASSWORD, KEY_ID};
    	String selection = KEY_USERNAME + "=" + "'" + username + "'";
    	
    	//Cursor cursor = db.rawQuery("SELECT * FROM login WHERE username = '" , selectionArgs)
    	Cursor cursor = db.query(LOGIN_TABLE, columns, selection, null, null, null, null);
    	if (cursor.getCount() != 0) {
    		cursor.moveToFirst();
    		
    		//DEBUG
    		if (BuildConfig.DEBUG) {
    			Log.d("LoginOpenHelper.getUser.cursor_cols", String.valueOf(cursor.getColumnCount()));
        		Log.d("LoginOpenHelper.getUser.cursor_rows", String.valueOf(cursor.getCount()));
    		}
    		
    		//populate user with info from db
    		User user = new User(
    				cursor.getString(cursor.getColumnIndex(KEY_FULL_NAME)),
        			cursor.getString(cursor.getColumnIndex(KEY_USERNAME)), 
        			cursor.getString(cursor.getColumnIndex(KEY_PASSWORD)));
        	user.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
        	
        	return user;
    	} else {
    		throw new DatabaseException("user does not exist");
    	}
    }
    
    @Override
    public User getElementById(int id) throws UnsupportedOperationException {
    	throw new UnsupportedOperationException("use getElementByName(String username)");
    }
    
    @Override
    public List<User> getAllElements() {
    	SQLiteDatabase db = this.getReadableDatabase();
    	String[] columns = {KEY_FULL_NAME, KEY_USERNAME, KEY_PASSWORD};
    	String orderBy = KEY_ID + " ASC";
    	
    	Cursor cursor = db.query(LOGIN_TABLE, columns, null, null, null, null, orderBy);
    	
    	List<User> userList = new ArrayList<User>();
    	if (cursor.getCount() != 0) {
    		cursor.moveToFirst();
        	
        	do {
        		User user = new User(
        				cursor.getString(cursor.getColumnIndex(KEY_FULL_NAME)),
        				cursor.getString(cursor.getColumnIndex(KEY_USERNAME)), 
        				cursor.getString(cursor.getColumnIndex(KEY_PASSWORD)));
        		userList.add(user);
        	} while (cursor.moveToNext());
    	}
    	
    	return userList;
    }
    
    @Override
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
    
    @Override
    public boolean resetTable() {
    	//DEBUG
    	if (BuildConfig.DEBUG) {
    		SQLiteDatabase db = this.getWritableDatabase();
        	return (db.delete(LOGIN_TABLE, null, null) > 0);
    	} else {
    		return false;
    	}
    }
    
    @Override
    public String getTableInfo() {
    	StringBuilder info = new StringBuilder();
    	
    	//table info
    	info.append("table info: \n");
    	info.append(this.getReadableDatabase().toString() + "\n");
    	
    	//get table size
    	info.append("size: " + getTableSize() + "\n");
    	
    	//get all usernames
    	info.append("users: ");
    	List<User> users = getAllElements();
    	for (User user : users) {
    		info.append(user.getUsername() + ", ");
    	}
    	
    	return info.toString();
    }
    
    /**
     * checks database to see if username already exists
     * @param user
     * @return
     */
    boolean checkUserAlreadyExists(User user) {
    	try {
        	getElementByName(user.getUsername());
        	return true;
    	} catch (DatabaseException e) {
        	return false;
    	}
    }
    
}
