package com.CeramicKoala.cs2340.activities;

import java.text.NumberFormat;

import com.CeramicKoala.cs2340.R;
import com.CeramicKoala.cs2340.model.Account;
import com.CeramicKoala.cs2340.model.AlertDialogManager;
import com.CeramicKoala.cs2340.model.DatabaseException;
import com.CeramicKoala.cs2340.model.AccountOpenHelper;
import com.CeramicKoala.cs2340.model.SessionManager;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class AccountHomeActivity extends Activity {
	
	private AccountOpenHelper myHelper;
	private NumberFormat currencyFormatter;
	protected AlertDialogManager alertManager;
	private SessionManager sessionManager;
	
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_home);
		
		// instantiate all helper objects
		myHelper = new AccountOpenHelper(this);
		sessionManager = new SessionManager(this);
		//DEPRECATED intent = getIntent();
		alertManager = new AlertDialogManager(this);
		
		// update view if user is logged in
		if (sessionManager.hasCurrentAccount()) {
			
			Account currentAccount = sessionManager.getAccount();
			
			currencyFormatter = NumberFormat.getCurrencyInstance();
			double balance = currentAccount.getBalance();
			String balanceString = currencyFormatter.format(balance);
			
			
			TextView loginMessageTextView = (TextView) findViewById(R.id.login_message);
			String loginMessage = currentAccount.getName();
			loginMessageTextView.setText(loginMessage);
			
			TextView balanceMessageTextView = (TextView) findViewById(R.id.current_balance);
			String balanceMessage = getString(R.string.balance_message) + " "+ balanceString;
			balanceMessageTextView.setText(balanceMessage);
		} else {
			
			alertManager.generateAlertDialog(
					AlertDialogManager.AlertType.ACCOUNT_DOES_NOT_EXIST)
					.show();
		}	
			
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.account_registration, menu);
		return true;
	}
	
	protected Intent getIntent(Class<?> activityClass) {
		
		Intent intent = new Intent(this, activityClass);
		return intent;
	}
	
	public void startTransaction(View view) {
		
		startActivity(getIntent(TransactionActivity.class));
	}
	
	public void generateReport(View view) {
		
		startActivity(getIntent(ReportActivity.class));
	}
}


