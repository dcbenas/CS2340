package com.CeramicKoala.cs2340.model;

import java.util.HashMap;
import java.util.Map;

/**
 * this class represents a single user. 
 * @author Benjamin Newcomer
 *
 */
public class User extends DatabaseElement {
	
	private String username;
	private String password;
	private String fullName;
	private Map<String, Integer> accounts;
	
	/**
	 * constructor
	 * @param fullName
	 * @param username
	 * @param password
	 */
	public User(String fullName, String username, String password) {
		this.fullName = fullName;
		this.username = username;
		this.password = password;
		accounts = new HashMap<String, Integer>();
	}
	
	/**
	 * setter for account name
	 * @param name account name
	 */
	public void setName(String name) {
		username = name;
	}
	
	/**
	 * accessor for password
	 * @return password
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * accessor for fullName
	 * @return fullName
	 */
	public String getFullName() {
		return fullName;
	}
	
	/**
	 * accessor for username (stored as name)
	 * @see DatabaseElement#getId()
	 * @return username
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * returns a list of account names
	 * TODO change return type back to String[]
	 */
	public Object[] getAccounts() {
		if (accounts.isEmpty()) {
			Object[] out = {"no accounts"};
			return out;
		}
		return accounts.keySet().toArray();
	}
	
	public int getAccountSize() {
		return accounts.hashCode();
	}
	
	/**
	 * setter for fullName
	 * @param fullName
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	/**
	 * adds account to User
	 * should only be used by database helpers
	 * @param accountName
	 * @param accountId
	 */
	public void addAccount(String accountName, int accountId) {
		accounts.put(accountName, accountId);
	}
	
	/**
	 * returns string representation of user
	 */
	public String toString() {
		StringBuilder out = new StringBuilder("user: ");
		out.append("fullName=" + fullName + ", ");
		out.append("username=" + username + ", ");
		out.append("password=" + password + ", ");
		out.append("id=" + id + ".");
		out.append("accounts= " + getAccounts());
		
		return out.toString();
	}
	
	@Override
	public boolean equals(Object o) {
		return (this.username.equals(((User) o).getUsername()));
	}
}
