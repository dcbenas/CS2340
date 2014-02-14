package com.example.cs2340project;

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
	private int id;
	
	public User(String username, String password) {
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
}
