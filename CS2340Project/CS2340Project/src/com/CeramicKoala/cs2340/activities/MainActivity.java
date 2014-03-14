package com.CeramicKoala.cs2340.activities;

import com.CeramicKoala.cs2340.BuildConfig;
import com.CeramicKoala.cs2340.R;
import com.CeramicKoala.cs2340.model.AccountOpenHelper;
import com.CeramicKoala.cs2340.model.AlertDialogManager;
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

	
	 //constants for use in creating loggedIn intent
	 public static final String USERNAME = "com.example.cs2340project.USERNAME";
	 public static final String PASSWORD = "com.example.cs2340project.PASSWORD";
	 //DEPRECATED private AlertDialog isEmpty;
	 private LoginOpenHelper loginHelper;
	 private AlertDialogManager alertManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		loginHelper = new LoginOpenHelper(this);
		alertManager = new AlertDialogManager(this);
		//DEPRECATED isEmpty = setUpAlertDialog("Error",getString(R.string.log_in_error_field_empty));
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
		} else {
			
			alertManager.generateAlertDialog(
					AlertDialogManager.AlertType.FIELD_IS_EMPTY)
					.show();
		}
	 }
	 
	 private boolean notEmpty() {
		
		//TODO delete these vars if they are really never used
		final String USERNAME = getString(R.string.username_constant);
		final String PASSWORD = getString(R.string.password_constant);
		
		//get username
		EditText field_username = (EditText) findViewById(R.id.field_username);
		String username = field_username.getText().toString();
		if (username.isEmpty()) {
			return false;
		}
		
		//get password
		EditText field_password = (EditText) findViewById(R.id.field_password);
		String password = field_password.getText().toString();
		if (password.isEmpty()) {
			return false;
		}
		
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
		
		//making sure there are no 2 instances of LogInActivity and Logging out of current account
		//TODO replace reference to log out with SessionManager#logOut()
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		loginHelper.logOut();
		return intent;
	}
	
	/**
	 * resets database table login. for dev use only
	 * will print to log false if unsuccessful or
	 * if table is already empty. Does remove admin
	 */
	public void resetDatabase(View view) {
		
		LoginOpenHelper loginHelper = new LoginOpenHelper(this);
		AccountOpenHelper accountHelper = new AccountOpenHelper(this);
		
		boolean success1 = accountHelper.resetTable();
		boolean success2 = loginHelper.resetTable();
		
		//replace the admin that was just deleted
		createAdmin();
		Log.d("reset db", String.valueOf(success1 && success2));
		
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
 
// DEPRECATED
//	protected AlertDialog setUpAlertDialog(String title, String message) {
//		
//		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
//		alertDialog.setTitle(title);
//		alertDialog.setMessage(message);
//		alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int which) {
//	    
//			}
//		});
//		return alertDialog;
//	}
}
