package com.CeramicKoala.cs2340.model;

/**
 * DatabaseException is an Exception specific to database errors.
 * It is simple now, but has been written for the purpose of
 * expansion.
 * @author Benjamin Newcomer
 */
public class DatabaseException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DatabaseException(String message) {
		super(message);
	}
	
	public DatabaseException() {
		this("unknown database error");
	}
	

	
	
}
