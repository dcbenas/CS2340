package com.CeramicKoala.cs2340.activities;

import com.CeramicKoala.cs2340.model.User;
import com.example.cs2340project.BuildConfig;
import com.example.cs2340project.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * this activity handles new account registration. Takes in username and password
 * from MainActivity. Uses AccountOpenHelper.addUser to add a new user. 
 * NOTE user Id cannot be displayed. User has a unique Id in the database, 
 * but AccountOpenHelper.addUser does not retrieve it
 * @author Benjamin Newcomer
 */
public class RegisterActivity extends AccountManagementActivity {
	
	private String username;
	private String password;
	private String fullName;
	EditText register_username;
	EditText register_password;
	EditText register_full_name;
	
	//TODO add confirm password field and check password
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		//setup
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		alertDialog = setUpAlertDialog("Error", getString(R.string.register_error_username_already_exists));
		
		//fill register_username and register_password fields with info from main activity
		Intent intent = getIntent();
		final String USERNAME = getText(R.string.username_constant).toString();
		final String PASSWORD = getText(R.string.password_constant).toString();
		
		//set register_username
		register_username = (EditText) findViewById(R.id.register_username);
		username = intent.getStringExtra(USERNAME);
		register_username.setText(username);
		
		//set register_password
		register_password = (EditText) findViewById(R.id.register_password);
		password = intent.getStringExtra(PASSWORD);
		register_password.setText(password);
		
		//set full_name field
		register_full_name = (EditText) findViewById(R.id.register_full_name);
		
	}
	
	/**
	 * starts LogInActivity. Responds to click on Submit button
	 * @param view
	 */
	public void submit(View view) {
		
		//get updated info from view
		username = register_username.getText().toString();
		password = register_password.getText().toString();
		fullName = register_full_name.getText().toString();
		
		//add user to database
		User user = dbModel.addUser(new User(fullName, username, password));
		
		//DEBUG
		if (BuildConfig.DEBUG) {
			//log success of addUser()
			Log.d("LogInActivity.add_user", String.valueOf(user.getId()));
		}
		
		//set textView text with successful registration message or show alert dialog
		TextView registerMessageTextView = (TextView) findViewById(R.id.register_message);
		String registerMessage;
		if (password.equals(user.getPassword())) {
			registerMessage = getString(R.string.register_success)
					+ "\n" + "Full Name: " + user.getFullName()
					+ "\n" + "Username: " + user.getUsername() 
					+ "\n" + "Password: " + user.getPassword();
			registerMessageTextView.setText(registerMessage);
		} else {
			alertDialog.show();
		}
				
		startActivity(getIntent(LogInActivity.class));
	}
	


}
