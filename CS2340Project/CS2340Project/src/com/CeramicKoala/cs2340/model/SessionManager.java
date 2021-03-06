package com.CeramicKoala.cs2340.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
//import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;

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
	
	private static final String TAG = "SessionManager";
	
//	private static boolean isLoggedIn;
//	private static String username;
//	private static int userId;
//	private static boolean hasCurrentAccount;
//	private static String accountName;
//	private static int accountId;

	private SharedPreferences prefs;
	private Editor editor;
	private LoginOpenHelper loginHelper;
	private AccountOpenHelper accountHelper;
	private AlertDialogManager alertManager;
	
	//info for connectin to shared prefrences

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
	public SessionManager(Activity activity) {
		
 		//prefs = activity.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		prefs = PreferenceManager.getDefaultSharedPreferences(activity);
		editor = prefs.edit();
		loginHelper = new LoginOpenHelper(activity);
		accountHelper = new AccountOpenHelper(activity);
		alertManager = new AlertDialogManager(activity);
	
	}
	
	/**
	 * sets user info upon correct login
	 * VERIFY LOG IN BEFORE CALLING THIS METHOD
	 * sets isLoggedIn, username, and userId
	 * @param user
	 */
	public void logIn(User user) {
		
		// check that username and password match checkCred regex
		if (checkCred(user.getUsername(), user.getPassword())) {
			
			try {
				// check that user exists in database
				// will throw DatabaseException if user doesn't exist
				User loggedInUser = loginHelper.getElementByName(user.getUsername());
				System.out.println(loggedInUser.getFullName());
				
				Log.d(TAG, "got user from database");
				//user exists, successful login
				editor.putString(KEY_USERNAME, loggedInUser.getUsername());
				editor.putInt(KEY_USER_ID, loggedInUser.getId());
				editor.putBoolean(KEY_LOGGED_IN, true);
				
				editor.apply();
//				username = loggedInUser.getUsername();
//				userId = loggedInUser.getId();
//				isLoggedIn = true;
				
			} catch (DatabaseException e) {
				
				//user does not exist
				AlertDialog alert = alertManager.generateAlertDialog(
						AlertDialogManager.AlertType.ACCOUNT_DOES_NOT_EXIST);
				
				//change message to display message from Database Exception
				alert.setMessage(e.getMessage());
				alert.show();
			}
			
		}
	}
	
	/**
	 * erases all user and account info
	 */
	public void logOut() {
		
//		isLoggedIn = false;
//		username = null;
//		userId = 0;
//		removeAccount();
		
		editor.clear();
		editor.apply();
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
		
		editor.apply();
		
//		accountName = account.getName();
//		accountId = account.getAccountId();
//		hasCurrentAccount = true;
	}
	
	/**
	 * erases only current account info
	 */
	public void removeAccount() {
		
		editor.remove(KEY_ACCOUNT_NAME);
		editor.remove(KEY_ACCOUNT_ID);
		editor.remove(KEY_HAS_ACCOUNT);
		
		editor.apply();
		
//		accountName = null;
//		accountId = 0;
//		hasCurrentAccount = false;
	}
	
	/**
	 * getter for isLoggedIn
	 * defaults to false
	 * @return isLoggedIn
	 */
	public boolean isLoggedIn() {
		
		return prefs.getBoolean(KEY_LOGGED_IN, false);
//		return isLoggedIn;
	}
	
	/**
	 * getter for username
	 * defaults to null
	 * @return username
	 */
	public String getUsername() {
		
		return prefs.getString(KEY_USERNAME, null);
//		return username;
	}
	
	/**
	 * getter for user id
	 * defaults to -1
	 * @return userId
	 */
	public int getUserId() {
		
		return prefs.getInt(KEY_USER_ID, -1);
//		return userId;
	}
	
	/**
	 * gets current user with username username
	 * defaults to null
	 * @return current user
	 */
	public User getUser() {
		
		String username = prefs.getString(KEY_USERNAME, null);
		System.out.println("username: " + username);
		User user = null;
		
		try {
			
			user = loginHelper.getElementByName(username);
		
		} catch (DatabaseException e) {
			
			//user does not exist
			AlertDialog alert = alertManager.generateAlertDialog(
					AlertDialogManager.AlertType.ACCOUNT_DOES_NOT_EXIST);
			
			// modify alert to display message for DatabaseException
			alert.setMessage(e.getMessage());
			
			alert.show();
		}
		
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
//		if (isLoggedIn) {
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
//		return hasCurrentAccount;
	}
	
	/**
	 * getter for account name
	 * defaults to null
	 * @return accountName
	 */
	public String getAccountName() {
		
		return prefs.getString(KEY_ACCOUNT_NAME, null);
//		return accountName;
	}
	
	/**
	 * getter for account id
	 * deafults to -1
	 * @return accountId
	 */
	public int getAccountId() {
		
		return prefs.getInt(KEY_ACCOUNT_ID, -1);
//		return accountId;
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
	
	/**
	 * provides initial checks for username and password
	 * @param user
	 * @param pass
	 * @return true if everything is good
	 */
	private boolean checkCred(String user, String pass) {
		
		if(!user.matches("[a-z|A-Z|0-9]{6}[a-z|A-Z|0-9]*")) {
			
			alertManager.generateAlertDialog(
					AlertDialogManager.AlertType.INCORRECT_LOGIN)
					.show();
			return false;
		}

		if(!pass.matches("[a-z|A-Z|0-9]{6}[a-z|A-Z|0-9]*")) {
			
			return false;
		}

		return true;
	}
	
}
