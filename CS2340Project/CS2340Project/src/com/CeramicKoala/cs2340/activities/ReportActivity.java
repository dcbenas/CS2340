package com.CeramicKoala.cs2340.activities;

import com.CeramicKoala.cs2340.BuildConfig;
import com.CeramicKoala.cs2340.R;
import com.CeramicKoala.cs2340.model.AccountOpenHelper;
import com.CeramicKoala.cs2340.model.DatabaseException;
import com.CeramicKoala.cs2340.model.DatabaseOpenHelper;
import com.CeramicKoala.cs2340.model.LoginOpenHelper;
import com.CeramicKoala.cs2340.model.User;

import java.sql.Date;
import java.text.NumberFormat;
import java.util.Calendar;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;
import android.widget.DatePicker;
import android.widget.TextView;
import android.support.v4.app.FragmentActivity;
import android.app.Dialog;
import android.app.DatePickerDialog;
import android.support.v4.app.DialogFragment;



public class ReportActivity extends FragmentActivity {
	DatePickerFragment startDate = new DatePickerFragment();
	DatePickerFragment endDate = new DatePickerFragment();
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//intent = getIntent();
		
		setContentView(R.layout.activity_report);
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.account_registration, menu);
		return true;
	}
	
	protected Intent getIntent(Class<?> activityClass) {
		Intent intent = new Intent(this, activityClass);
		return intent;
	}
	
	public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);
			
			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}
		
		public void onDateSet(DatePicker view, int year, int month, int day) {
			Date d = new Date(year, month, day);
			}
	}
	
	public void showStartDatePickerDialog(View v) {
	    DialogFragment newFragment = new DatePickerFragment();
	    newFragment.show(getSupportFragmentManager(), "startdatePicker");
	}
	
	public void showEndDatePickerDialog(View v) {
	    DialogFragment newFragment = new DatePickerFragment();
	    newFragment.show(getSupportFragmentManager(), "enddatePicker");
	}
}