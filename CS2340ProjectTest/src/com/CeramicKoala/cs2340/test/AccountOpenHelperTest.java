package com.CeramicKoala.cs2340.test;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.CeramicKoala.cs2340.model.Account;
import com.CeramicKoala.cs2340.model.AccountOpenHelper;
import com.CeramicKoala.cs2340.model.DatabaseException;
import com.CeramicKoala.cs2340.model.LoginOpenHelper;
import com.CeramicKoala.cs2340.model.User;

import android.test.AndroidTestCase;
import android.util.Log;

public class AccountOpenHelperTest extends AndroidTestCase {

	AccountOpenHelper accountHelper;
	LoginOpenHelper loginHelper;
	Account testAccount;
	User testUser;
	
	@Before
	protected void setUp() throws Exception {
		super.setUp();
		accountHelper = new AccountOpenHelper(getContext());
		loginHelper = new LoginOpenHelper(getContext());
		testUser = new User("name", "username", "password");
		testUser.setId(1);
		testUser.addAccount(1);
		testAccount = new Account(1);
		testAccount.setId(1);
		accountHelper.resetTable();
	}
	
	@After
	protected void tearDown() throws Exception {
		loginHelper.resetTable();
		super.tearDown();
	}
	
	@Test
	public void testAddElement() throws DatabaseException {
		Account account = accountHelper.addElement(testAccount);
		assertEquals(1, account.getUserId());
		assertEquals(testAccount, account);
		accountHelper.resetTable();
	}
	
	@Test
	public void testUpdateElement() throws DatabaseException {
		accountHelper.addElement(testAccount);
		Account updatedAccount = testAccount;
		updatedAccount.setName("otherAccount");
		Account account = accountHelper.updateElement(updatedAccount);
		assertEquals(updatedAccount, account);
		accountHelper.resetTable();
	}
	
	@Test
	public void testDeleteElement() throws DatabaseException {
		accountHelper.addElement(testAccount);
		Account accountToDelete = accountHelper.getElementById(testAccount.getId());
		boolean deleted = accountHelper.deleteElement(accountToDelete);
		assertTrue(deleted);
		accountHelper.resetTable();
	}
	
	@Test
	public void testGetElementById() throws DatabaseException {
		accountHelper.addElement(testAccount);
		Account account = accountHelper.getElementById(testAccount.getId());
		assertEquals(account, testAccount);
		accountHelper.resetTable();
	}
	
	@Test
	public void testGetAllElements() throws DatabaseException {
		Account testAccount2 = new Account(1);
		testAccount2.setName("account2");
		accountHelper.addElement(testAccount);
		accountHelper.addElement(testAccount2);
		List<Account> accounts = accountHelper.getAccountsForUser(testUser);
		assertEquals(2, accounts.size());
		assertEquals(accounts.get(0), testAccount);
		assertEquals(accounts.get(1), testAccount2);
		accountHelper.resetTable();
	}

}
