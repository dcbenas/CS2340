package com.example.myfirstapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

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
			
		}
	}
	
	public void returnToLogin(View view) {
		startActivity(getIntent(AccountHomeActivity.class));
	}
	
}
