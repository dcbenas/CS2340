package com.example.myfirstapp;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class AccountHomeActivity extends AccountManagementActivity {
	private AccountOpenHelper myHelper;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myHelper = new AccountOpenHelper(this);
		setContentView(R.layout.activity_account_home);
		TextView loginMessageTextView = (TextView) findViewById(R.id.login_message);
		String loginMessage = getString(R.string.log_in_success) + " "+ AccountOpenHelper.currentAccount.getName();
		loginMessageTextView.setText(loginMessage);
		
		
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
	
	public void startTransaction(View view) {
		startActivity(getIntent(TransactionActivity.class));
	}
}


