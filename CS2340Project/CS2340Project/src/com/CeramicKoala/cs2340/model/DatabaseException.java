package com.CeramicKoala.cs2340.model;

public class DatabaseException extends Exception {
	
	private String message;
	
	public DatabaseException() {
		message = "unknown database error";
	}
	
	public DatabaseException(String message) {
		this.message = message;
	}
}
