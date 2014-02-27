package com.CeramicKoala.cs2340.test;

import java.util.List;

import com.CeramicKoala.cs2340.R;
import com.CeramicKoala.cs2340.R.layout;
import com.CeramicKoala.cs2340.R.menu;
import com.CeramicKoala.cs2340.model.AccountOpenHelper;
import com.CeramicKoala.cs2340.model.DatabaseException;
import com.CeramicKoala.cs2340.model.DatabaseOpenHelper;
import com.CeramicKoala.cs2340.model.Account;
import com.CeramicKoala.cs2340.model.LoginOpenHelper;
import com.CeramicKoala.cs2340.model.User;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class AccountOpenHelperTest extends Activity {

	static TextView textView;
	static AccountOpenHelper accountHelper;
	static LoginOpenHelper loginHelper;
	static User testUser;
	static Account testAccount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_open_helper_test);
		
		//instantiate open helpers, user, and account and add test user to db
		accountHelper = new AccountOpenHelper(this);
		loginHelper = new LoginOpenHelper(this);
		try {
			loginHelper.resetTable();
			accountHelper.resetTable();
			
			testUser = new User("testUsername1", "testPass1", "testName1");
			loginHelper.addElement(testUser);
			testUser = loginHelper.getElementByName(testUser.getUsername());
			testAccount = new Account(testUser.getId());
			textView = (TextView) findViewById(R.id.account_open_helper_test);
			
			
			boolean testAddElement = testAddElement();
			boolean testUpdateElement = testUpdateElement();
			boolean testDeleteElement = testDeleteElement();
			boolean testGetElementById = testGetElementById();
			boolean testGetAllElements = testGetAllElements(); 
			boolean testGetTableSize = testGetTableSize();
			
			StringBuilder msg = new StringBuilder();
			if (testAddElement) msg.append("add element works \n");
			else msg.append("add element does not work \n");
			if (testUpdateElement) msg.append("update element works \n");
			else msg.append("update element does not work \n");
			if (testDeleteElement) msg.append("delete element works \n");
			else msg.append("delete element does not work \n");
			if (testGetElementById) msg.append("get element works \n");
			else msg.append("get element does not work \n");
			if (testGetAllElements) msg.append("get all elements works \n");
			else msg.append("get all element does not work \n");
			if (testGetTableSize) msg.append("get table size works \n");
			else msg.append("get table size does not work \n");
			
			textView.setText(msg.toString());
			
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.account_open_helper_test, menu);
		return true;
	}
	
	private boolean testAddElement() {
		try {
			Account account = accountHelper.addElement(testAccount);
			if (account.equals(testAccount)) {
				Log.d("testAddElement", "success");
				return true;
			} else {
				throw new Exception("accounts not equal");
			}
		} catch (Exception e) {
			Log.d("testAddElement", e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	
	private boolean testUpdateElement() {
		try {
			Account updatedTestAccount = testAccount;
			updatedTestAccount.setBalance(100);
			Account updatedAccount = accountHelper.updateElement(updatedTestAccount);
			if (updatedAccount.getBalance() == updatedTestAccount.getBalance()) {
				Log.d("testUpdateElement", "success");
				return true;
			} else {
				throw new Exception("updated fields do not match");
			}
		} catch (Exception e) {
			Log.d("testUpdateElement", e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	
	private boolean testDeleteElement() {
		try {
			boolean deleted = accountHelper.deleteElement(testAccount);
			if (deleted) {
				Log.d("testDeleteElement", "success");
				return true;
			} else {
				throw new Exception("problem deleting element");
			}
		} catch (Exception e) {
			Log.d("testDeleteElement", e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	
	private boolean testGetElementById() {
		try {
			Account retrievedAccount = accountHelper.getElementById(testAccount.getAccountId());
			if (retrievedAccount.equals(testAccount)) {
				Log.d("testGetElementById", "success");
				return true;
			} else {
				throw new Exception("updated fields do not match");
			}
		} catch (Exception e) {
			Log.d("testGetElementById", e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	
	private boolean testGetAllElements() {
		List<Account> accounts = accountHelper.getAllElements(testUser);
		if (accounts != null) {
			Log.d("testGetAllElements", "success");
			return true;
		} else {
			Log.d("testGetAllElements", "fail");
			return false;
		}
	}
	
	private boolean testGetTableSize() {
		int tableSize = accountHelper.getTableSize();
		if (tableSize > 0) {
			Log.d("testGetTableSize", "success");
			return true;
		} else {
			Log.d("testGetTableSize", "fail");
			return false;
		}
	}

}
