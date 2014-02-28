package com.CeramicKoala.cs2340.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The Account represents a bank account. TODO: child classes for each type 
 * of account (checking, savings, etc) will be created. Account will be set 
 * to abstract when child classes are written.
 * @author Benjamin Newcomer
 */
public class Account extends DatabaseElement {
	
	private int userId;
	private double balance;
	private double interestRate;
	private String accountName;
	
	/**
	 * minimal constructor
	 * @param accountId
	 * @param userId
	 */
	public Account(int userId) {
		this.userId = userId;
		
		id = 0;
		accountName = "";
		balance = 0;
		interestRate = 0;
	}
	
	/**
	 * full constructor
	 * @param accountId
	 * @param userId
	 * @param name
	 * @param balance
	 * @param interestRate
	 */
	public Account(int accountId, int userId, String name, double balance, double interestRate) {
		this(userId);
		
		this.id = accountId;
		this.accountName = name;
		this.balance = balance;
		this.interestRate = interestRate;
	}
	
	/**
	 * setter for account name
	 * @param name account name
	 */
	public void setName(String name) {
		accountName = name;
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
	 * getter for name
	 * @return accountName
	 */
	public String getName() {
		return accountName;
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
	
	/**
	 * accessor for id
	 * @return int id
	 */
	public int getUserId() {
		return userId;
	}
	
	/**
	 * accessor for account id (stored as id)
	 * @see DatabaseElement#getId()
	 * @return account id
	 */
	public int getAccountId() {
		return getId();
	}
	
	
	@Override
	public String toString() {
		StringBuilder out = new StringBuilder();
		out.append("name: " + accountName + ", ");
		out.append("userId: " + userId + ", ");
		out.append("accountId: " + id + ", ");
		out.append("balance: " + balance + ", ");
		out.append("interestRate: " + interestRate + ", ");
		return out.toString();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (!(o instanceof Account)) {
			return false;
		}
		
		Account a = (Account) o;
		return (this.accountName.equals(a.getName()));
	}

	
}
