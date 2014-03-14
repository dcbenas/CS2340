package com.CeramicKoala.cs2340.activities;

import com.CeramicKoala.cs2340.R;
import com.CeramicKoala.cs2340.model.Account;
import com.CeramicKoala.cs2340.model.AccountOpenHelper;
import com.CeramicKoala.cs2340.model.AlertDialogManager;
import com.CeramicKoala.cs2340.model.DatabaseException;
import com.CeramicKoala.cs2340.model.LoginOpenHelper;
import com.CeramicKoala.cs2340.model.SessionManager;
import com.CeramicKoala.cs2340.model.User;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class AccountRegistrationActivity extends Activity {
	
	private AccountOpenHelper accountHelper;
	private AlertDialogManager alertManager;
	private LoginOpenHelper loginHelper;
	private SessionManager sessionManager;
	//deprecated private AlertDialog invalidNumber;
	//deprecated private AlertDialog accountExists;
	//deprecated private AlertDialog isEmpty;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_registration);
		
		// instantiate all helper objects
		accountHelper = new AccountOpenHelper(this);
		loginHelper = new LoginOpenHelper(this);
		alertManager = new AlertDialogManager(this);
		sessionManager = new SessionManager(this);

// DEPRECATED
//		invalidNumber = setUpAlertDialog("Error", "That is an invalid number", false);
//		accountExists = setUpAlertDialog("Error", "That account already exists", false);
//		isEmpty = setUpAlertDialog("Error",getString(R.string.account_registration_field_empty), false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.account_registration, menu);
		return true;
	}

// DEPRECATED
//	@Override
//	protected Intent getIntent(Class<?> activityClass) {
//		
//		Intent intent = new Intent(this, activityClass);
//		return intent;
//	}
	

	public void createAccount(View view) {
		
		//TODO instead of catching a NumberFormatException, ensure that input is a number
		//by using an appropriate input object (number pad)
		EditText field_name = (EditText) findViewById(R.id.field_accountName);
		String name = field_name.getText().toString();
		
		EditText field_startingBalance = (EditText) findViewById(R.id.field_startingBalance);
		String startingBalance = field_startingBalance.getText().toString();
		
		EditText field_interestRate = (EditText) findViewById(R.id.field_interestRate);
		String interestRate = field_interestRate.getText().toString();
		
		if (name.isEmpty() || startingBalance.isEmpty() || interestRate.isEmpty())
			
			alertManager.generateAlertDialog(
					AlertDialogManager.AlertType.FIELD_IS_EMPTY)
					.show();
		else {
			
			try {
				
				//create new account and add to database
				Account newAccount = accountHelper.addElement(new Account(
								0, 
								loginHelper.getCurrentUser().getId(),
								name, 
								Double.valueOf(startingBalance),
								Double.valueOf(interestRate)));
				
				//update user with new account id
				User updatedUser = sessionManager.getUser();
				updatedUser.addAccount(newAccount.getAccountId());
				loginHelper.updateElement(updatedUser);
				
				//TODO what does setting these flags do?
				Intent intent = new Intent(this, LogInActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				
				startActivity(intent);
			
			} catch (NumberFormatException e) {
				
				//TODO code should be redone so NumberFormatException is not a worry
				AlertDialog alert = alertManager.generateAlertDialog(
						AlertDialogManager.AlertType.ERROR);
				alert.setMessage("Invalid number format");
				alert.show();
				
				e.printStackTrace();
			} catch (DatabaseException e) {
				
				alertManager.generateAlertDialog(
						AlertDialogManager.AlertType.ACCOUNT_ALREADY_EXISTS)
						.show();
				e.printStackTrace();
			}
		}
	}
}
