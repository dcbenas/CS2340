package com.CeramicKoala.cs2340.model;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * AlertDialogManager keeps track and provides version control for all alert dialogs 
 * (pop up error messages) for the entire application. Activites should use AlertDialogManager to 
 * display alert dialogs when appropriate. Activites should not define alert dialogs.
 * If necessary, they can generate an alert dialog using this class and customize
 * the message.
 * @author Benjamin Newcomer
 */
public class AlertDialogManager {
	
	/**
	 * AlertType defines the type of alert and provides
	 * preset titles, messages, and quit booleans
	 * @author Benjamin Newcomer
	 */
	public enum AlertType{
		
		// user errors
		USERNAME_ALREADY_EXISTS("User Account Error",
				"That username already exists", false),
		INCORRECT_PASSWORD("Login Error", 
				"Your password is incorrect", false),
		INCORRECT_LOGIN("Login Error",
				"Your username/password combination is incorrect", false),
		// account errors
		ACCOUNT_ALREADY_EXISTS("Account Error", 
				"That account already exists", false),
		ACCOUNT_DOES_NOT_EXIST("Account Error", 
				"Account does not exist", true),
		// field errors
		FIELD_IS_EMPTY("Empty Field", 
				"You left one of the required fields empty", false),	
		// date errors
		NO_START_DATE("Date Error", 
				"You did not select a start date", false),
		NO_END_DATE("Date Error", 
				"You did not select an end date", false),
		IMPROPER_DATE_RANGE("Date Error", 
				"Your start date must come before your end date "
				+ "and neither date can be in the future", false),
		//transaction errors
		EMPTY_TRANSACTION("Transaction Error", 
				"Your transaction must have an amount greater than zero", false),
		CHOOSE_TYPE("Transaction Error", "Please select deposit or withdrawal", false),
		OVERDRAWN_BALANCE("Account Error", 
				"You cannot withdraw more than your account balance", false),
		//general errors
		ERROR_QUIT_FALSE("Unknown Error",
				"Unknown error occured", false),
		ERROR_QUIT_TRUE("Unknown Error",
				"Unknown Error occured", true);
		
		String title;
		String message;
		boolean quit;
		
		/**
		 * constructor
		 * @param title provided by enum
		 * @param message provided by enum
		 * @param quit provided by enum
		 */
		AlertType(String title, String message, boolean quit) {
			this.title = title;
			this.message = message;
			this.quit = quit;
		}
	}
	Activity activity;
	
	/**
	 * constructor
	 * @param activity that creates this
	 */
	public AlertDialogManager(Activity activity) {
		
		this.activity = activity;
	}

	/**
	 * generateAlertDialog creates a generic alert dialog depending on the
	 * alert type passed in
	 * @param type of alert
	 * @return AlertDialog
	 */
	public AlertDialog generateAlertDialog(AlertType type) {
		
		AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
		alertDialog.setTitle(type.title);
		alertDialog.setMessage(type.message);
		
		if (type.quit) {
			
			alertDialog.setButton(
					DialogInterface.BUTTON_NEUTRAL, "OK", 
					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							activity.finish();
						}
					
					});
		} else {
			
			alertDialog.setButton(
					DialogInterface.BUTTON_NEUTRAL, "OK", 
					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							//do nothing
						}
					
					});
		}
		
		return alertDialog;
	}
	
	

}
