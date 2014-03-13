package com.CeramicKoala.cs2340.activities;

import java.util.List;

import com.CeramicKoala.cs2340.R;
import com.CeramicKoala.cs2340.model.Account;
import com.CeramicKoala.cs2340.model.AccountOpenHelper;
import com.CeramicKoala.cs2340.model.DatabaseException;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
public class LogInActivity extends AccountManagementActivity implements OnItemSelectedListener {
	//TODO Inseok - alter spinner behavior so that selecting account from spinner starts account activity automatically
	//TODO Matthew - add "Generate Report" button that starts GenerateReportActivity
	//TODO David/Casey - Make our app look pro. Custom buttons and icons n such
	
	private AlertDialog wrongPassword, noAccount;
	private int chosenAccount;
	private AccountOpenHelper accountHelper;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		//setup
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log_in);
		alertDialog = setUpAlertDialog("Error", getString(R.string.log_in_error_no_account), true);
		wrongPassword = setUpAlertDialog("Error", getString(R.string.log_in_error_incorrect_password), true);
		noAccount = setUpAlertDialog("Error", getString(R.string.no_account),false);

		if (loginHelper.getCurrentUser() == null) {
			try {
				//get user from database
				loginHelper.getElementByName(username);
				//set textView text with successful login message or display alert dialog
				TextView loginMessageTextView = (TextView) findViewById(R.id.login_message);
				if (checkCred(loginHelper.getCurrentUser().getUsername(), loginHelper.getCurrentUser().getPassword())) {
					if (password.equals(loginHelper.getCurrentUser().getPassword())) {
						String loginMessage = getString(R.string.log_in_success) + " "+ loginHelper.getCurrentUser().getFullName();
						loginMessageTextView.setText(loginMessage);
					} else {
						wrongPassword.show();
					}
				}
				if(loginHelper.getCurrentUser().getAccountSize() != 0)
					updateAccountSpinner();
			} catch (DatabaseException e) {
				Log.d("LogInActivity.get_user", e.getMessage());
				alertDialog.show();
			}
			
		}
		else {
			TextView loginMessageTextView = (TextView) findViewById(R.id.login_message);
			String loginMessage = getString(R.string.log_in_success) + " "+ loginHelper.getCurrentUser().getFullName();
			loginMessageTextView.setText(loginMessage);
			loginHelper.updateElement(loginHelper.getCurrentUser());
			if(loginHelper.getCurrentUser().getAccountSize() != 0)
				updateAccountSpinner();
		}
		System.out.println(loginHelper.getCurrentUser().getUsername());
	}
	
	public void updateAccountSpinner() {
		accountHelper = new AccountOpenHelper(this);
		List<Account> accounts = null;
		try {
			accounts = accountHelper.getAccountsForUser(loginHelper.getCurrentUser());
			Object[] accountNames = new Object[accounts.size()];
			int counter = 0;
			for (Account account : accounts) {
				accountNames[counter++] = account.getName();
			}
			Spinner s = (Spinner) findViewById(R.id.account_spinner);
			ArrayAdapter<Object> adapter = new ArrayAdapter<Object>(this,android.R.layout.simple_spinner_item, accountNames);
			s.setAdapter(adapter);
			s.setOnItemSelectedListener(this);
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		System.out.println(accounts.size());
	}
	
	public void createAccount(View view) {
		startActivity(getIntent(AccountRegistrationActivity.class));
	}
	
	public void viewAccount(View view) {
		Intent intent = getIntent(AccountHomeActivity.class);
		String CHOSEN_ACCOUNT = getString(R.string.chosen_account_constant);
		List<Account> accounts = null;
		accountHelper.logout();
		if (loginHelper.getCurrentUser().getAccountSize() != 0) {
			try {
				accounts = accountHelper.getAccountsForUser(loginHelper.getCurrentUser());
			} catch (DatabaseException e) {
				e.printStackTrace();
			}
			int id = accounts.get(chosenAccount).getAccountId();
			intent.putExtra(CHOSEN_ACCOUNT, id);
			startActivity(intent);
		} else {
			noAccount.show();
		}
	}
	
	@Override
	protected Intent getIntent(Class<?> activityClass) {
		Intent intent = new Intent(this, activityClass);
		return intent;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		chosenAccount = position;
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// Doesn't do anything
	}

}
