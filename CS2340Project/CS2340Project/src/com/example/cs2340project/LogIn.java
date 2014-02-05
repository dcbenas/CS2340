package com.example.cs2340project;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.widget.TextView;

/**
 * this class handles login processing and communicating with the SQLite db
 * via the AccountOpenHelper class. Currently, this activity only responds to
 * the login button. The button adds the user to the db if a unique username is
 * entered. If the username already exists, the password is checked. If a correct
 * password is entered, then login is successful. If incorrect, login is 
 * unsuccessful.
 * @author Benjamin Newcomer
 * @version 1.0
 *
 */
public class LogIn extends Activity {
	
	private static AccountOpenHelper accountHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		accountHelper = new AccountOpenHelper(this);
		
		//retrieve username and password info intent sent by MainActivity
		Intent intent = getIntent();
		String username = intent.getStringExtra(MainActivity.USERNAME);
		String password = intent.getStringExtra(MainActivity.PASSWORD);
		
		//add user based on input. addUser checks to see if user exists. 
		//If user exists, addUser returns false, else adds new user to database
		boolean success = accountHelper.addUser(new User(username, password));
		//log success of addUser()
		Log.d("LogIn.add_user", String.valueOf(success));
		
		//query database for user account info, assuming account exists
		//If unsuccessful, getUser returns an empty user with Id -1
		User user = accountHelper.getUser(username);
		//Log success of getUser()
		if (user.getId() == -1) Log.d("LogIn.get_user", "user does not exist");
		
		setContentView(R.layout.activity_log_in);
		// Show the Up button in the action bar.
		setupActionBar();
		
		//set textView text with successful/unsuccessful login message
		TextView displayUsernameTextView = (TextView) findViewById(R.id.display_login_message);
		String loginMsg;
		if (password.equals(user.getPassword())) {
			loginMsg = getString(R.string.log_in_success) 
					+ "\n" + "Username: " + user.getUsername() 
					+ "\n" + "Password: " + user.getPassword() 
					+ "\n" + "User Id: " + user.getId();
		} else {
			loginMsg = getString(R.string.log_in_error_incorrect_password);
		}
		displayUsernameTextView.setText(loginMsg);

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
		getMenuInflater().inflate(R.menu.logged_in, menu);
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
	 * resets database. for dev use only
	 */
	public void resetDatabase(View view) {
		Log.d("reset db", String.valueOf(accountHelper.resetDatabase() > 0));
	}

}
