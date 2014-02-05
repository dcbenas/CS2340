package com.example.cs2340project;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

/**
 *  This is the main activity. This activity is called on app startup.
 *  Username/password fields are presented along with login and register buttons.
 *  Currently only the login button is supported. The button starts the activity
 *  LogIn and passes in username and password.
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
	 * starts Logged In activity. Responds to click on Log In button
	 * @param view
	 */
	public void logIn (View view) {
		Intent intent = new Intent(this, LogIn.class);
		
		//get username
		EditText field_username = (EditText) findViewById(R.id.field_username);
		String username = field_username.getText().toString();
		intent.putExtra(USERNAME, username);
		
		//get password
		EditText field_password = (EditText) findViewById(R.id.field_password);
		String password = field_password.getText().toString();
		intent.putExtra(PASSWORD, password);
		
		startActivity(intent);
	}

}
