package com.CeramicKoala.cs2340.model;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;

/**
 * ReportGenerator generates reports for users. So far
 * the class supports only Spending Report
 * @author Benjamin Newcomer
 */
public class ReportGenerator {
	
	enum ReportType {SPENDING_REPORT};
	TransactionOpenHelper transactionHelper;
	AccountOpenHelper accountHelper;
	User user;
	ReportType type;
	
	/**
	 * constructor
	 * @param context
	 * @param user
	 */
	public ReportGenerator(Context context, User user) {
		this.user = user;
		transactionHelper = new TransactionOpenHelper(context);
		accountHelper = new AccountOpenHelper(context);
	}
	
	/**
	 * generates a report based on input report type
	 * @param type
	 * @return
	 * @throws DatabaseException 
	 * @throws ParseException 
	 */
	public List<Transaction> generateReport(ReportType type) throws ParseException, DatabaseException {
		List<Transaction> report = null;
		
		switch (type) {
		case SPENDING_REPORT:
			report = generateSpendingReport();
			break;
		default:
			break;
		}
		
		return report;
	}
	
	private List<Transaction> generateSpendingReport() throws ParseException, DatabaseException {
		List<Transaction> report = new ArrayList<Transaction>();
		
		//get all transactions that are withdrawals
		for (Transaction t : getAllTransactions()) {
			if (t.typeToInt() == 1) {
				report.add(t);
			}
		}
		
		Collections.sort(report);
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
