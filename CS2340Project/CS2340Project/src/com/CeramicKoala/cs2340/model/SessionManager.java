package com.CeramicKoala.cs2340.model;

/**
 * Session Manager is used to interact with SharedPreferences
 * and store all information related to a single user session.
 * Use SessionManager for logging in/out
 * @author Benjamin Newcomer
 */
public class SessionManager {

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
	 * sets user info upon correct login
	 * VERIFY LOG IN BEFORE CALLING THIS METHOD
	 * @param user
	 */
	public void logIn(User user) {
		
	}
	
	/**
	 * erases all user and account info
	 */
	public void logOut() {
		
	}
	
	/**
	 * sets current account info
	 * @param account
	 */
	public void setAccount(Account account) {
		
	}
	
	/**
	 * erases only current account info
	 */
	public void unsetAccount() {
		
	}
	
	/**
	 * getter for isLoggedIn
	 * @return isLoggedIn
	 */
	public boolean isLoggedIn() {
		return false;
	}
	
	/**
	 * getter for username
	 * @return username
	 */
	public String getUsername() {
		return null;
	}
	
	/**
	 * getter for user id
	 * @return userId
	 */
	public int getUserId() {
		return 0;
	}
	
	/**
	 * gets current user with id userId
	 * @return current user
	 */
	public User getUser() {
		return null;
	}
	
	/**
	 * getter for hasCurrentAccount
	 * @return hasCurrentAccount
	 */
	public boolean hasCurrentAccount() {
		return false;
	}
	
	/**
	 * getter for account name
	 * @return accountName
	 */
	public String getAccountName() {
		return null;
	}
	
	/**
	 * getter for account id
	 * @return accountId
	 */
	public int getAccountId() {
		return 0;
	}
	
	/**
	 * gets account with id accountId
	 * @return account
	 */
	public Account getAccount() {
		return null;
	}
	
}
