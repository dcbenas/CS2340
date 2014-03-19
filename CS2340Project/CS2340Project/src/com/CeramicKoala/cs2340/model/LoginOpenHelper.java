package com.CeramicKoala.cs2340.model;

import java.util.ArrayList;
import java.util.List;

import com.CeramicKoala.cs2340.BuildConfig;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

/**
 * LoginOpenHelper provides an interface for performing CRUD operations on the
 * login table in the CeramicKoala SQLite Database
 * @see com.CeramicKoala.cs2340.test.LoginOpenHelperTest
 * @author Benjamin Newcomer
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class LoginOpenHelper extends DatabaseOpenHelper<User> {
	
	//table holding login info
    static final String LOGIN_TABLE = "login";
    static final String KEY_ID = "userId";
    static final String KEY_USERNAME = "username";
    static final String KEY_PASSWORD = "password";
    static final String KEY_FULL_NAME = "fullName";
    //TODO Inseok - move currentUser to AccountManagementActivity
   //DEPRECATED static User currentUser;
    
    //sql queries for DatabaseOpenHelper
    static final String LOGIN_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + LOGIN_TABLE + 
            "(" + KEY_ID + " INTEGER PRIMARY KEY, "
            + KEY_USERNAME + " TEXT, "
            + KEY_PASSWORD + " TEXT, "
            + KEY_FULL_NAME + " TEXT);";
    static final String LOGIN_TABLE_UPGRADE = 
			"DROP TABLE IF EXISTS " + LOGIN_TABLE + ";";

    
    public LoginOpenHelper(Context context) {
        super(context);
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
    	if (!checkUserAlreadyExists(user)) {
    		return new User("user already exists", null, null);
    	} else {
    		String where = KEY_ID + "=?";
        	String[] whereArgs = {String.valueOf(user.getId())};
        	
        	int success = db.update(LOGIN_TABLE, values, where, whereArgs);
        	if (success > 0) {
        		//return newly updated user
        		try {
                	return getElementByName(user.getUsername());
        		} catch (DatabaseException e) {
        			return new User("getElement error", null, null);
        		}
        	} else {
        		return new User("update no succes", null, null);
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

// DEPRECATED
//    public User getCurrentUser() {
//    	return currentUser;
//    }
    
    @Override
    public User getElementByName(String username) throws DatabaseException {
    	SQLiteDatabase db = this.getReadableDatabase();
    	String[] columns = {KEY_FULL_NAME, KEY_USERNAME, KEY_PASSWORD, KEY_ID};
    	String selection = KEY_USERNAME + "=" + "'" + username + "'";
    	
    	Cursor cursor = db.query(LOGIN_TABLE, columns, selection, null, null, null, null);
    	if (cursor.getCount() != 0) {
    		cursor.moveToFirst();
    		
    		//populate user with info from db
    		User user = new User(
    				cursor.getString(cursor.getColumnIndex(KEY_FULL_NAME)),
        			cursor.getString(cursor.getColumnIndex(KEY_USERNAME)), 
        			cursor.getString(cursor.getColumnIndex(KEY_PASSWORD)));
        	user.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
        	
    		user = getAccountsForUser(user);
        	//DEPRECATED currentUser = user;
        	return user;
    	} else {
    		throw new DatabaseException("user does not exist");
    	}
    }
    
    /**
     * populates user with account Id's
     * @param user
     * @param db
     * @return
     */
    private User getAccountsForUser(User user) {
    	AccountOpenHelper accountHelper = new AccountOpenHelper(context);
    	SQLiteDatabase db = accountHelper.getReadableDatabase();
    	
    	//query db for account list
		String[] columns = {
				AccountOpenHelper.KEY_ACCOUNT_NAME, 
				AccountOpenHelper.KEY_ACCOUNT_ID};
		String selection = KEY_ID + "=" + "'" + user.getId() + "'";
    	Cursor cursor = db.query(AccountOpenHelper.ACCOUNT_TABLE, 
    			columns, selection, null, null, null, null);
    	
    	if (cursor.getCount() != 0) {
    		cursor.moveToFirst();
    		do {
    			user.addAccount(
    					cursor.getInt(
    							cursor.getColumnIndex(
    								AccountOpenHelper.KEY_ACCOUNT_ID)));
    		} while (cursor.moveToNext());
    		
    		return user;
    	} else {
    		return user;
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

// DEPRECATED
//    public void logOut() {
//    	currentUser = null;
//    }
}
