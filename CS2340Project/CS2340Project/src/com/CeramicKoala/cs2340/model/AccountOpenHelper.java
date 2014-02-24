package com.CeramicKoala.cs2340.model;

import java.util.ArrayList;
import java.util.List;

import com.CeramicKoala.cs2340.BuildConfig;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * AccountOpenHelper provides an interface for performing CRUD operations on the
 * account table in the CeramicKoala SQLite Database
 * Pertinent database fields are located in com.CeramicKoala.cs2340.model.LoginOpenHelper
 * @author Benjamin Newcomer
 */
public class AccountOpenHelper extends SQLiteOpenHelper implements DatabaseOpenHelper<Account> {
	
	
	LoginOpenHelper loginHelper;
	static final String ACCOUNT_TABLE = "account";
	static final String KEY_ACCOUNT_ID = "accountId";
	static final String KEY_ACCOUNT_NAME = "name";
	static final String KEY_ACCOUNT_BALANCE = "balance";
	static final String KEY_ACCOUNT_INTEREST_RATE = "interestRate";
	
    public AccountOpenHelper(Context context) {
        super(context, LoginOpenHelper.DATABASE_NAME, null, LoginOpenHelper.DATABASE_VERSION);
        loginHelper = new LoginOpenHelper(context);
    }
    
    public AccountOpenHelper(Context context, String databaseName, int databaseVersion) {
    	super(context, databaseName,  null, databaseVersion);
    }
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		//create account table if not exists
    	final String ACCOUNT_TABLE_CREATE =
                "CREATE TABLE IF NOT EXISTS " + ACCOUNT_TABLE + 
                "(" + KEY_ACCOUNT_ID + " INTEGER PRIMARY KEY, "
                + LoginOpenHelper.KEY_ID + " INTEGER, "
                + KEY_ACCOUNT_NAME + " TEXT, "
                + KEY_ACCOUNT_BALANCE + " TEXT, "
                + KEY_ACCOUNT_INTEREST_RATE + " TEXT);";
    	
        db.execSQL(ACCOUNT_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		final String upgrade = 
    			"DROP TABLE IF EXISTS " + ACCOUNT_TABLE + ";";
    	db.execSQL(upgrade);
    	onCreate(db);

	}
	
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	//do nothing
    }
	
	@Override
    public Account addElement(Account account) throws DatabaseException {
    	//if (!loginHelper.checkUserAlreadyExists(user)) throw new DatabaseException("user does not exist.");
    	if (checkAccountAlreadyExists(account)) throw new DatabaseException("account already exists.");
    	
		//add new user
		SQLiteDatabase db = this.getWritableDatabase();
    	ContentValues values = new ContentValues();
    	values.put(LoginOpenHelper.KEY_ID, account.getUserId());
    	values.put(KEY_ACCOUNT_NAME, account.getName());
    	values.put(KEY_ACCOUNT_BALANCE, account.getBalance());
    	values.put(KEY_ACCOUNT_INTEREST_RATE, account.getInterestRate());
    	
    	long success = db.insert(ACCOUNT_TABLE, null, values);
    	db.close();
    	if (success != -1) {
    		return account;
    	} else {
    		Account noAccount = new Account(0);
    		return noAccount;
    	}
    	
    }
    
    @Override
    public Account updateElement(Account account) throws DatabaseException {
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	ContentValues values = new ContentValues();
    	values.put(LoginOpenHelper.KEY_ID, account.getUserId());
    	values.put(KEY_ACCOUNT_NAME, account.getName());
    	values.put(KEY_ACCOUNT_BALANCE, account.getBalance());
    	values.put(KEY_ACCOUNT_INTEREST_RATE, account.getInterestRate());
    	
    	String where = KEY_ACCOUNT_ID + "=?";
        String[] whereArgs = {String.valueOf(account.getAccountId())};
        	
        int success = db.update(ACCOUNT_TABLE, values, where, whereArgs);
        if (success >0) {
        	//return newly updated account
            return getElementById(account.getAccountId());
       	} else {
    		throw new DatabaseException("account update failure.");
       	}
    }
    
    @Override
    public boolean deleteElement(Account account) {
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	String where = KEY_ACCOUNT_ID + "=?";
    	String[] whereArgs = {String.valueOf(account.getAccountId())};
    	
    	int success = db.delete(ACCOUNT_TABLE, where, whereArgs);
    	return (success > 0);
    }
   
    @Override
    public Account getElementByName(String name) throws UnsupportedOperationException {
    	throw new UnsupportedOperationException("use getElementById(int accountId)");
    }
    
    @Override
    public Account getElementById(int accountId) throws DatabaseException {
    	SQLiteDatabase db = this.getReadableDatabase();
    	String[] columns = {
    			KEY_ACCOUNT_NAME, 
    			KEY_ACCOUNT_BALANCE, 
    			KEY_ACCOUNT_INTEREST_RATE, 
    			LoginOpenHelper.KEY_ID,
    			KEY_ACCOUNT_ID};
    	String selection = KEY_ACCOUNT_ID + "=" + "'" + accountId + "'";
    	
    	Cursor cursor = db.query(ACCOUNT_TABLE, columns, selection, null, null, null, null);
    	if (cursor.getCount() != 0) {
    		cursor.moveToFirst();
    		
    		//DEBUG
    		if (BuildConfig.DEBUG) {
    			Log.d("LoginOpenHelper.getUser.cursor_cols", String.valueOf(cursor.getColumnCount()));
        		Log.d("LoginOpenHelper.getUser.cursor_rows", String.valueOf(cursor.getCount()));
    		}
    		
    		//populate Account with info from db
    		Account account = new Account(
    				cursor.getInt(cursor.getColumnIndex(KEY_ACCOUNT_ID)),
    				cursor.getInt(cursor.getColumnIndex(LoginOpenHelper.KEY_ID)),
    				cursor.getString(cursor.getColumnIndex(KEY_ACCOUNT_NAME)),
    				cursor.getDouble(cursor.getColumnIndex(KEY_ACCOUNT_BALANCE)),
    				cursor.getDouble(cursor.getColumnIndex(KEY_ACCOUNT_INTEREST_RATE)));
        	
        	return account;
    	} else {
    		throw new DatabaseException("account does not exist");
    	}
    }
    
    @Override
    public List<Account> getAllElements() throws UnsupportedOperationException {
    	throw new UnsupportedOperationException("use getAllElements(User user)");
    }
    
    /**
     * gets accounts for a specific user
     * @see DatabaseInterface#getAllElements()
     */
    public List<Account> getAllElements(User user) {
    	SQLiteDatabase db = this.getReadableDatabase();
    	String[] columns = {
    			KEY_ACCOUNT_NAME, 
    			KEY_ACCOUNT_BALANCE, 
    			KEY_ACCOUNT_INTEREST_RATE, 
    			LoginOpenHelper.KEY_ID,
    			KEY_ACCOUNT_ID};
    	String selection = LoginOpenHelper.KEY_ID + "=" + "'" + user.getId() + "'";
    	String orderBy = KEY_ACCOUNT_ID + " ASC";
    	
    	Cursor cursor = db.query(ACCOUNT_TABLE, columns, selection, null, null, null, orderBy);
    	
    	List<Account> accountList = new ArrayList<Account>();
    	if (cursor.getCount() != 0) {
    		cursor.moveToFirst();
        	
        	do {
        		//populate Account with info from db
        		Account account = new Account(
        				cursor.getInt(cursor.getColumnIndex(KEY_ACCOUNT_ID)),
        				cursor.getInt(cursor.getColumnIndex(LoginOpenHelper.KEY_ID)),
        				cursor.getString(cursor.getColumnIndex(KEY_ACCOUNT_NAME)),
        				cursor.getDouble(cursor.getColumnIndex(KEY_ACCOUNT_BALANCE)),
        				cursor.getDouble(cursor.getColumnIndex(KEY_ACCOUNT_INTEREST_RATE)));
        		accountList.add(account);
        	} while (cursor.moveToNext());
    	}
    	
    	return accountList;
    }
    
    @Override
    public int getTableSize() {
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.rawQuery("SELECT count (*) FROM " + ACCOUNT_TABLE + ";", null);
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
        	return (db.delete(ACCOUNT_TABLE, null, null) > 0);
    	} else {
    		return false;
    	}
    }
    
    public String getTableInfo() {
    	StringBuilder info = new StringBuilder();
    	
    	//table info
    	info.append("table info: \n");
    	info.append(this.getReadableDatabase().toString() + "\n");
    	
    	//get table size
    	info.append("size: " + getTableSize() + "\n");
    	
    	return info.toString();
    }
    
    /**
     * checks database to see if username already exists
     * @param user
     * @return
     */
    boolean checkAccountAlreadyExists(Account account) {
    	try {
        	getElementById(account.getAccountId());
        	return true;
    	} catch (DatabaseException e) {
        	return false;
    	}
    }

}
