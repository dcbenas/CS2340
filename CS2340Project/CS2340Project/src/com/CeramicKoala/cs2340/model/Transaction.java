package com.CeramicKoala.cs2340.model;

import java.text.DateFormatSymbols;
import java.util.Date;
import java.text.NumberFormat;

/**
 * Transaction represents a single account transaction. Transactions are
 * assigned to an account on creation through the accountId field. All
 * fields are final because Transaction is meant for record keeping and
 * should not be altered after creation.
 * @author Benjamin Newcomer
 */

@SuppressWarnings("rawtypes")
public class Transaction extends DatabaseElement implements Comparable {
	
	public enum TransactionType {
		DEPOSIT(0, "Deposit"), SALARY(0, "Salary"), GIFT(0, "Gift"), PARENTS(0, "Parents"), SCHOLARSHIP(0, "Scholarship"), 
		WITHDRAWAL(1, "Withdrawal"), FOOD(1, "Food"), RENT(1, "Rent"), ENTERTAINMENT(1, "Entertainment"), CLOTHING(1, "Clothing");
		
		private int id;
		private String type;
		
		TransactionType(int id, String type) {
			this.id = id;
			this.type = type;
		}
			
		public int toInt() {
				return id;
			}
		
		public String toString() {
			return type;
		}
	
	};
	private final TransactionType type;
	private final double amount;
	private final Date timestamp;
	private final int accountId;
	private final Date date;
		
	/**
	 * constructor
	 * @param accountId
	 * @param type
	 * @param amount
	 * @param date
	 */
	public Transaction(int accountId, int type, double amount, Date date) {
		this.accountId = accountId;
		this.type = getType(type);
		this.amount = amount;
		this.date = date;
		
		//set timestamp with current system time
		long currentTime = System.currentTimeMillis();
		timestamp = new Date(currentTime);
		
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
	public Transaction(int accountId, int type, double amount, Date date, Date timestamp, int id) {
		this.accountId = accountId;
		this.type = getType(type);
		this.amount = amount;
		this.date = date;
		this.id = id;
		this.timestamp = timestamp;
		
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
	 * returns an int for the type
	 * @return
	 */
	public int typeToInt() {
		return type.toInt();
	}
	
	/**
	 * returns the transaction type based on int type
	 * @param type
	 * @return
	 */
	private TransactionType getType(int type) {

		switch (type) {
		case 0:
			return TransactionType.DEPOSIT;
		case 1:
			return TransactionType.WITHDRAWAL;
		}
		return null;
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
	
	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (!(o instanceof Transaction)) {
			return false;
		}
		
		return (this.id == ((Transaction) o).getId());
	}
	
	@Override
	public int compareTo(Object o) {
		if (!(o instanceof Transaction)) {
			throw new IllegalArgumentException("cannot compare objects of different classes");
		}
		
		Transaction t = (Transaction) o;
		return this.timestamp.compareTo(t.getTimestamp());
	}
	
	@SuppressWarnings("deprecation")
	public String getDateString() {
		StringBuilder out = new StringBuilder();
		String stringMonth = new DateFormatSymbols().getMonths()[date.getMonth()];
		out.append("\n" + stringMonth + " ");
		out.append(date.getDate() + ", ");
		out.append((date.getYear() + 1900));
		return out.toString();
	}
	
	public String getAmountString() {
		StringBuilder out = new StringBuilder();
		NumberFormat format = NumberFormat.getCurrencyInstance();
		String formattedAmount = format.format(amount);
		out.append("\n" + formattedAmount);
		
		return out.toString();
	}
	
	@Override
	public String toString() {
		StringBuilder out = new StringBuilder();
		out.append(amount + "\n");
		
		return out.toString();
	}
}
