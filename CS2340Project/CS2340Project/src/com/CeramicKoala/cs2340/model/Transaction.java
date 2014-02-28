package com.CeramicKoala.cs2340.model;

import java.util.Date;
import java.sql.Timestamp;
import java.text.DateFormat;

/**
 * Transaction represents a single account transaction. Transactions are
 * assigned to an account on creation through the accountId field. All
 * fields are final because Transaction is meant for record keeping and
 * should not be altered after creation.
 * @author Benjamin Newcomer
 */
public class Transaction extends DatabaseElement {
	
	private enum TransactionType {
		INCOME ("Income"), GIFT ("Gift"), INTEREST("Interest"), 
		GROCERIES ("Groceries"), SPENDING_MONEY ("Spending money"), 
		DEPOSIT ("Unspecified Deposit"), WITHDRAWAL ("Unspecified Withdrawal");
		
		private final String type;
		
		private TransactionType(String s) {
			type = s;
		}
		
		public String toString() {
			return type;
		}
	};
	private final TransactionType type;
	private final double amount;
	private final Timestamp timestamp;
	private final int accountId;
	private final Date date;
		
	/**
	 * constructor
	 * @param accountId
	 * @param type
	 * @param amount
	 * @param date
	 */
	public Transaction(int accountId, String type, double amount, Date date) {
		this.accountId = accountId;
		this.type = TransactionType.valueOf(type);
		this.amount = amount;
		this.date = date;
		
		//set timestamp with current system time
		long currentTime = System.currentTimeMillis();
		timestamp = new Timestamp(currentTime);
		
	}
	
	/**
	 * constructor FOR DATABASEOPENHELPER USE ONLY
	 * @param accountId
	 * @param type
	 * @param amount
	 * @param date
	 * @param timestamp
	 * @param id
	 */
	public Transaction(int accountId, String type, double amount, Date date, Date timestamp, int id) {
		this.accountId = accountId;
		this.type = TransactionType.valueOf(type);
		this.amount = amount;
		this.date = date;
		this.id = id;
		this.timestamp = (Timestamp) timestamp;
		
	}
	
	/**
	 * getter for account id
	 * @return int account id
	 */
	public int getAccountId() {
		return accountId;
	}
	
	/**
	 * getter for type
	 * @return String type
	 */
	public String getType() {
		return type.toString();
	}
	
	/**
	 * getter for amount
	 * @return double amount
	 */
	public double getAmount() {
		return amount;
	}
	
	/**
	 * getter for timestamp for display
	 * @return Date timestamp
	 */
	public Date getTimestamp() {
		return timestamp;
	}
	
	/**
	 * getter for date
	 * @return Date date
	 */
	public Date getDate() {
		return date;
	}
	
}
