package com.example.cs2340project;

import android.os.Bundle;
import android.util.Log;
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
public class LogInActivity extends AccountManagementActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		//setup
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log_in);
		alertDialog = setUpAlertDialog("Error", getString(R.string.log_in_error_no_account));
		
		//get user from database
		User user = accountHelper.getUser(username);
		//Log success of getUser()
		if (user.getId() == -1) Log.d("LogInActivity.get_user", "user does not exist");

		//set textView text with successful login message or display alert dialog
		TextView loginMessageTextView = (TextView) findViewById(R.id.login_message);
		if (password.equals(user.getPassword())) {
			String loginMessage = getString(R.string.log_in_success) 
					+ "\n" + "Username: " + user.getUsername() 
					+ "\n" + "Password: " + user.getPassword() 
					+ "\n" + "User Id: " + user.getId();
			loginMessageTextView.setText(loginMessage);
		} else {
			alertDialog.show();
		}
	}

}
