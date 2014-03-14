package com.CeramicKoala.cs2340.activities;

import com.CeramicKoala.cs2340.R;
import com.CeramicKoala.cs2340.model.AlertDialogManager;
import com.CeramicKoala.cs2340.model.LoginOpenHelper;

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
	
	protected static LoginOpenHelper loginHelper;
	//DEPRECATED protected AlertDialog alertDialog;
	protected AlertDialog loginWrong;
	protected AlertDialog passWrong;
	protected AlertDialog emptyField;
	protected String username;
	protected String password; 
	protected Intent intent;
	protected AlertDialogManager alertManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setupActionBar();
		
		loginHelper = new LoginOpenHelper(this);
		alertManager = new AlertDialogManager(this);
		
		//retrieve username and password info from intent sent by MainActivity
		intent = getIntent();
		
		if (loginHelper.getCurrentUser() == null) {
			
			String USERNAME = getString(R.string.username_constant);
			String PASSWORD = getString(R.string.password_constant);
			
			username = intent.getStringExtra(USERNAME);
			password = intent.getStringExtra(PASSWORD);
			
			loginWrong = setUpAlertDialog("Error", getString(R.string.log_in_error_user_at_least_six), false);
			passWrong = setUpAlertDialog("Error", getString(R.string.log_in_error_pass_at_least_six), false);
			emptyField = setUpAlertDialog("Error", getString(R.string.empty_field_error), false);
		}
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
			System.out.println(NavUtils.getParentActivityName(this));
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * sets up alert dialog for 'error account info mismatch' dialog
	 */
	protected AlertDialog setUpAlertDialog(String title, String message, boolean quit) {
		
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		
		if (quit) {
			alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
		} else {
			alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			});
		}
		
		return alertDialog;
	}
	
	/**
	 * helper method that places username and password
	 * for logging in into an intent
	 * 
	 * !! might be obsolete, since all its subclasses simply override it.
	 * @return Intent with username and password
	 */
	//TODO discuss getIntent in AMA
	protected Intent getIntent(Class<?> activityClass) {
		
		Intent intent = new Intent(this, activityClass);
		
		if (loginHelper.getCurrentUser() == null) {
			
			final String USERNAME = getString(R.string.username_constant);
			final String PASSWORD = getString(R.string.password_constant);
			
			//set username
			EditText register_username = (EditText) findViewById(R.id.register_username);
			String username = register_username.getText().toString();
			intent.putExtra(USERNAME, username);
			
			//set password
			EditText register_password = (EditText) findViewById(R.id.register_password);
			String password = register_password.getText().toString();
			intent.putExtra(PASSWORD, password);
		}
		return intent;
	}
	
	protected boolean checkCred(String user, String pass) {
		
		if(!user.matches("[a-z|A-Z|0-9]{6}[a-z|A-Z|0-9]*")) {
			loginWrong .show();
			return false;
		}

		if(!pass.matches("[a-z|A-Z|0-9]{6}[a-z|A-Z|0-9]*")) {
			passWrong.show();
			return false;
		}

		return true;
	}
}
