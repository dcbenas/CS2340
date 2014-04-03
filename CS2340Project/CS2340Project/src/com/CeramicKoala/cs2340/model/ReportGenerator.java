package com.CeramicKoala.cs2340.model;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
//import android.content.Context;

/**
 * ReportGenerator generates reports for users. So far
 * the class supports only Spending Report
 * @author Benjamin Newcomer
 */
public class ReportGenerator {
	
	public enum ReportType {SPENDING_REPORT, DEPOSIT_REPORT, TRANSACTION_HISTORY, ACCOUNT_LISTING};
	TransactionOpenHelper transactionHelper;
	AccountOpenHelper accountHelper;
	SessionManager sessionManager;
	User user;
	ReportType type;
	
	/**
	 * constructor
	 * @param context
	 * @param user
	 */
	public ReportGenerator(Activity activity) {
		transactionHelper = new TransactionOpenHelper(activity);
		accountHelper = new AccountOpenHelper(activity);
		sessionManager = new SessionManager(activity);

		this.user = sessionManager.getUser();
		
	}
	
	/**
	 * generates a report based on input report type
	 * @param type
	 * @return
	 * @throws DatabaseException 
	 * @throws ParseException 
	 */
	public List<Transaction> generateReport(ReportType type, Date beginning, Date end, int accountID) throws ParseException, DatabaseException {
		List<Transaction> report = null;
		
		switch (type) {
			case SPENDING_REPORT:
				report = generateSpendingReport(beginning, end);
				break;
			
			case DEPOSIT_REPORT:
				report = generateDepositReport(beginning, end);
				break;
			
			case TRANSACTION_HISTORY:
				report = generateTransactionHistory(beginning, end, accountID);
				break;
		
			default:
				break;
		}
		
		return report;
	}
	
	public List<Account> generateAccountListingReport() throws DatabaseException, ParseException {
		return (accountHelper.getAccountsForUser(user));
	}
	
	
	/**
	 * Generates a double array with the total income, total expenses, and flow
	 * TODO - add date functionality
	 * 
	 * @return double array with 3 relevant values
	 * @throws DatabaseException
	 * @throws ParseException
	 */
	public double[] generateCashFlowReport() throws DatabaseException, ParseException {
		List<Transaction> transList = getAllTransactions();
		double[] output = new double[3];
		double income = 0.0;
		double expenses = 0.0;
		for (Transaction t: transList) {
			if (t.typeToInt() <= 4) {
				income = income + t.getAmount();
			} else {
				expenses = expenses + t.getAmount();
			}
			
		}
		double flow = income - expenses;
		output[0] = income;
		output[1] = expenses;
		output[2] = flow;
		return output;
	}
	
	public String getTitles() {
		StringBuilder output = new StringBuilder();
		output.append("Income" + "\n");
		output.append("Expenses" + "\n");
		output.append("Cash Flow" + "\n");
		return (output.toString());
	}
	
	public String formatValues(double[] report) {
		StringBuilder output = new StringBuilder();
		output.append(report[0] + "\n");
		output.append(report[1] + "\n");
		output.append(report[2] + "\n");
		return (output.toString());
	}
	
	@SuppressWarnings("unchecked")
	private List<Transaction> generateSpendingReport(Date beginning, Date end) throws ParseException, DatabaseException {
		List<Transaction> report = new ArrayList<Transaction>();
		//get all transactions that are withdrawals
		for (Transaction t : getAllTransactions()) {
			if (t.typeToInt() == 1) {
				report.add(t);
			}
		}
		Collections.sort(report);
		//remove transactions outside date window
		Iterator<Transaction> i = report.iterator();
		while (i.hasNext()) {
			Transaction t = i.next();
			if ((beginning.compareTo(t.getDate()) > 0)
					|| (end.compareTo(t.getDate()) < 0)) {
				i.remove();
			}
		}
		
		return report;
	}
	
	@SuppressWarnings("unchecked")
	private List<Transaction> generateDepositReport(Date beginning, Date end) throws ParseException, DatabaseException {
		List<Transaction> report = new ArrayList<Transaction>();
		
		//get all transactions that are withdrawals
		for (Transaction t : getAllTransactions()) {
			if (t.typeToInt() == 0) {
				report.add(t);
			}
		}
		
		Collections.sort(report);
		
		//remove transactions outside date window
		Iterator<Transaction> i = report.iterator();
		while (i.hasNext()) {
			Transaction t = i.next();
			if ((beginning.compareTo(t.getDate()) > 0)
					|| (end.compareTo(t.getDate()) < 0)) {
				i.remove();
			}
		}
		
		return report;
	}
	
	@SuppressWarnings("unchecked")
	private List<Transaction> generateTransactionHistory(Date beginning, Date end, int accountID) throws ParseException, DatabaseException {
		List<Transaction> report = transactionHelper.getAllElements(accountID);
		Collections.sort(report);
		
		//remove transactions outside date window
		Iterator<Transaction> i = report.iterator();
		while (i.hasNext()) {
			Transaction t = i.next();
			if ((beginning.compareTo(t.getDate()) > 0)
					|| (end.compareTo(t.getDate()) < 0)) {
				i.remove();
			}
		}
		
		
		//System.out.println(report);
		return report;
	}
	
	
	private List<Transaction> getAllTransactions() throws DatabaseException, ParseException {
		List<Transaction> allTransactions = new ArrayList<Transaction>();
		List<Account> accounts = accountHelper.getAccountsForUser(user);
		
		for (Account account : accounts) {
			List<Transaction> transactions = 
					transactionHelper.getAllElements(account.getAccountId());
			allTransactions.addAll(transactions);
		}
		
		return allTransactions;
	}

}
