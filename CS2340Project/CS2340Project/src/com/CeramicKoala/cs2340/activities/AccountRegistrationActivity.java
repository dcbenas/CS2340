package com.CeramicKoala.cs2340.activities;

import com.CeramicKoala.cs2340.R;
import com.CeramicKoala.cs2340.R.layout;
import com.CeramicKoala.cs2340.R.menu;
import com.CeramicKoala.cs2340.model.Account;
import com.CeramicKoala.cs2340.model.AccountOpenHelper;
import com.CeramicKoala.cs2340.model.DatabaseException;
import com.CeramicKoala.cs2340.model.User;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class AccountRegistrationActivity extends AccountManagementActivity {
	
	//TODO Inseok - selecting account from spinner transitions to account activity
	
	private User user;
	private AccountOpenHelper accountHelper;
	private AlertDialog invalidNumber;
	private AlertDialog accountExists;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		accountHelper = new AccountOpenHelper(this);
		setContentView(R.layout.activity_account_registration);
		alertDialog = setUpAlertDialog("Error", getString(R.string.log_in_error_no_account));
		invalidNumber = setUpAlertDialog("Error", "That is an invalid number");
		accountExists = setUpAlertDialog("Error", "That account already exists");
		
		try {
			//get user from database
			user = loginHelper.getElementByName(username);
		} catch (DatabaseException e) {
			Log.d("AccountRegisterActivity.get_user", e.getMessage());
			alertDialog.show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.account_registration, menu);
		return true;
	}
	
	@Override
	protected Intent getIntent(Class<?> activityClass) {
		
		final String USERNAME = getString(R.string.username_constant);
		final String PASSWORD = getString(R.string.password_constant);
		Intent intent = new Intent(this, activityClass);
		
		intent.putExtra(USERNAME, username);
		intent.putExtra(PASSWORD, password);
		
		return intent;
	}

	public void createAccount(View view) {
		//TODO trying to create invalid account doesn't throw/handle exception
		System.out.println("Works");
		EditText field_name = (EditText) findViewById(R.id.field_accountName);
		String name = field_name.getText().toString();
		
		EditText field_startingBalance = (EditText) findViewById(R.id.field_startingBalance);
		String startingBalance = field_startingBalance.getText().toString();
		
		EditText field_interestRate = (EditText) findViewById(R.id.field_interestRate);
		String interestRate = field_interestRate.getText().toString();
		
		try {
			accountHelper.addElement(new Account(0, user.getId(),name, new Double(startingBalance), new Double(interestRate)));
			startActivity(getIntent(LogInActivity.class));
		} catch (NumberFormatException e) {
			invalidNumber.show();
			e.printStackTrace();
		} catch (DatabaseException e) {
			accountExists.show();
			e.printStackTrace();
		}
	}
}
