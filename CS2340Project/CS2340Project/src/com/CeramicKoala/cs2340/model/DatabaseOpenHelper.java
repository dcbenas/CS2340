package com.CeramicKoala.cs2340.model;

import java.text.ParseException;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class DatabaseOpenHelper<T extends DatabaseElement> extends SQLiteOpenHelper {

	//info specific to SQLite database and table
	static final int DATABASE_VERSION = 15;
	static final String DATABASE_NAME = "CeramicKoala";
    Context context;
	
    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }
	
	@Override
    public void onCreate(SQLiteDatabase db) {
		//create login table if not exists
        db.execSQL(LoginOpenHelper.LOGIN_TABLE_CREATE);
        //create account table if not exists
        db.execSQL(AccountOpenHelper.ACCOUNT_TABLE_CREATE);
        //create transaction table if not exists
        db.execSQL(TransactionOpenHelper.TRANSACTION_TABLE_CREATE);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	//upgrade login table
    	db.execSQL(LoginOpenHelper.LOGIN_TABLE_UPGRADE);
    	//upgrade account table
    	db.execSQL(AccountOpenHelper.ACCOUNT_TABLE_UPGRADE);
    	//upgrade transaction table
    	db.execSQL(TransactionOpenHelper.TRANSACTION_TABLE_UPGRADE);
    	onCreate(db);
    }
	
	/**
     * adds a new element to the table. 
     * @param T t element to be added
     * @return T newly added element if successful
     * @throws DatabaseException if unsuccessful
     */
    public abstract T addElement(T t) throws DatabaseException;
    
    /**
     * updates element in table
     * @param T t element with id and new information
     * @return T new element if successful
     * @throws DatabaseException if unsuccessful
     */
    public abstract T updateElement(T t) throws DatabaseException, UnsupportedOperationException;
    
    /**
     * deletes an element permanently
     * @param T t element to be deleted
     * @return true if successful
     */
    public abstract boolean deleteElement(T t);
    
    /**
     * retrieves an element by name
     * @param name field
     * @return T the element with the input name if successful
     * @throws DatabaseException if element does not exist
     * @throws UnsupportedOperationException if unsupported
     */
    public abstract T getElementByName(String name) throws DatabaseException, UnsupportedOperationException;
    
    /**
     * retrieves an element by unique id
     * @param id
     * @return T the element with the input id if successful
     * @throws DatabaseException if element does not exist
     * @throws UnsupportedOperationException if unsupported
     */
    public abstract T getElementById(int id) throws DatabaseException, UnsupportedOperationException;
    
    
    /**
     * gets a list of all elements
     * @return List<T> all elements in table or empty list
     * @throws UnsupportedOperationException if unsupported
     * @throws ParseException 
     */
    public abstract List<T> getAllElements() throws UnsupportedOperationException, ParseException;
    
    /**
     * gets the number of rows in table
     * @return int size of table
     */
    public abstract int getTableSize();
    
    /**
     * deletes all rows in the table
     * @return
     */
    public abstract boolean resetTable();
    
    /**
     * gets table.toString, table size, and optional further
     * information
     * @return String above information
     */
    public abstract String getTableInfo();
    
    /**
     * private method that implementing classes are encouraged to implement
     * to assist with CRUD operations
     * checks if element already exists in table
     * @param user
     * @return true if element already exists in table
     * boolean elementAlreadyExists(T t);
     */
}
