package com.CeramicKoala.cs2340.activities;

import com.CeramicKoala.cs2340.R;
import com.CeramicKoala.cs2340.R.layout;
import com.CeramicKoala.cs2340.R.menu;
import com.CeramicKoala.cs2340.model.Account;
import com.CeramicKoala.cs2340.model.AccountOpenHelper;
import com.CeramicKoala.cs2340.model.DatabaseException;
import com.CeramicKoala.cs2340.model.LoginOpenHelper;
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
	
	private AccountOpenHelper accountHelper;
	private AlertDialog invalidNumber;
	private AlertDialog accountExists;
	private AlertDialog isEmpty;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		accountHelper = new AccountOpenHelper(this);
		setContentView(R.layout.activity_account_registration);
		invalidNumber = setUpAlertDialog("Error", "That is an invalid number", false);
		accountExists = setUpAlertDialog("Error", "That account already exists", false);
		isEmpty = setUpAlertDialog("Error",getString(R.string.account_registration_field_empty), false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.account_registration, menu);
		return true;
	}
	
	@Override
	protected Intent getIntent(Class<?> activityClass) {
		Intent intent = new Intent(this, activityClass);
		intent.putExtra(FROM_MAIN, false);
		return intent;
	}
	

	public void createAccount(View view) {
		//TODO trying to create invalid account doesn't throw/handle exception
		EditText field_name = (EditText) findViewById(R.id.field_accountName);
		String name = field_name.getText().toString();
		
		EditText field_startingBalance = (EditText) findViewById(R.id.field_startingBalance);
		String startingBalance = field_startingBalance.getText().toString();
		
		EditText field_interestRate = (EditText) findViewById(R.id.field_interestRate);
		String interestRate = field_interestRate.getText().toString();
		if (name.isEmpty() || startingBalance.isEmpty() || interestRate.isEmpty())
			isEmpty.show();
		else {
			try {
				accountHelper.addElement(new Account(0, loginHelper.getCurrentUser().getId(),name, new Double(startingBalance), new Double(interestRate)));
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
}
