package com.CeramicKoala.cs2340.model;

public class DatabaseException extends Exception {

	public DatabaseException(String message) {
		super(message);
	}
	
	public DatabaseException() {
		this("unknown database error");
	}
	

	
	
}
