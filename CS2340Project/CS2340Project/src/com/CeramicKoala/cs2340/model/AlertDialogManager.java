package com.CeramicKoala.cs2340.model;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class AlertDialogManager {
	
	public enum AlertType{
		USERNAME_ALREADY_EXISTS("User Account Error",
				"That username already exists", false),
		ACCOUNT_ALREADY_EXISTS("Account Error", 
				"That account already exists", false),
		ACCOUNT_DOES_NOT_EXIST("Account Error", 
				"Account does not exist",false),
		INCORRECT_PASSWORD("Login Error", 
				"Your password is incorrect", true),
		FIELD_IS_EMPTY("Empty Field", 
				"You left one of the required fields empty", false),
		NO_START_DATE("Date Error", 
				"You did not select a start date", false),
		NO_END_DATE("Date Error", 
				"You did not select an end date", false),
		IMPROPER_DATE_RANGE("Date Error", 
				"Your start date must come before your end date "
				+ "and neither date can be in the future", false),
		EMPTY_TRANSACTION("Transaction Error", 
				"Your transaction must have an amount greater than zero", false),
		OVERDRAWN_BALANCE("Account Error", 
				"You cannot withdraw more than your account balance", false);
		
		String title;
		String message;
		boolean quit;
		
		AlertType(String title, String message, boolean quit) {
			this.title = title;
			this.message = message;
			this.quit = quit;
		}
	}
	Activity activity;
	
	public AlertDialogManager(Activity activity) {
		this.activity = activity;
	}

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
