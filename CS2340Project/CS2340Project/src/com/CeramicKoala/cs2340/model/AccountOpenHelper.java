package com.CeramicKoala.cs2340.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AccountOpenHelper extends SQLiteOpenHelper {
	
	//info specific to SQLite database and table
	private static final int DATABASE_VERSION = 5;
	private static final String DATABASE_NAME = "CeramicKoala";
	//table holding Account info
	private static final String ACCOUNT_TABLE = "account";
	//uses KEY_ID from login table
	private static final String KEY_ACCOUNT_NAME = "accountName";
	private static final String KEY_ACCOUNT_BALANCE = "accountBalance";
	private static final String KEY_ACCOUNT_INTEREST_RATE = "accountInterestRate";
	
    public AccountOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
