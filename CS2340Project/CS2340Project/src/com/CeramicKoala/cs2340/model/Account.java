package com.CeramicKoala.cs2340.model;

/**
 * The Account represents a bank account. TODO: child classes for each type of account (checking, savings, etc)
 * will be created. Account will be set to abstract when child classes are written.
 * @author Benjamin Newcomer
 */
public class Account {
	
	//the Id of the user associated with the account
	private int userId;
	//name of the account (selected by user)
	private String name;
	private double balance;
	private double interestRate;
	
	/**
	 * constructor to be used when there is no interest rate
	 * @param name
	 * @param balance
	 */
	public Account(String name, double balance) {
		this.name = name;
		this.balance = balance;
		this.interestRate = 0;
	}
	
	/**
	 * setter for name
	 * @param name, new name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * setter for balance
	 * @param balance, new balance
	 */
	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	/**
	 * increments balance by a set amount
	 * @param amount, the amount to increment balance. 
	 * can be positive (deposits) or negative (withdrawals)
	 */
	public void incrementBalance(double amount) {
		this.balance += amount;
	}
	
	/**
	 * setter for interest rate
	 * @param interestRate, new interest rate
	 */
	public void setInterestRate(double interestRate) {
		this.interestRate = interestRate;
	}
	
	/**
	 * accessor for name
	 * @return String name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * accessor for balance
	 * @return double balance
	 */
	public double getBalance() {
		return balance;
	}
	
	/**
	 * accessor for interestRate
	 * @return double interestRate
	 */
	public double getInterestRate() {
		return interestRate;
	}
	
}
