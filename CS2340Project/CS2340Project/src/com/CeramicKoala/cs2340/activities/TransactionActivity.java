package com.CeramicKoala.cs2340.activities;

import com.CeramicKoala.cs2340.R;
import com.CeramicKoala.cs2340.model.DatabaseOpenHelper;
import com.CeramicKoala.cs2340.model.LoginOpenHelper;
import com.CeramicKoala.cs2340.model.User;
import com.CeramicKoala.cs2340.model.Account;
import com.CeramicKoala.cs2340.model.AccountOpenHelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class TransactionActivity extends AccountHomeActivity {
private AccountOpenHelper myHelper;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myHelper = new AccountOpenHelper(this);
		setContentView(R.layout.activity_transaction);
		
		
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.account_registration, menu);
		return true;
	}
	
	protected Intent getIntent(Class<?> activityClass) {
		Intent intent = new Intent(this, activityClass);
		intent.putExtra(FROM_MAIN, false);
		return intent;
	}
	
	public void makeTransaction(View view) {
		try {
			EditText transactionAmount = (EditText) findViewById(R.id.transaction_amount);
			String balChange = transactionAmount.getText().toString();
			double balanceChange = new Double(balChange);
			double currBal = AccountOpenHelper.currentAccount.getBalance();
			double newBal = currBal + balanceChange;//hooAH;
			AccountOpenHelper.currentAccount.setBalance(newBal);
			Account tempAcc = AccountOpenHelper.currentAccount;
			myHelper.updateElement(tempAcc);
			Intent intent = getIntent(AccountHomeActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
			startActivity(intent);
			finish();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void returnToLogin(View view) {
		startActivity(getIntent(AccountHomeActivity.class));
	}
	
}
