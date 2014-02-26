package com.CeramicKoala.cs2340.activities;

import com.CeramicKoala.cs2340.BuildConfig;
import com.CeramicKoala.cs2340.R;
import com.CeramicKoala.cs2340.model.DatabaseException;
import com.CeramicKoala.cs2340.model.DatabaseOpenHelper;
import com.CeramicKoala.cs2340.model.LoginOpenHelper;
import com.CeramicKoala.cs2340.model.User;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


/**
 *  This is the main activity. This activity is called on app startup.
 *  Username/password fields are presented along with login and register buttons.
 *  Currently only the login button is supported. The button starts the activity
 *  LogInActivity and passes in username and password.
 * @author Benjamin Newcomer
 * @version 1.0
 */
public class MainActivity extends Activity {
	
	//TODO implement a method for sending Users through intents rather than individual fields

	 //constants for use in creating loggedIn intent
	 public static final String USERNAME = "com.example.cs2340project.USERNAME";
	 public static final String PASSWORD = "com.example.cs2340project.PASSWORD";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		//clear the password field upon return to MainActivity
		EditText passwordEditText = (EditText) findViewById(R.id.field_password);
		passwordEditText.setText(null);
		
		if (BuildConfig.DEBUG) {
			//set db_info textview to display size of table
			TextView dbInfoTextView = (TextView) findViewById(R.id.db_info);
			dbInfoTextView.setText(getTableInfo());
		}
	}

	 /**
	  * starts LogInActivity. Responds to click on Log In button
	  * @param view
	  */
	 public void logIn (View view) {
	     if (notEmpty()) {
			startActivity(getIntent(LogInActivity.class));
		}
	 }
	 
	 private boolean notEmpty() {
		 final String USERNAME = getString(R.string.username_constant);
		final String PASSWORD = getString(R.string.password_constant);
		
		//get username
		EditText field_username = (EditText) findViewById(R.id.field_username);
		String username = field_username.getText().toString();
		if (username.isEmpty())
			return false;
		//get password
		EditText field_password = (EditText) findViewById(R.id.field_password);
		String password = field_password.getText().toString();
		if (password.isEmpty())
			return false;
		return true;
	 }
	 
	 /**
	  * starts RegisterActivity. Responds to click on Register button
	  * @param view
	  */
	 public void register(View view) {
	     startActivity(getIntent(RegisterActivity.class));
	 }
 
	/**
	 * helper method for logIn and register. places username
	 * and password into an intent
	 * @return Intent with username and password
	 */
	private Intent getIntent(Class<?> activityClass) {
		//TODO implement a method for sending Users through intents rather than individual fields
		
		final String USERNAME = getString(R.string.username_constant);
		final String PASSWORD = getString(R.string.password_constant);
		Intent intent = new Intent(this, activityClass);
		
		//get username
		EditText field_username = (EditText) findViewById(R.id.field_username);
		String username = field_username.getText().toString();
		intent.putExtra(USERNAME, username);
		
		//get password
		EditText field_password = (EditText) findViewById(R.id.field_password);
		String password = field_password.getText().toString();
		intent.putExtra(PASSWORD, password);
		
		return intent;
	}
	
	/**
	 * resets database table login. for dev use only
	 * will print to log false if unsuccessful or
	 * if table is already empty. Does remove admin
	 */
	public void resetDatabase(View view) {
		DatabaseOpenHelper<User> dbModel = new LoginOpenHelper(this);
		boolean success = dbModel.resetTable();
		//replace the admin that was just deleted
		createAdmin();
		Log.d("reset db", String.valueOf(success));
		onResume();
	}
	
	/**
	 * helper method that is called in MainActivity.onCreate()
	 * creates an admin user (if one does not already exist)
	 */
	private void createAdmin() {
		DatabaseOpenHelper<User> dbModel = new LoginOpenHelper(this);
		try {
			dbModel.addElement( new User(
							getString(R.string.default_full_name),
							getString(R.string.default_user), 
							getString(R.string.default_password)));
			if (BuildConfig.DEBUG) {
					Log.d("MainActivity.create_admin", "admin created");
			}
		} catch (DatabaseException e) {
			if (BuildConfig.DEBUG) {
				Log.d("MainActivity.create_admin", e.getMessage());
			}
		}
	}
	
	/**
	 * @see DatabaseOpenHelper#getTableInfo()
	 */
	private String getTableInfo() {
		DatabaseOpenHelper<User> dbModel = new LoginOpenHelper(this);
		return dbModel.getTableInfo();
	}
 
	protected AlertDialog setUpAlertDialog(String title, String message) {
		
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
	    
			}
		});
		return alertDialog;
	}
}
