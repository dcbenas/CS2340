package com.CeramicKoala.cs2340.activities;

import java.util.Date;

import com.CeramicKoala.cs2340.R;
import com.CeramicKoala.cs2340.model.Account;
import com.CeramicKoala.cs2340.model.AccountOpenHelper;
import com.CeramicKoala.cs2340.model.AlertDialogManager;
import com.CeramicKoala.cs2340.model.DatabaseException;
import com.CeramicKoala.cs2340.model.SessionManager;
import com.CeramicKoala.cs2340.model.Transaction;
import com.CeramicKoala.cs2340.model.Transaction.TransactionType;
import com.CeramicKoala.cs2340.model.TransactionOpenHelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

/**
 * Transaction Activity supports deposits and withdrawals. Updates the relevant
 * SQLlite databases on update
 * 
 * @author Matthew Berman
 */
public class TransactionActivity extends Activity implements OnItemSelectedListener {
	
	//TODO Matthew - change all transaction methods to accept a date (specific to the day)
	
	private TransactionOpenHelper transaction;
	private AccountOpenHelper accountHelper;
	private AlertDialogManager alertManager;
	private SessionManager sessionManager;
	
	//0 = UNCHECKED, 1 = DEPOSIT, 2 = WITHDRAWAL
	private Object[] depositTypes;
	private Object[] withdrawalTypes;
	
	//determines if a category is selected
	private int transType;
	private TransactionType transCategory;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transaction);
		
		//instantiate helper objects
		accountHelper = new AccountOpenHelper(this);
		transaction = new TransactionOpenHelper(this);
		alertManager = new AlertDialogManager(this);
		sessionManager = new SessionManager(this);
		transType = 0;
		updateAccountSpinner();
		//Size hardcoded, for now ?
		depositTypes = new Object[5];
		withdrawalTypes = new Object[5];
		depositTypes[0] = Transaction.TransactionType.DEPOSIT;
		depositTypes[1] = Transaction.TransactionType.SALARY;
		depositTypes[2] = Transaction.TransactionType.GIFT;
		depositTypes[3] = Transaction.TransactionType.PARENTS;
		depositTypes[4] = Transaction.TransactionType.SCHOLARSHIP;
		withdrawalTypes[0] = Transaction.TransactionType.WITHDRAWAL;
		withdrawalTypes[1] = Transaction.TransactionType.RENT;
		withdrawalTypes[2] = Transaction.TransactionType.CLOTHING;
		withdrawalTypes[3] = Transaction.TransactionType.FOOD;
		withdrawalTypes[4] = Transaction.TransactionType.ENTERTAINMENT;
		
		
		
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
	
	/**
	 * Makes a deposit if all fields are filled out correctly.
	 * Otherwise, shows relevant alert dialog
	 * 
	 * @param view the current view
	 */
	private void makeDeposit(View view) {
		
		try {
			//TODO update code to use AccountHelper#updateElement() correctly
			//TODO update code to use Account#incrementBalance instead of Account#setBalance()
			EditText transactionAmount = (EditText) findViewById(R.id.transaction_amount);
			String balChange = transactionAmount.getText().toString();
			
			if (balChange.isEmpty()|| (Double.valueOf(balChange) <= 0)) {
				
				alertManager.generateAlertDialog(
						AlertDialogManager.AlertType.EMPTY_TRANSACTION)
						.show();
			} else {
				
				double balanceChange = Double.valueOf(balChange);

				Account updatedAccount = sessionManager.getAccount();

				updatedAccount.setBalance(updatedAccount.getBalance() + balanceChange);
		
				int id = sessionManager.getAccountId();
				Date date = new Date();
				Transaction deposit = new Transaction(id, transCategory, balanceChange, date);
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
	private void makeWithdrawal(View view) {
		
		try {
			
			EditText transactionAmount = (EditText) findViewById(R.id.transaction_amount);
			String balChange = transactionAmount.getText().toString();
			
			if (balChange.isEmpty() || (Double.valueOf(balChange) <= 0)) {
				
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
					Transaction withdrawal = new Transaction(id, transCategory, balanceChange, date);
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
	
	public void updateAccountSpinner() {
		Spinner spinner;
		Object[] categoryNames;
		switch (transType) {
		    case 0:
		    	spinner = (Spinner)findViewById(R.id.category_spinner);
		    	spinner.setVisibility(View.GONE);
		    	break;
		    	
		    case 1:
		    	spinner = (Spinner)findViewById(R.id.category_spinner);
		    	categoryNames = new Object[depositTypes.length];
		    	for (int i = 0; i < depositTypes.length; i++) {
		    		TransactionType t = (TransactionType) depositTypes[i];
		    		categoryNames[i] = t.toString();
		    	}
		    	ArrayAdapter<Object> adapterD = new ArrayAdapter<Object>(
						this,
						android.R.layout.simple_spinner_item, 
						categoryNames);
		    	spinner.setAdapter(adapterD);
		    	spinner.setVisibility(View.VISIBLE);
				
				//set this to run callback when item is selected from spinner
				spinner.setOnItemSelectedListener(this);
				break;
			
		    case 2:
		    	spinner = (Spinner)findViewById(R.id.category_spinner);
		    	categoryNames = new Object[withdrawalTypes.length];
		    	for (int i = 0; i < withdrawalTypes.length; i++) {
		    		TransactionType t = (TransactionType) withdrawalTypes[i];
		    		categoryNames[i] = t.toString();
		    	}
		    	ArrayAdapter<Object> adapterW = new ArrayAdapter<Object>(
						this,
						android.R.layout.simple_spinner_item, 
						categoryNames);
		    	spinner.setAdapter(adapterW);
		    	spinner.setVisibility(View.VISIBLE);
				
				//set this to run callback when item is selected from spinner
				spinner.setOnItemSelectedListener(this);
				break;    	
		    	
		}
	}
	
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		switch (transType) {
		case 1:
			transCategory = (TransactionType) depositTypes[position];
			System.out.println(transCategory);
			break;
			
		case 2:
			transCategory = (TransactionType) withdrawalTypes[position];
			break;
			
		default:
			alertManager.generateAlertDialog(
					AlertDialogManager.AlertType.ERROR_QUIT_FALSE)
					.show();
			break;
		}
			
		//get chosen account and set to current account in session manager
		
	}
	
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}
	
	/**
	 * Sets the transaction type to correspond to which radio button
	 * was clicked
	 * 
	 * @param view the current view
	 */
	public void onRadioButtonClicked(View view) {
	    // Is the button now checked?
	    boolean checked = ((RadioButton) view).isChecked();
	    
	    // Check which radio button was clicked
	    switch(view.getId()) {
	        case R.id.radio_deposit:
	            if (checked)
	                transType = 1;
	            	updateAccountSpinner();
	            break;
	            
	        case R.id.radio_withdrawal:
	            if (checked) {
	            	transType = 2;
	            	updateAccountSpinner();
	            }
	         
	            break;
	    }
	}
	
	/**
	 * Makes a deposit or withdrawal given the chosen radio button
	 * 
	 * @param view the current view
	 */
	public void performTransaction(View view) {
		switch (transType) {
		
		case 0:
			alertManager.generateAlertDialog(
					AlertDialogManager.AlertType.CHOOSE_TYPE)
					.show();
			break;
			
		case 1:
			makeDeposit(view);
			break;
		
		case 2:
			makeWithdrawal(view);
			break;
		
		default:
			alertManager.generateAlertDialog(
					AlertDialogManager.AlertType.ERROR_QUIT_FALSE)
					.show();
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
