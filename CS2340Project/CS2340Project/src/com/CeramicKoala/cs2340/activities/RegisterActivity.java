package com.CeramicKoala.cs2340.activities;

import com.CeramicKoala.cs2340.BuildConfig;
import com.CeramicKoala.cs2340.R;
import com.CeramicKoala.cs2340.model.DatabaseException;
import com.CeramicKoala.cs2340.model.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * this activity handles new account registration. Takes in username and password
 * from MainActivity. Uses LoginOpenHelper.addUser to add a new user. 
 * NOTE user Id cannot be displayed. User has a unique Id in the database, 
 * but LoginOpenHelper.addUser does not retrieve it
 * @author Benjamin Newcomer
 */
public class RegisterActivity extends AccountManagementActivity {
	
	private String username;
	private String password;
	private String fullName;
	EditText register_username;
	EditText register_password;
	EditText register_full_name;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		//setup
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		alertDialog = setUpAlertDialog("Error", getString(R.string.register_error_username_already_exists), false);
		
		//fill register_username and register_password fields with info from main activity
		Intent intent = getIntent();
		final String USERNAME = getString(R.string.username_constant);
		final String PASSWORD = getString(R.string.password_constant);
		final String FROM_MAIN = getString(R.string.from_main_constant);
		
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
		
		if (checkCred(username, password)) {
			//check if username and password is valid THEN add user to database
			try {
				User user = loginHelper.addElement(new User(fullName, username,
						password));
				if (password.equals(user.getPassword())) {
					startActivity(getIntent(LogInActivity.class));
				}
			} catch (DatabaseException e) {
				if (BuildConfig.DEBUG) {
					alertDialog.setMessage(e.getMessage());
				}
				alertDialog.show();
			}
		}
				
	}
	
	@Override
	public Intent getIntent(Class<?> activityClass) {
		Intent intent = super.getIntent(activityClass);
		intent.putExtra(FROM_MAIN, true);
		return intent;
	}

}
