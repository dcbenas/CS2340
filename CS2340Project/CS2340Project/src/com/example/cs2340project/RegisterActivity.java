package com.example.cs2340project;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * this activity handles new account registration. Takes in username and password
 * from MainActivity. Uses AccountOpenHelper.addUser to add a new user. 
 * NOTE user Id cannot be displayed. User has a unique Id in the database, 
 * but AccountOpenHelper.addUser does not retrieve it
 * @author Benjamin Newcomer
 */
public class RegisterActivity extends AccountManagementActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		//setup
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		alertDialog = setUpAlertDialog("Error", getString(R.string.register_error_username_already_exists));
				
		//add user to database
		User user = accountHelper.addUser(new User(username, password));
		//log success of addUser()
		Log.d("LogInActivity.add_user", String.valueOf(user.getId()));
		
		//set textView text with successful registration message or show alert dialog
		TextView registerMessageTextView = (TextView) findViewById(R.id.register_message);
		String registerMessage;
		if (password.equals(user.getPassword())) {
			registerMessage = getString(R.string.register_success) 
					+ "\n" + "Username: " + user.getUsername() 
					+ "\n" + "Password: " + user.getPassword();
			registerMessageTextView.setText(registerMessage);
		} else {
			alertDialog.show();
		}
	}

}
