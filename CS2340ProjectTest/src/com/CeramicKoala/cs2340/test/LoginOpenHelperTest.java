package com.CeramicKoala.cs2340.test;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.CeramicKoala.cs2340.model.DatabaseException;
import com.CeramicKoala.cs2340.model.LoginOpenHelper;
import com.CeramicKoala.cs2340.model.User;

import android.test.AndroidTestCase;
import android.util.Log;

public class LoginOpenHelperTest extends AndroidTestCase {
	
	LoginOpenHelper loginHelper;
	User testUser;
	
	@Before
	protected void setUp() throws Exception {
		super.setUp();
		loginHelper = new LoginOpenHelper(getContext());
		testUser = new User("name", "username", "password");
		loginHelper.resetTable();
	}
	
	@After
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	@Test
	public void testAddElement() throws DatabaseException {
		User user = loginHelper.addElement(testUser);
		assertNotNull(user.getUsername());
		assertEquals(testUser, user);
		loginHelper.resetTable();
	}
	
	@Test
	public void testUpdateElement() throws DatabaseException {
		loginHelper.addElement(testUser);
		User updatedUser = loginHelper.getElementByName(testUser.getUsername());
		assertEquals(1, updatedUser.getId());
		updatedUser.setFullName("bob");
		User user = loginHelper.updateElement(updatedUser);
		Log.d("LoginOpenHelperTest#testUpdateElement", user.getFullName());
		assertNotNull(user.getUsername());
		assertEquals(updatedUser, user);
		loginHelper.resetTable();
	}
	
	@Test
	public void testDeleteElement() throws DatabaseException {
		loginHelper.addElement(testUser);
		User userToDelete = loginHelper.getElementByName(testUser.getUsername());
		boolean deleted = loginHelper.deleteElement(userToDelete);
		assertTrue(deleted);
		loginHelper.resetTable();
	}
	
	@Test
	public void testGetElementByName() throws DatabaseException {
		loginHelper.addElement(testUser);
		User user = loginHelper.getElementByName(testUser.getUsername());
		assertEquals(testUser, user);
		loginHelper.resetTable();
	}
	
	@Test
	public void testGetAllElements() throws DatabaseException {
		User testUser2 = new User("user2", "username2", "pass2");
		loginHelper.addElement(testUser);
		loginHelper.addElement(testUser2);
		List<User> users = loginHelper.getAllElements();
		assertEquals(2, users.size());
		assertEquals(testUser, users.get(0));
		assertEquals(testUser2, users.get(1));
		loginHelper.resetTable();
	}
}
