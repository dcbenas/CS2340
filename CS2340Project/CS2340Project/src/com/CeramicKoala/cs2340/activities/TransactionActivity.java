package com.CeramicKoala.cs2340.activities;

import java.util.Date;

import com.CeramicKoala.cs2340.R;
import com.CeramicKoala.cs2340.model.AccountOpenHelper;
import com.CeramicKoala.cs2340.model.Transaction;
import com.CeramicKoala.cs2340.model.TransactionOpenHelper;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class TransactionActivity extends AccountHomeActivity {
private TransactionOpenHelper transaction;
private AccountOpenHelper myHelper;
private AlertDialog isEmpty;
private AlertDialog underZero;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myHelper = new AccountOpenHelper(this);
		transaction = new TransactionOpenHelper(this);
		setContentView(R.layout.activity_transaction);
		isEmpty = setUpAlertDialog("Error","You need numbers for a transaction!", false);
		underZero = setUpAlertDialog("Error","You cannot withdraw more than your current balance!", false);
		
		
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
	
	public void makeDeposit(View view) {
		try {
			EditText transactionAmount = (EditText) findViewById(R.id.transaction_amount);
			String balChange = transactionAmount.getText().toString();
			if(balChange.isEmpty()) {
				isEmpty.show();
			} else {
				double balanceChange = new Double(balChange);
				double currBal = AccountOpenHelper.currentAccount.getBalance();
				double newBal = currBal + balanceChange;
				AccountOpenHelper.currentAccount.setBalance(newBal);
				myHelper.updateBalance();
				int id = AccountOpenHelper.currentAccount.getAccountId();
				Date date = new Date();
				Transaction deposit = new Transaction(id, 0, balanceChange, date);
				Transaction z = transaction.addElement(deposit);
				Intent intent = getIntent(AccountHomeActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void makeWithdrawal(View view) {
		try {
			EditText transactionAmount = (EditText) findViewById(R.id.transaction_amount);
			String balChange = transactionAmount.getText().toString();
			if(balChange.isEmpty()) {
				isEmpty.show();
			} else {
				double balanceChange = new Double(balChange);
				double currBal = AccountOpenHelper.currentAccount.getBalance();
				double newBal = currBal - balanceChange;//hooAH;
				if (newBal >= 0) {
					int id = AccountOpenHelper.currentAccount.getAccountId();
					Date date = new Date();
					Transaction withdrawal = new Transaction(id, 1, balanceChange, date);
					Transaction z = transaction.addElement(withdrawal);
					AccountOpenHelper.currentAccount.setBalance(newBal);
					myHelper.updateBalance();
					Intent intent = getIntent(AccountHomeActivity.class);
					//This clears its previous instance of the activity and any other activities on top of it.
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					finish();
				} else {
					underZero.show();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void returnToLogin(View view) {
		Intent intent = getIntent(AccountHomeActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
	}
}
