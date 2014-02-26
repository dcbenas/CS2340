package com.CeramicKoala.cs2340.model;

import java.util.ArrayList;

/**
 * this class represents a single user
 * accounts (or account Id's will be 
 * added later in development)
 * 
 * @author Benjamin Newcomer
 *
 */
public class User extends DatabaseElement {
	
	private String password;
	private String fullName;
	private ArrayList<Account> accounts;
	
	/**
	 * constructor
	 * @param fullName
	 * @param username
	 * @param password
	 */
	public User(String fullName, String username, String password) {
		this.fullName = fullName;
		this.name = username;
		this.password = password;
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
		return getName();
	}
	
	/**
	 * returns a list of accounts
	 */
	public ArrayList<Account> getAccounts() {
		return accounts;
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
	 * @param acc
	 */
	public void addAccount(Account acc) {
		accounts.add(acc);
	}
	
	/**
	 * returns string representation of user
	 */
	public String toString() {
		StringBuilder out = new StringBuilder("user: ");
		out.append("fullName=" + fullName + ", ");
		out.append("username=" + name + ", ");
		out.append("password=" + password + ", ");
		out.append("id=" + id + ".");
		
		return out.toString();
	}
	
	@Override
	public boolean equals(Object o) {
		return (this.name.equals(((User) o).getUsername()));
	}
}
