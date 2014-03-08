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
		Calendar calendar = Calendar.getInstance();
		calendar.set(2014, 2, 28);
		testTransaction = new Transaction(1, 0, 100, calendar.getTime());
		
		loginHelper.resetTable();
		accountHelper.resetTable();
		transactionHelper.resetTable();
		
		loginHelper.addElement(testUser);
		accountHelper.addElement(testAccount);
	}
	
	@After
	protected void tearDown() throws Exception {
		loginHelper.resetTable();
		accountHelper.resetTable();
		transactionHelper.resetTable();
		super.tearDown();
	}
	
	@Test
	public void testAddElement() throws DatabaseException {
		Transaction transaction = transactionHelper.addElement(testTransaction);
		assertEquals(testTransaction, transaction);
		
		//see if adding transaction updated account balance
		Account account = accountHelper.getElementById(testAccount.getId());
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
		
		List<Transaction> transToDelete = transactionHelper.getAllElements(testAccount.getId());
		assertEquals(1, transToDelete.size());
		Transaction toDelete = transToDelete.get(0);
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
		
		Calendar c = Calendar.getInstance();
		Transaction testTrans2 = new Transaction(1, 0, 10, c.getTime());
		transactionHelper.addElement(testTransaction);
		transactionHelper.addElement(testTrans2);
		
		List<Transaction> transactions = transactionHelper.getAllElements();
		assertEquals(2, transactions.size());
		assertEquals(testTransaction, transactions.get(0));
		assertEquals(testTrans2, transactions.get(1));
	}


}
