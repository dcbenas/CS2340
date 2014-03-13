package com.CeramicKoala.cs2340.activities;

import java.text.NumberFormat;

import com.CeramicKoala.cs2340.R;
import com.CeramicKoala.cs2340.model.DatabaseException;
import com.CeramicKoala.cs2340.model.AccountOpenHelper;

import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class AccountHomeActivity extends AccountManagementActivity {
	private AccountOpenHelper myHelper;
	private NumberFormat currencyFormatter;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myHelper = new AccountOpenHelper(this);
		intent = getIntent();
			
		String CHOSEN_ACCOUNT = getString(R.string.chosen_account_constant);
		
		try {
			if (myHelper.currentAccount == null)
				myHelper.getElementById(intent.getIntExtra(CHOSEN_ACCOUNT, 1));
			else
				myHelper.getCurrent();
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		currencyFormatter = NumberFormat.getCurrencyInstance();
		double balance = AccountOpenHelper.currentAccount.getBalance();
		String balanceString = currencyFormatter.format(balance);
		setContentView(R.layout.activity_account_home);
		TextView loginMessageTextView = (TextView) findViewById(R.id.login_message);
		String loginMessage = AccountOpenHelper.currentAccount.getName();
		loginMessageTextView.setText(loginMessage);
		TextView balanceMessageTextView = (TextView) findViewById(R.id.current_balance);
		String balanceMessage = getString(R.string.balance_message) + " "+ balanceString;
		balanceMessageTextView.setText(balanceMessage);
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


