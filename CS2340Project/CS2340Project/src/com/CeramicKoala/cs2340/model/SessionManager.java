package com.CeramicKoala.cs2340.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Session Manager is used to interact with SharedPreferences
 * and store all information related to a single user session.
 * Use SessionManager for logging in/out.
 * SessionManager does not verify any information. It is simply used
 * for information storage and reference. Verification should be done
 * beforehand
 * @author Benjamin Newcomer
 */
public class SessionManager {

	private SharedPreferences prefs;
	private Editor editor;
	private LoginOpenHelper loginHelper;
	private AccountOpenHelper accountHelper;
	
	//info for connectin to shared prefrences
	private static final int PRIVATE_MODE = 0;
	private static final String PREF_NAME = "CeramicKoalaPrefs";

	//info for current user
	private static final String KEY_LOGGED_IN = "isLoggedIn";
	private static final String KEY_USERNAME = "username";
	private static final String KEY_USER_ID = "userId";
	
	//info for current account
	private static final String KEY_HAS_ACCOUNT = "hasCurrentAccount";
	private static final String KEY_ACCOUNT_NAME = "accountName";
	private static final String KEY_ACCOUNT_ID = "accountId";
	
	/**
	 * constructor
	 * @param context
	 */
	@SuppressLint("CommitPrefEdits")
	public SessionManager(Context context) {
		
		prefs = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = prefs.edit();
		loginHelper = new LoginOpenHelper(context);
		accountHelper = new AccountOpenHelper(context);
	
	}
	
	/**
	 * sets user info upon correct login
	 * VERIFY LOG IN BEFORE CALLING THIS METHOD
	 * sets isLoggedIn, username, and userId
	 * @param user
	 */
	public void logIn(User user) {
		
		editor.putString(KEY_USERNAME, user.getUsername());
		editor.putInt(KEY_USER_ID, user.getId());
		editor.putBoolean(KEY_LOGGED_IN, true);
		
		editor.commit();
	
	}
	
	/**
	 * erases all user and account info
	 */
	public void logOut() {
		
		editor.clear();
		editor.commit();
	}
	
	/**
	 * sets current account info
	 * sets hasCurrentAccount, accountName, 
	 * and accountId
	 * @param account
	 */
	public void setAccount(Account account) {
		
		editor.putString(KEY_ACCOUNT_NAME, account.getName());
		editor.putInt(KEY_ACCOUNT_ID, account.getAccountId());
		editor.putBoolean(KEY_HAS_ACCOUNT, true);
		
		editor.commit();
	}
	
	/**
	 * erases only current account info
	 */
	public void removeAccount() {
		
		editor.remove(KEY_ACCOUNT_NAME);
		editor.remove(KEY_ACCOUNT_ID);
		editor.remove(KEY_HAS_ACCOUNT);
		
		editor.commit();
	}
	
	/**
	 * getter for isLoggedIn
	 * defaults to false
	 * @return isLoggedIn
	 */
	public boolean isLoggedIn() {
		
		return prefs.getBoolean(KEY_LOGGED_IN, false);
	}
	
	/**
	 * getter for username
	 * defaults to null
	 * @return username
	 */
	public String getUsername() {
		
		return prefs.getString(KEY_USERNAME, null);
	}
	
	/**
	 * getter for user id
	 * defaults to -1
	 * @return userId
	 */
	public int getUserId() {
		
		return prefs.getInt(KEY_USER_ID, -1);
	}
	
	/**
	 * gets current user with id userId
	 * defaults to null
	 * @return current user
	 */
	public User getUser() {
		
		int userId = prefs.getInt(KEY_USER_ID, -1);
		User user = loginHelper.getElementById(userId);
		
		return user;
	}
	
	/**
	 * changes user info if user updates
	 * info while logged in
	 * SHOULD ONLY BE USED AFTER USER VERIFICATION
	 * IS PERFORMED
	 * @param user
	 */
	public void updateUser(User user) {
		
		if (prefs.getBoolean(KEY_LOGGED_IN, false)) {
			logIn(user);
		} else {
			logOut();
		}
	}
	
	/**
	 * getter for hasCurrentAccount
	 * defaults to false
	 * @return hasCurrentAccount
	 */
	public boolean hasCurrentAccount() {
		
		return prefs.getBoolean(KEY_HAS_ACCOUNT, false);
	}
	
	/**
	 * getter for account name
	 * defaults to null
	 * @return accountName
	 */
	public String getAccountName() {
		
		return prefs.getString(KEY_ACCOUNT_NAME, null);
	}
	
	/**
	 * getter for account id
	 * deafults to -1
	 * @return accountId
	 */
	public int getAccountId() {
		
		return prefs.getInt(KEY_ACCOUNT_ID, -1);
	}
	
	/**
	 * gets account with id accountId
	 * @return account
	 */
	public Account getAccount() {
		
		int accountId = prefs.getInt(KEY_ACCOUNT_ID, -1);
		Account account = null;
		try {
			account = accountHelper.getElementById(accountId);
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		
		return account;
		
	}
	
}
