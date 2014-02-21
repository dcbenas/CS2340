package com.CeramicKoala.cs2340.model;

/**
 * this class represents a single user
 * accounts (or account Id's will be 
 * added later in development)
 * 
 * @author Benjamin Newcomer
 *
 */
public class User extends DatabaseElement {
	//TODO add account field (array of accounts) and getter/setters
	
	private String password;
	private String fullName;
	
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
	 * setter for fullName
	 * @param fullName
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
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
}
