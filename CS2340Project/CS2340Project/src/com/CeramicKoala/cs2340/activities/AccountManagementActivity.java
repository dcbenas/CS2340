package com.CeramicKoala.cs2340.activities;

import com.CeramicKoala.cs2340.R;
import com.CeramicKoala.cs2340.model.DatabaseOpenHelper;
import com.CeramicKoala.cs2340.model.LoginOpenHelper;
import com.CeramicKoala.cs2340.model.User;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;

public abstract class AccountManagementActivity extends Activity {
	
	protected static DatabaseOpenHelper<User> loginHelper;
	protected AlertDialog alertDialog;
	protected String username;
	protected String password; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setupActionBar();
		loginHelper = new LoginOpenHelper(this);
		
		//retrieve username and password info from intent sent by MainActivity
		Intent intent = getIntent();
		String USERNAME = getString(R.string.username_constant);
		String PASSWORD = getString(R.string.password_constant);
		username = intent.getStringExtra(USERNAME);
		password = intent.getStringExtra(PASSWORD);
		
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.account_management, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * sets up alert dialog for 'error account info mismatch' dialog
	 */
	protected AlertDialog setUpAlertDialog(String title, String message) {
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Go Back", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});

		return alertDialog;
	}
	
	/**
	 * helper method that places username and password
	 * for logging in into an intent
	 * @return Intent with username and password
	 */
	protected Intent getIntent(Class<?> activityClass) {
		
		final String USERNAME = getString(R.string.username_constant);
		final String PASSWORD = getString(R.string.password_constant);
		Intent intent = new Intent(this, activityClass);
		
		//set username
		EditText register_username = (EditText) findViewById(R.id.register_username);
		String username = register_username.getText().toString();
		intent.putExtra(USERNAME, username);
		
		//set password
		EditText register_password = (EditText) findViewById(R.id.register_password);
		String password = register_password.getText().toString();
		intent.putExtra(PASSWORD, password);
		
		return intent;
	}
}
