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
		List<Transaction> transList = new ArrayList<Transaction>();
		//get all transactions that are withdrawals
		for (Transaction t : getAllTransactions()) {
			if (t.typeToInt() == 1) {
				transList.add(t);
			}
		}
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
	public double[] generateSpendingReport(Date beginning, Date end) throws ParseException, DatabaseException {
		List<Transaction> report = new ArrayList<Transaction>();
		//get all transactions that are withdrawals
		for (Transaction t : getAllTransactions()) {
			//All withdrawals have type greater than 4
			if (t.typeToInt() > 4) {
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
		
		double[] output = new double[6];
		double other = 0;
		double food = 0;
		double rent = 0;
		double entertainment = 0;
		double clothing = 0;
		double total = 0;
		for (Transaction t: report) {
			switch (t.typeToInt()) {
			    case 5:
			    	other += t.getAmount();
			    	break;
			    	
			    case 6:
			    	food += t.getAmount();
			    	break;
			    	
			    case 7:
			    	rent += t.getAmount();
			    	break;
			    	
			    case 8:
			    	entertainment += t.getAmount();
			    	break;
			    	
			    case 9:
			    	clothing += t.getAmount();
			    	break;
			    	
			    default:
			    	System.out.println("Error!");
			    	break;
			}
		}
		total = other + food + rent + entertainment + clothing ;
		output[0] = food;
		output[1] = rent;
		output[2] = entertainment;
		output[3] = clothing;
		output[4] = other;
		output[5] = total;
		return output;
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
