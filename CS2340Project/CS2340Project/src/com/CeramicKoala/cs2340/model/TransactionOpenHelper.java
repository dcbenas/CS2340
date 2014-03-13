package com.CeramicKoala.cs2340.model;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.CeramicKoala.cs2340.BuildConfig;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TransactionOpenHelper extends DatabaseOpenHelper<Transaction> {
	
	AccountOpenHelper accountHelper;

	//table holding transaction info
    static final String TRANSACTION_TABLE = "accountTransaction";
    static final String KEY_ID = "transactionId";
    static final String KEY_TYPE = "type";
    static final String KEY_AMOUNT = "amount";
    static final String KEY_TIMESTAMP = "timestamp";
    static final String KEY_DATE = "date";
    
    //sql queries for DatabaseOpenHelper
    static final String TRANSACTION_TABLE_CREATE =
    		"CREATE TABLE IF NOT EXISTS " + TRANSACTION_TABLE +
    		"(" + KEY_ID + " INTEGER PRIMARY KEY, "
    		+ AccountOpenHelper.KEY_ACCOUNT_ID + " INTEGER, "
    		+ KEY_TYPE + " INTEGER, "
    		+ KEY_AMOUNT + " REAL, "
    		+ KEY_DATE + " TEXT, "
    		+ KEY_TIMESTAMP + " TEXT);";
    static final String TRANSACTION_TABLE_UPGRADE = 
			"DROP TABLE IF EXISTS " + TRANSACTION_TABLE + ";";

    public TransactionOpenHelper(Context context) {
        super(context);
        accountHelper = new AccountOpenHelper(context);
    }
	
	@Override
	public Transaction addElement(Transaction transaction) throws DatabaseException {
		SQLiteDatabase db = this.getWritableDatabase();
		
		String timestamp = formatDateToString(transaction.getTimestamp());
		String date = formatDateToString(transaction.getDate());
		
		ContentValues values = new ContentValues();
    	values.put(AccountOpenHelper.KEY_ACCOUNT_ID, transaction.getAccountId());
    	values.put(KEY_TYPE, transaction.typeToInt());
    	values.put(KEY_AMOUNT, transaction.getAmount());
    	values.put(KEY_TIMESTAMP, timestamp);
    	values.put(KEY_DATE, date);
    	values.put(AccountOpenHelper.KEY_ACCOUNT_ID, transaction.getAccountId());
    	
    	long success = db.insert(TRANSACTION_TABLE, null, values);
    	db.close();
    	if ((success != -1) && updateAccount(transaction)) {
    		return transaction;
    	} else {
    		Transaction noTransaction = new Transaction(0, 0, 0, null);
    		return noTransaction;
    	}
	}
	
	private boolean updateAccount(Transaction transaction) throws DatabaseException {
		Account account = accountHelper.getElementById(transaction.getAccountId());
		account.incrementBalance(transaction.getAmount());
		Account updatedAccount = accountHelper.updateElement(account);
		
		if (updatedAccount.getBalance() == account.getBalance()) {
			return true;
		} else {
			return false;
		}
	}
	
	private String formatDateToString(Date date) {
		DateFormat formatter = DateFormat.getDateTimeInstance();
		return formatter.format(date);
	}
	
	private Date formatDateToDate(String date) throws ParseException {
		DateFormat formatter = DateFormat.getDateTimeInstance();
		return formatter.parse(date);
	}

	@Override
	public Transaction updateElement(Transaction transaction)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException("transactions cannot be altered");
	}

	@Override
	public boolean deleteElement(Transaction transaction) {
		SQLiteDatabase db = this.getWritableDatabase();
    	
    	String where = KEY_ID + "=?";
    	String[] whereArgs = {String.valueOf(transaction.getId())};
    	
    	int success = db.delete(TRANSACTION_TABLE, where, whereArgs);
    	return (success > 0);
	}

	@Override
	public Transaction getElementByName(String name)
			throws DatabaseException, UnsupportedOperationException {
		throw new UnsupportedOperationException("use getAllElements(int accountId)");
	}

	@Override
	public Transaction getElementById(int id) throws DatabaseException,
			UnsupportedOperationException {
		throw new UnsupportedOperationException("use getAllElements(int accountId)");
	}

	@Override
	public List<Transaction> getAllElements() throws ParseException {
		return getAllElements(-1);
	}
	
	/**
	 * gets all elements for a given account
	 * @see TransactionOpenHelper#getAllElements()
	 * @param accountId
	 * @return List of transactions
	 * @throws ParseException 
	 */
	public List<Transaction> getAllElements(int accountId) throws ParseException {
		SQLiteDatabase db = this.getReadableDatabase();
    	
		String[] columns = {KEY_ID, KEY_TYPE, KEY_AMOUNT, KEY_DATE, 
				KEY_TIMESTAMP, AccountOpenHelper.KEY_ACCOUNT_ID};
    	String orderBy = KEY_ID + " ASC";
    	String selection = AccountOpenHelper.KEY_ACCOUNT_ID + "=" 
    			+ "'" + accountId + "'";
    	
    	if (accountId == -1) {
    		selection = null;
    	}
    	
    	Cursor cursor = db.query(TRANSACTION_TABLE, columns, selection, null, null, null, orderBy);
    	
    	List<Transaction> transactionList = new ArrayList<Transaction>();
    	if (cursor.getCount() != 0) {
    		cursor.moveToFirst();
        	
        	do {
        		Transaction transaction = new Transaction(
        				cursor.getInt(cursor.getColumnIndex(AccountOpenHelper.KEY_ACCOUNT_ID)),
        				cursor.getInt(cursor.getColumnIndex(KEY_TYPE)),
        				cursor.getDouble(cursor.getColumnIndex(KEY_AMOUNT)),
        				formatDateToDate(cursor.getString(cursor.getColumnIndex(KEY_DATE))),
        				(Timestamp) formatDateToDate(cursor.getString(cursor.getColumnIndex(KEY_TIMESTAMP))),
        				cursor.getInt(cursor.getColumnIndex(KEY_ID)));
        				
        		transactionList.add(transaction);
        	} while (cursor.moveToNext());
    	}
    	
    	return transactionList;
	}

	@Override
	public int getTableSize() {
		SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.rawQuery("SELECT count (*) FROM " + TRANSACTION_TABLE + ";", null);
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
        	return (db.delete(TRANSACTION_TABLE, null, null) > 0);
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
    	
    	return info.toString();
	}

}
