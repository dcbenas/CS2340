package com.CeramicKoala.cs2340.test;


import org.junit.Before;
import org.junit.Test;

import android.test.AndroidTestCase;

import com.CeramicKoala.cs2340.model.DatabaseOpenHelper;
import com.CeramicKoala.cs2340.model.LoginOpenHelper;
import com.CeramicKoala.cs2340.model.User;

public class LoginOpenHelperTest extends AndroidTestCase {

	DatabaseOpenHelper<User> loginHelper;
	
	@Before
	protected void setUp() throws Exception {
		super.setUp();
		loginHelper = new LoginOpenHelper(getContext());
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
