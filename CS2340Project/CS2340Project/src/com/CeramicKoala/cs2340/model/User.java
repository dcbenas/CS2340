package com.CeramicKoala.cs2340.model;

/**
 * this class represents a single user
 * accounts (or account Id's will be 
 * added later in development)
 * 
 * @author Benjamin Newcomer
 *
 */
public class User {
	private String username;
	private String password;
	private String fullName;
	private int id;
	
	public User(String fullName, String username, String password) {
		this.fullName = fullName;
		this.username = username;
		this.password = password;
	}
	
	/**
	 * accessor for username
	 * @return username
	 */
	public String getUsername() {
		return username;
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
	 * setter for Id
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * accessor for Id
	 * @return int Id
	 */
	public int getId() {
		return id;
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
		
		return out.toString();
	}
}
