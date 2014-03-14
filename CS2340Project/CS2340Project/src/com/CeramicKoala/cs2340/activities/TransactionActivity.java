package com.CeramicKoala.cs2340.activities;

import java.util.Date;

import com.CeramicKoala.cs2340.R;
import com.CeramicKoala.cs2340.model.Account;
import com.CeramicKoala.cs2340.model.AccountOpenHelper;
import com.CeramicKoala.cs2340.model.AlertDialogManager;
import com.CeramicKoala.cs2340.model.SessionManager;
import com.CeramicKoala.cs2340.model.Transaction;
import com.CeramicKoala.cs2340.model.TransactionOpenHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

/**
 * Transaction Activity supports deposits and withdrawals. Updates the relevant
 * SQLlite databases on update
 * 
 * @author Matthew Berman
 */
public class TransactionActivity extends Activity {
	
	//TODO Matthew - change all transaction methods to accept a date (specific to the day)
	
	private TransactionOpenHelper transaction;
	private AccountOpenHelper accountHelper;
	private AlertDialogManager alertManager;
	private SessionManager sessionManager;
// DEPRECATED
//	private AlertDialog isEmpty;
//	private AlertDialog underZero;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transaction);
		
		//instantiate helper objects
		accountHelper = new AccountOpenHelper(this);
		transaction = new TransactionOpenHelper(this);
		alertManager = new AlertDialogManager(this);
		sessionManager = new SessionManager(this);
		
		
// DEPRECATED
//		isEmpty = setUpAlertDialog("Error","You need numbers for a transaction!", false);
//		underZero = setUpAlertDialog("Error","You cannot withdraw more than your current balance!", false);

		
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
//		Intent intent = new Intent(this, activityClass);
//		return intent;
//	}
	
	/**
	 * Makes a deposit if all fields are filled out correctly.
	 * Otherwise, shows relevant alert dialog
	 * 
	 * @param view the current view
	 */
	public void makeDeposit(View view) {
		
		try {
			//TODO update code to use AccountHelper#updateElement() correctly
			//TODO update code to use Account#incrementBalance instead of Account#setBalance()
			EditText transactionAmount = (EditText) findViewById(R.id.transaction_amount);
			String balChange = transactionAmount.getText().toString();
			
			if (balChange.isEmpty()) {
				
				alertManager.generateAlertDialog(
						AlertDialogManager.AlertType.EMPTY_TRANSACTION)
						.show();
			} else {
				
				double balanceChange = Double.valueOf(balChange);
				Account updatedAccount = sessionManager.getAccount();
				updatedAccount.setBalance(balanceChange);
				accountHelper.updateElement(updatedAccount);

				int id = sessionManager.getAccountId();
				Date date = new Date();
				Transaction deposit = new Transaction(id, 0, balanceChange, date);
				transaction.addElement(deposit);
				Intent intent = new Intent(this, AccountHomeActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	/**
	 * Makes a withdrawal if all fields are filled out correctly.
	 * Otherwise, shows relevant alert dialog
	 * 
	 * @param view the current view
	 */
	public void makeWithdrawal(View view) {
		
		try {
			
			EditText transactionAmount = (EditText) findViewById(R.id.transaction_amount);
			String balChange = transactionAmount.getText().toString();
			
			if (balChange.isEmpty()) {
				
				alertManager.generateAlertDialog(
						AlertDialogManager.AlertType.EMPTY_TRANSACTION)
						.show();
			} else {
				
				double balanceChange = Double.valueOf(balChange);
				double currBal = sessionManager.getAccount().getBalance();
				double newBal = currBal - balanceChange;//hooAH;
				
				if (newBal >= 0) {
					
					int id = sessionManager.getAccountId();
					Date date = new Date();
					Transaction withdrawal = new Transaction(id, 1, balanceChange, date);
					transaction.addElement(withdrawal);
					
					Account updatedAccount = sessionManager.getAccount();
					updatedAccount.setBalance(newBal);
					accountHelper.updateElement(updatedAccount);
					
					Intent intent = new Intent(this, AccountHomeActivity.class);
					//This clears its previous instance of the activity and any other activities on top of it.
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					
					startActivity(intent);
					//TODO I don't think this code will ever run
					finish();
				} else {
					
					alertManager.generateAlertDialog(
							AlertDialogManager.AlertType.OVERDRAWN_BALANCE)
							.show();
				}
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns the client to the account home activity page
	 * 
	 * @param view the current view
	 */
	public void returnToLogin(View view) {
		
		Intent intent = new Intent(this, AccountHomeActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		//TODO don't think we need this
		finish();
	}
}
