package com.example.cs2340project;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

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
	//password info not currently being used
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
	
	/**
	 * starts LogInActivity. Responds to click on Log In button
	 * @param view
	 */
	public void logIn (View view) {
		startActivity(getIntent(LogInActivity.class));
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
	 * if table is already empty
	 */
	public void resetDatabase(View view) {
		AccountOpenHelper accountHelper = new AccountOpenHelper(this);
		boolean success = accountHelper.resetDatabase();
		Log.d("reset db", String.valueOf(success));
	}
	
	
	
	
	
	
	
	
	
	
	
	

}
