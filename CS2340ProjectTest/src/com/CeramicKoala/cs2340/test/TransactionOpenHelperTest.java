package com.CeramicKoala.cs2340.test;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.CeramicKoala.cs2340.model.Account;
import com.CeramicKoala.cs2340.model.AccountOpenHelper;
import com.CeramicKoala.cs2340.model.DatabaseException;
import com.CeramicKoala.cs2340.model.LoginOpenHelper;
import com.CeramicKoala.cs2340.model.Transaction;
import com.CeramicKoala.cs2340.model.TransactionOpenHelper;
import com.CeramicKoala.cs2340.model.User;

import android.test.AndroidTestCase;
import android.util.Log;

public class TransactionOpenHelperTest extends AndroidTestCase {

	LoginOpenHelper loginHelper;
	AccountOpenHelper accountHelper;
	TransactionOpenHelper transactionHelper;
	User testUser;
	Account testAccount;
	Transaction testTransaction;
	Calendar calendar;
	
	@Before
	protected void setUp() throws Exception {
		super.setUp();
		
		transactionHelper = new TransactionOpenHelper(getContext());
		accountHelper = new AccountOpenHelper(getContext());
		loginHelper = new LoginOpenHelper(getContext());
		
		testUser = new User("name", "username", "password");
		testUser.setId(1);
		testAccount = new Account(1);
		testAccount.setId(1);
		calendar = Calendar.getInstance();
		calendar.set(2014, 2, 28);
		testTransaction = new Transaction(
				testAccount.getAccountId(), 
				Transaction.TransactionType.DEPOSIT, 
				100, 
				calendar.getTime());
		
		loginHelper.resetTable();
		accountHelper.resetTable();
		transactionHelper.resetTable();
		
		loginHelper.addElement(testUser);
		accountHelper.addElement(testAccount);
	}
	
	@Test
	public void testAddElement() throws DatabaseException {
		Transaction transaction = transactionHelper.addElement(testTransaction);
		assertEquals(testTransaction, transaction);
		
		//see if adding transaction updated account balance
		Account account = accountHelper.getElementById(testAccount.getAccountId());
		double expectedBalance = testAccount.getBalance() + transaction.getAmount();
		assertEquals(expectedBalance, account.getBalance());
	}
	
	@Test
	public void testDeleteElement() throws DatabaseException, ParseException {
		try {
			transactionHelper.addElement(testTransaction);
		} catch (DatabaseException e) {
			//this just means transaction is already there, which is okay
		}
		
		List<Transaction> transToDelete = transactionHelper.getAllElements(testAccount.getAccountId());
		assertEquals(1, transToDelete.size());
		Transaction toDelete = transToDelete.get(0);
		testTransaction.setId(toDelete.getId());
		assertEquals(testTransaction, toDelete);
		
		boolean deleted = transactionHelper.deleteElement(toDelete);
		assertTrue(deleted);
	}
	
	@Test
	public void testGetAllElements() throws DatabaseException, ParseException {
		try {
			transactionHelper.addElement(testTransaction);
		} catch (DatabaseException e) {
			//this just means transaction is already there, which is okay
		}
		
		Transaction testTrans2 = new Transaction(
				testAccount.getAccountId(), 
				Transaction.TransactionType.DEPOSIT, 
				100, 
				calendar.getTime());
		
		transactionHelper.addElement(testTrans2);
		
		List<Transaction> transactions = transactionHelper.getAllElements();
		assertEquals(2, transactions.size());
		
		testTransaction.setId(transactions.get(0).getId());
		assertEquals(testTransaction, transactions.get(0));
		testTrans2.setId(transactions.get(1).getId());
		assertEquals(testTrans2, transactions.get(1));
	}


}
