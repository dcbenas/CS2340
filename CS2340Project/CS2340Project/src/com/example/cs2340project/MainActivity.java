package com.example.cs2340project;

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
 protected AlertDialog loginWrong;
 private AlertDialog passWrong;
 
 @Override
 protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_main);
  createAdmin();
  loginWrong = setUpAlertDialog("Error", getString(R.string.log_in_error_user_at_least_six));
  passWrong = setUpAlertDialog("Error", getString(R.string.log_in_error_pass_at_least_six));
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
   if (checkCred())
     startActivity(getIntent(LogInActivity.class));
 }
 
 /**
  * starts RegisterActivity. Responds to click on Register button
  * @param view
  */
 public void register(View view) {
   if (checkCred())
     startActivity(getIntent(RegisterActivity.class));
 }
 
 /*
  * checks input requirements
  */
 
 private boolean checkCred() {
  EditText field_username = (EditText) findViewById(R.id.field_username);
  String username = field_username.getText().toString();
  
  if(!username.matches("[a-z|A-Z|0-9]{6}[a-z|A-Z|0-9]*")) {
   loginWrong.show();
   return false;
  }
  
  EditText field_password = (EditText) findViewById(R.id.field_password);
  String password = field_password.getText().toString();
  if(!password.matches("[a-z|A-Z|0-9]{6}[a-z|A-Z|0-9]*")) {
   passWrong.show();
   return false;
  }
  
  return true;
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
  * if table is already empty. Does remove admin
  */
 public void resetDatabase(View view) {
  AccountOpenHelper accountHelper = new AccountOpenHelper(this);
  boolean success = accountHelper.resetDatabase();
  Log.d("reset db", String.valueOf(success));
 }
 
 /**
  * helper method that is called in MainActivity.onCreate()
  * creates an admin user (if one does not already exist)
  */
 private void createAdmin() {
  AccountOpenHelper accountHelper = new AccountOpenHelper(this);
  User admin = accountHelper.addUser(
    new User(getString(R.string.default_user), getString(R.string.default_password)));
  
  //DEBUG
  if (BuildConfig.DEBUG) {
   if (admin.getId() != -1) {
    Log.d("MainActivity.create_admin", "admin created");
   } else {
    Log.d("MainActivity.create_admin", "admin already exists");
   }
  }

 }
 
 /**
  * @see AccountOpenHelper#getTableInfo()
  */
 private String getTableInfo() {
  AccountOpenHelper accountHelper = new AccountOpenHelper(this);
  return accountHelper.getTableInfo();
 }
 
 /*
  * helper method that sets up error message
  */
 
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
