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
	
	private NumberFormat currencyFormatter;
	private AlertDialogManager alertManager;
	private SessionManager sessionManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_home);
		
		// instantiate all helper objects
		sessionManager = new SessionManager(this);
		//DEPRECATED intent = getIntent();
		alertManager = new AlertDialogManager(this);
		currencyFormatter = NumberFormat.getCurrencyInstance();
		
		// update view if user is logged in
		if (sessionManager.hasCurrentAccount()) {
			
			// get formatted account balance
			Account currentAccount = sessionManager.getAccount();
			String balanceString = currencyFormatter.format(currentAccount.getBalance());
			
			// update login message textview
			TextView loginMessageTextView = (TextView) findViewById(R.id.login_message);
			String loginMessage = currentAccount.getName();
			loginMessageTextView.setText(loginMessage);
			
			// update balance textview
			TextView balanceMessageTextView = (TextView) findViewById(R.id.current_balance);
			String balanceMessage = getString(R.string.balance_message) + " "+ balanceString;
			balanceMessageTextView.setText(balanceMessage);
		} else {
			
			// there is no current account set
			alertManager.generateAlertDialog(
					AlertDialogManager.AlertType.ACCOUNT_DOES_NOT_EXIST)
					.show();
		}	
			
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.account_registration, menu);
		return true;
	}

// DEPRECATED
//	protected Intent getIntent(Class<?> activityClass) {
//		
//		Intent intent = new Intent(this, activityClass);
//		return intent;
//	}
	
	public void startTransaction(View view) {
		
		startActivity(new Intent(this, TransactionActivity.class));
	}
	
	public void generateReport(View view) {
		
		startActivity(new Intent(this, ReportActivity.class));
	}
}


