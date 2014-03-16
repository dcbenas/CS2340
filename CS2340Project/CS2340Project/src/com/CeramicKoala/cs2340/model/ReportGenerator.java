package com.CeramicKoala.cs2340.model;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Context;

/**
 * ReportGenerator generates reports for users. So far
 * the class supports only Spending Report
 * @author Benjamin Newcomer
 */
public class ReportGenerator {
	
	public enum ReportType {SPENDING_REPORT, DEPOSIT_REPORT};
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
	public List<Transaction> generateReport(ReportType type, Date beginning, Date end) throws ParseException, DatabaseException {
		List<Transaction> report = null;
		
		switch (type) {
			case SPENDING_REPORT:
				report = generateSpendingReport(beginning, end);
				break;
			
			case DEPOSIT_REPORT:
				report = generateDepositReport(beginning, end);
				break;
		
			default:
				break;
		}
		
		return report;
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
