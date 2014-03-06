package com.CeramicKoala.cs2340.model;

import java.util.ArrayList;
import java.util.List;

/**
 * this class represents a single user. 
 * @author Benjamin Newcomer
 *
 */
public class User extends DatabaseElement {
	
	private String username;
	private String password;
	private String fullName;
	private List<Integer> accounts;
	
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
		accounts = new ArrayList<Integer>();
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
	 * getter for accounts
	 * @return accounts
	 */
	public List<Integer> getAccounts() {
		return accounts;
	}
	
	/**
	 * returns size of accounts list
	 * @return size of accounts
	 */
	public int getAccountSize() {
		return accounts.size();
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
	 * @param accountId
	 */
	public void addAccount(int accountId) {
		accounts.add(accountId);
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
		if (o == null) {
			return false;
		}
		if (!(o instanceof User)) {
			return false;
		}
		
		return (this.username.equals(((User) o).getUsername()));
	}
}
