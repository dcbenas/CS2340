package com.CeramicKoala.cs2340.activities;

import java.util.List;

import com.CeramicKoala.cs2340.R;
import com.CeramicKoala.cs2340.model.Account;
import com.CeramicKoala.cs2340.model.AccountOpenHelper;
import com.CeramicKoala.cs2340.model.AlertDialogManager;
import com.CeramicKoala.cs2340.model.DatabaseException;
import com.CeramicKoala.cs2340.model.SessionManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * this class handles login processing and communicating with the SQLite db
 * via the LoginOpenHelper class. Currently, this activity only responds to
 * the login button. The button adds the user to the db if a unique username is
 * entered. If the username already exists, the password is checked. If a correct
 * password is entered, then login is successful. If incorrect, login is 
 * unsuccessful.
 * @author Benjamin Newcomer
 * @version 1.0
 *
 */
public class LogInActivity extends Activity implements OnItemSelectedListener {
	
	//TODO Inseok - alter spinner behavior so that selecting account from spinner starts account 
	// activity automatically
	//TODO David/Casey - Make our app look pro. Custom buttons and icons n such
	

	//DEPRECATED private AlertDialog wrongPassword, noAccount;
	private List<Account> accounts;
	private AccountOpenHelper accountHelper;
	private SessionManager sessionManager;
	private AlertDialogManager alertManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		//setup
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log_in);
		
		//instantiate helper objects
		sessionManager = new SessionManager(this);
		alertManager = new AlertDialogManager(this);
		
		//DEPRECATED alertDialog = setUpAlertDialog("Error", getString(R.string.log_in_error_no_account), true);
		//DEPRECATED wrongPassword = setUpAlertDialog("Error", getString(R.string.log_in_error_incorrect_password), true);
		//DEPRECATED noAccount = setUpAlertDialog("Error", getString(R.string.no_account),false);

		//TODO please explain this block so that it can be updated to use SessionManager
		//TODO this is a GIANT if statment. should be refactored to be more readable
		//TODO replace reference to static current user usng SessionManager
		
		System.out.println("login activity: " + String.valueOf(sessionManager.isLoggedIn()));
		if (sessionManager.isLoggedIn()) {
			System.out.println("log in successful from inside Login activity");
			
			//set textView text with successful login message
			TextView loginMessageTextView = (TextView) findViewById(R.id.login_message);
			
			// update account spinner
			if(sessionManager.getUser().getAccountSize() != 0) {
				updateAccountSpinner();
			}
			
			// set welcome message
			String loginMessage = getString(R.string.log_in_success) + " "
					+ sessionManager.getUser().getFullName();
			loginMessageTextView.setText(loginMessage);
				
		}
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
//		if (loginHelper.getCurrentUser() == null) {
//			
//			try {
//				
//				//get user from database
//				loginHelper.getElementByName(username);
//				//set textView text with successful login message or display alert dialog
//				TextView loginMessageTextView = (TextView) findViewById(R.id.login_message);
//				
//				if (checkCred(loginHelper.getCurrentUser().getUsername(), 
//						loginHelper.getCurrentUser().getPassword())) {
//					
//					if (password.equals(loginHelper.getCurrentUser().getPassword())) {
//						
//						String loginMessage = getString(R.string.log_in_success) + " "+ loginHelper.getCurrentUser().getFullName();
//						loginMessageTextView.setText(loginMessage);
//					} else {
//						
//						alertManager.generateAlertDialog(
//								AlertDialogManager.AlertType.INCORRECT_PASSWORD)
//								.show();
//					}
//				}
//				
//				if(loginHelper.getCurrentUser().getAccountSize() != 0)
//					
//					updateAccountSpinner();
//			} catch (DatabaseException e) {
//				
//				Log.d("LogInActivity.get_user", e.getMessage());
//				alertManager.generateAlertDialog(
//						AlertDialogManager.AlertType.ACCOUNT_DOES_NOT_EXIST)
//						.show();
//			}
//			
//		} else {
//			
//			TextView loginMessageTextView = (TextView) findViewById(R.id.login_message);
//			String loginMessage = 
//					getString(R.string.log_in_success) + " "
//					+ loginHelper.getCurrentUser().getFullName();
//			loginMessageTextView.setText(loginMessage);
//			loginHelper.updateElement(loginHelper.getCurrentUser());
//			
//			if (loginHelper.getCurrentUser().getAccountSize() != 0) {
//				updateAccountSpinner();
//			}
//		}
//		
//		//TODO reaplce call to println with log.d() call
//		System.out.println(loginHelper.getCurrentUser().getUsername());
//	}
	
	public void updateAccountSpinner() {
		
		accountHelper = new AccountOpenHelper(this);
		accounts = null;
		
		try {
			
			// get list of current user's accounts and instantiate needed objects/counters 
			accounts = accountHelper.getAccountsForUser(sessionManager.getUser());
			Object[] accountNames = new Object[accounts.size()];
			int counter = 0;
			
			// loop through accounts and add account name to accountNames array
			for (Account account : accounts) {
				accountNames[counter++] = account.getName();
			}
			
			// update spinner with names from accountNames array
			Spinner s = (Spinner) findViewById(R.id.account_spinner);
			ArrayAdapter<Object> adapter = new ArrayAdapter<Object>(
					this,
					android.R.layout.simple_spinner_item, 
					accountNames);
			s.setAdapter(adapter);
			
			//set this to run callback when item is selected from spinner
			s.setOnItemSelectedListener(this);
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		//TODO use log.d() instead of println
		//System.out.println(accounts.size());
	}
	
	public void createAccount(View view) {
		
		startActivity(new Intent(this, AccountRegistrationActivity.class));
	}
	
	// TODO instead of selecting accounts then pressing a button, the app should
	// open the account upon clicking it within the spinner. (start the spinner on
	// a default message that does not go anywhere)
	public void viewAccount(View view) {
		
		Intent intent = new Intent(this, AccountHomeActivity.class);
		//DEPRECATED final String CHOSEN_ACCOUNT = getString(R.string.chosen_account_constant);
		
		if (sessionManager.hasCurrentAccount()) {
			
			startActivity(intent);
		} else {
			
			// user has not selected an account from spinner
			AlertDialog alert = alertManager.generateAlertDialog(
					AlertDialogManager.AlertType.ERROR_QUIT_FALSE);
			alert.setTitle("Account Error");
			alert.setMessage("Please select an account");
			alert.show();
			
		} 
	}
	
	public void getAccountListing(View view) {
		Intent intent = new Intent(this, AccountListingActivity.class);
		startActivity(intent);
	}
	
	public void getCashFlowReport(View view) {
		int followUp = 2;
		Intent intent = new Intent(this, ReportActivity.class);
		intent.putExtra("followUpDecision", followUp);
		startActivity(intent);
	}
	
	public void getDepositReport(View view) {
		int followUp = 3;
		Intent intent = new Intent(this, ReportActivity.class);
		intent.putExtra("followUpDecision", followUp);
		startActivity(intent);
	}
	
	public void getWithdrawalReport(View view) {
		int followUp = 0;
		Intent intent = new Intent(this, ReportActivity.class);
		intent.putExtra("followUpDecision", followUp);
		startActivity(intent);
	}

// DEPRECATED
//	@Override
//	protected Intent getIntent(Class<?> activityClass) {
//		
//		Intent intent = new Intent(this, activityClass);
//		return intent;
//	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		
		//get chosen account and set to current account in session manager
		int chosenAccountPos = position;
		Account chosenAccount = accounts.get(chosenAccountPos);
		sessionManager.setAccount(chosenAccount);
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		//It's required for implementation of OnItemSelectListener
	}

}
