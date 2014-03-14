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
import android.view.MenuItem;
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
		getMenuInflater().inflate(R.menu.logged_in, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		// Handle presses on the action bar items
	    switch (item.getItemId()) {
	       
	    	case R.id.log_out:
	        	sessionManager.logOut();
	            startActivity(new Intent(this, MainActivity.class));
	            return true;
	        
	        default:
	            return super.onOptionsItemSelected(item);
	    }
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
		
		// get account name from textview
		EditText field_name = (EditText) findViewById(R.id.field_accountName);
		String name = field_name.getText().toString();
		
		// get account starting balance from textview
		EditText field_startingBalance = (EditText) findViewById(R.id.field_startingBalance);
		String startingBalance = field_startingBalance.getText().toString();
		
		// get account interest rate from textview
		EditText field_interestRate = (EditText) findViewById(R.id.field_interestRate);
		String interestRate = field_interestRate.getText().toString();
		
		// check for empty fields
		if (name.isEmpty() || startingBalance.isEmpty() || interestRate.isEmpty()) {
			
			alertManager.generateAlertDialog(
					AlertDialogManager.AlertType.FIELD_IS_EMPTY)
					.show();
		} else {
			
			// fields are not empty
			try {
				
				//create new account and add to database
				Account newAccount = accountHelper.addElement(new Account(
								0, 
								loginHelper.getCurrentUser().getId(),
								name, 
								Double.valueOf(startingBalance),
								Double.valueOf(interestRate)));
				
				//update current user with new account id and update in database
				User updatedUser = sessionManager.getUser();
				updatedUser.addAccount(newAccount.getAccountId());
				loginHelper.updateElement(updatedUser);
				
				//TODO what does setting these flags do?
				// start LogInActivity
				Intent intent = new Intent(this, LogInActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			
			} catch (NumberFormatException e) {
				
				//TODO code should be redone so NumberFormatException is not a worry
				AlertDialog alert = alertManager.generateAlertDialog(
						AlertDialogManager.AlertType.ERROR_QUIT_FALSE);
				alert.setMessage("Invalid number format");
				alert.show();
				
				e.printStackTrace();
			} catch (DatabaseException e) {
				
				// account name already exists. cannot be duplicated
				alertManager.generateAlertDialog(
						AlertDialogManager.AlertType.ACCOUNT_ALREADY_EXISTS)
						.show();
				e.printStackTrace();
			}
		}
	}
}
