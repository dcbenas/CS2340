package com.CeramicKoala.cs2340.activities;

import com.CeramicKoala.cs2340.R;
import com.CeramicKoala.cs2340.model.AlertDialogManager;
import com.CeramicKoala.cs2340.model.SessionManager;
import java.text.DateFormatSymbols;
import java.util.Calendar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.widget.DatePicker;
import android.app.Activity;
import android.app.Dialog;
import android.app.DatePickerDialog;
import android.app.DialogFragment;



public class ReportActivity extends Activity {
	
	//TODO Matthew - make the date range inclusive instead of exclusive
	
	private DatePickerFragmentStart startDate = new DatePickerFragmentStart();
	private DatePickerFragmentEnd endDate = new DatePickerFragmentEnd();
	private Calendar currentDay = Calendar.getInstance();
	private AlertDialogManager alertManager;
	private SessionManager sessionManager;
// DEPRECATED
//	private AlertDialog noStart;
//	private AlertDialog noEnd;
//	private AlertDialog startInFuture;
//	private AlertDialog endInFuture;
//	private AlertDialog endBeforeStart;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		//DEPRECATED intent = getIntent();
		setContentView(R.layout.activity_report);
		
		//instantiate helper objects
		alertManager = new AlertDialogManager(this);
		sessionManager = new SessionManager(this);
		
// DEPRECATED
//		noStart = setUpAlertDialog("Error","Please select a start date for your report!", false);
//		noEnd = setUpAlertDialog("Error","Please select an end date for your report!", false);
//		endBeforeStart = setUpAlertDialog("Error","Your start date must come before your end date!", false);
//		endInFuture = setUpAlertDialog("Error","Cannot generate a report that spans into the future.", false);
//		startInFuture = setUpAlertDialog("Error","Definitely cannot generate a report that starts in the future.", false);
//		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.logged_in, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		// Handle presses on the action bar items
	    switch (item.getItemId()) {
	       
	    	case R.id.log_out:
	        	sessionManager.logOut();
	            startActivity(new Intent(this, MainActivity.class));
	            return true;
	        
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	protected Intent getIntent(Class<?> activityClass) {
		Intent intent = new Intent(this, activityClass);
		return intent;
	}
	
	public void displayReport(View view) {
		if (startDate.start == null) {
			
			alertManager.generateAlertDialog(
					AlertDialogManager.AlertType.NO_START_DATE)
					.show();
		} else if (endDate.end == null) {
			
			alertManager.generateAlertDialog(
					AlertDialogManager.AlertType.NO_END_DATE)
					.show();
		} else if (startDate.startCalendar.after(endDate.endCalendar)){
			
			alertManager.generateAlertDialog(
					AlertDialogManager.AlertType.IMPROPER_DATE_RANGE)
					.show();
		} else if (startDate.startCalendar.after(currentDay)) {
			
			alertManager.generateAlertDialog(
					AlertDialogManager.AlertType.IMPROPER_DATE_RANGE)
					.show();
		//} else if (endDate.endCalendar.after(currentDay)) {
			//endInFuture.show();
		} else {
			int whereToGo = getIntent().getExtras().getInt("followUpDecision");
			switch (whereToGo) {
			case 0:
				Intent intent = new Intent(this, WithdrawalReportActivity.class);
				intent.putExtra("startDate", startDate.start);
				intent.putExtra("endDate", endDate.end);
				startActivity(intent);
				break;
				
			case 1:
				Intent transIntent = new Intent(this, TransactionHistoryActivity.class);
				transIntent.putExtra("startDate", startDate.start);
				transIntent.putExtra("endDate", endDate.end);
				startActivity(transIntent);
				break;
			
			case 2:
				Intent cashIntent = new Intent(this, CashFlowActivity.class);
				cashIntent.putExtra("startDate", startDate.start);
				cashIntent.putExtra("endDate", endDate.end);
				startActivity(cashIntent);
				break;
			}
	}
		
	}		

	
	//I'll javadoc it later, but basically they are the two date pickers
	public static class DatePickerFragmentStart extends DialogFragment implements DatePickerDialog.OnDateSetListener {
		
		private String start;
		private Calendar startCalendar;
		
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
			
			String stringMonth = new DateFormatSymbols().getMonths()[month];
			String dateString = (stringMonth + " " + day + ", " + year);
			start = dateString;
			startCalendar = Calendar.getInstance();
			startCalendar.set(year, month, day);
			}
		
	}
	
	public static class DatePickerFragmentEnd extends DialogFragment implements DatePickerDialog.OnDateSetListener {
		
		private String end;
		private Calendar endCalendar;
		
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
			
			String stringMonth = new DateFormatSymbols().getMonths()[month];
			String dateString = (stringMonth + " " + day + ", " + year);
			end = dateString;
			endCalendar = Calendar.getInstance();
			endCalendar.set(year, month, day);
		}
	}
	
	public void showStartDatePickerDialog(View v) {
	   
		DialogFragment newFragment = startDate;
	    newFragment.show(getFragmentManager(), "startdatePicker");
	}
	
	public void showEndDatePickerDialog(View v) {
	   
		DialogFragment newFragment = endDate;
	    newFragment.show(getFragmentManager(), "enddatePicker");
	}
}