package com.CeramicKoala.cs2340.test;


import org.junit.Before;
import org.junit.Test;

import android.test.AndroidTestCase;

import com.CeramicKoala.cs2340.model.DatabaseOpenHelper;
import com.CeramicKoala.cs2340.model.LoginOpenHelper;
import com.CeramicKoala.cs2340.model.User;

public class DatabaseOpenHelperTest extends AndroidTestCase {

	DatabaseOpenHelper<User> loginHelper;
	
	@Before
	protected void setUp() throws Exception {
		super.setUp();
		loginHelper = new LoginOpenHelper(getContext());
	}
	
	@Test
	public void testAddElement() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testUpdateElement() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testDeleteElement() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testGetElementByName() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testGetElementById() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testAllElements() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testGetTableSize() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testGetTableInfo() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testResetDatabase() {
		fail("Not yet implemented");
	}

}
