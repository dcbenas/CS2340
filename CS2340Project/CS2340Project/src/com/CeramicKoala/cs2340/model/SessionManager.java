package com.CeramicKoala.cs2340.model;

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
	private Context context;
	private LoginOpenHelper loginHelper;
	private AccountOpenHelper accountHelper;
	
	private static final int PRIVATE_MODE = 0;
	private static final String PREF_NAME = "CeramicKoalaPrefs";
	private static final String LOGGED_IN_KEY = "isLoggedIn";
	private static final String USERNAME_KEY = "username";
	private static final String USER_ID_KEY = "userId";
	private static final String HAS_ACCOUNT_KEY = "hasCurrentAccount";
	private static final String ACCOUNT_NAME = "accountName";
	private static final String ACCOUNT_ID = "accountId";
	
	// info for keeping track of log in
	// and current user 
	private boolean isLoggedIn;
	private String username;
	private int userId;
	
	// info for keeping track of
	// current account 
	private boolean hasCurrentAccount;
	private String accountName;
	private int accountId;
	
	/**
	 * constructor
	 * @param context
	 */
	public SessionManager(Context context) {
		this.context = context;
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
		username = user.getUsername();
		userId = user.getId();
		isLoggedIn = true;
	}
	
	/**
	 * erases all user and account info
	 */
	public void logOut() {
		username = null;
		userId = -1;
		isLoggedIn = false;
	}
	
	/**
	 * sets current account info
	 * sets hasCurrentAccount, accountName, 
	 * and accountId
	 * @param account
	 */
	public void setAccount(Account account) {
		accountName = account.getName();
		accountId = account.getAccountId();
		hasCurrentAccount = true;
	}
	
	/**
	 * erases only current account info
	 */
	public void unsetAccount() {
		accountName = null;
		accountId = -1;
		hasCurrentAccount = false;
	}
	
	/**
	 * getter for isLoggedIn
	 * @return isLoggedIn
	 */
	public boolean isLoggedIn() {
		return isLoggedIn;
	}
	
	/**
	 * getter for username
	 * @return username
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * getter for user id
	 * @return userId
	 */
	public int getUserId() {
		return userId;
	}
	
	/**
	 * gets current user with id userId
	 * @return current user
	 */
	public User getUser() {
		return null;
	}
	
	/**
	 * changes user info if user updates
	 * info while logged in
	 * SHOULD ONLY BE USED AFTER USER VERIFICATION
	 * IS PERFORMED
	 * @param user
	 */
	public void updateUser(User user) {
		
	}
	
	/**
	 * getter for hasCurrentAccount
	 * @return hasCurrentAccount
	 */
	public boolean hasCurrentAccount() {
		return hasCurrentAccount;
	}
	
	/**
	 * getter for account name
	 * @return accountName
	 */
	public String getAccountName() {
		return accountName;
	}
	
	/**
	 * getter for account id
	 * @return accountId
	 */
	public int getAccountId() {
		return accountId;
	}
	
	/**
	 * gets account with id accountId
	 * @return account
	 */
	public Account getAccount() {
		return null;
	}
	
}
