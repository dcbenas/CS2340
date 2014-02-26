package com.CeramicKoala.cs2340.activities;

import com.CeramicKoala.cs2340.R;
import com.CeramicKoala.cs2340.model.DatabaseException;
import com.CeramicKoala.cs2340.model.User;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * this class handles login processing and communicating with the SQLite db
 * via the LoginOpenHelper class. Currently, this activity only responds to
 * the login button. The button adds the user to the db if a unique username is
 * entered. If the username already exists, the password is checked. If a correct
 * password is entered, then login is successful. If incorrect, login is 
 * unsuccessful.
 * @author Benjamin Newcomer
 * @version 1.0
 *
 */
public class LogInActivity extends AccountManagementActivity {
	//TODO prevent login activity from starting if username already exists
	//TODO set content view to existing user xml if user has accounts and move setContentView down to bottom
	
	private AlertDialog wrongPassword;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		wrongPassword = setUpAlertDialog("Error", getString(R.string.log_in_error_incorrect_password));
		
		//setup
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log_in);
		alertDialog = setUpAlertDialog("Error", getString(R.string.log_in_error_no_account));
		
		User user = new User(null, null, null);
		
		try {
			//get user from database
			user = loginHelper.getElementByName(username);
			
			//set textView text with successful login message or display alert dialog
			TextView loginMessageTextView = (TextView) findViewById(R.id.login_message);
			if (checkCred(user.getUsername(), user.getPassword())) {
				if (password.equals(user.getPassword())) {
					String loginMessage = getString(R.string.log_in_success) + "\n"
							+ "Full name: " + user.getFullName() + "\n"
							+ "Username: " + user.getUsername() + "\n"
							+ "Password: " + user.getPassword() + "\n"
							+ "User Id: " + user.getId();
					loginMessageTextView.setText(loginMessage);
				} else {
					wrongPassword.show();
				}
			}
		} catch (DatabaseException e) {
			Log.d("LogInActivity.get_user", e.getMessage());
			alertDialog.show();
		}		
	}

}
