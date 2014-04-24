package com.CeramicKoala.cs2340.activities;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.CeramicKoala.cs2340.R;
import com.CeramicKoala.cs2340.model.ReportGenerator;
import com.CeramicKoala.cs2340.model.SessionManager;

public class DepositReportActivity extends Activity {
	
	private ReportGenerator reportMaker;
	private SessionManager sessionManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_deposit_report);
		
		// instantiate helper objects
		sessionManager = new SessionManager(this);
		reportMaker = new ReportGenerator(this);
		//DEPRECATED intent = getIntent();
		
		// set textview with appropriate report header
		// defaults to withdrawal report header right now
		TextView reportHeader = (TextView) findViewById(R.id.deposit_report_header);
		String loginMessage = getString(R.string.deposit_report) + " "
				+ sessionManager.getUser().getFullName();
		reportHeader.setText(loginMessage);
		
		//For start / end dates to go with report generator functionality
		// get start and end date strings from intent 
		String start = getIntent().getExtras().getString("startDate");
		String end = getIntent().getExtras().getString("endDate");
		
		// set textview with info about date range
		TextView dateInfo = (TextView) findViewById(R.id.report_date_info);
		String dateMessage = "From " + start + " to " + getString(R.string.date_start_to_end) + end;
		dateInfo.setText(dateMessage);
		
		try {
			
			// format start and end date strings into Date objects
			SimpleDateFormat formatter = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
			Date beginning = formatter.parse(start);
			Date endDate = formatter.parse(end);
			
			// get list of transactions specific to report
			// defaults to SPENDING REPORT right now
			String [] reportTitles = new String[6];
			reportTitles[0] = "Salary";
			reportTitles[1] = "Gift";
			reportTitles[2] = "Parents";
			reportTitles[3] = "Scholarship";
			reportTitles[4] = "Other";
			reportTitles[5] = "Total";
					
			
			double[] depositReport = reportMaker.generateDepositReport(beginning, endDate);
			System.out.println(depositReport[5]);
			
			// get textviews to update with report info
			TextView reportDate = (TextView) findViewById(R.id.deposit_report);
			TextView reportAmount = (TextView) findViewById(R.id.income_report_amount);
			
			// generate strings with report column headers
			String reportDateMessage = getString(R.string.category);
			String reportAmountMessage = getString(R.string.amount);
			
			// loop through list of transactions and populate strings with transaction info
			for (int i = 0; i < 6; i++) {
				NumberFormat format = NumberFormat.getCurrencyInstance();
				String amount = format.format(depositReport[i]);
				reportDateMessage = (reportDateMessage + "\n" + reportTitles[i]);
				reportAmountMessage = (reportAmountMessage + "\n" + amount);
			}
			
			// set textviews with transaction report strings that were just populated
			reportDate.setText(reportDateMessage);
			reportAmount.setText(reportAmountMessage);
		} catch (Exception e) {
			
			//TODO what exceptions can we expect from this try block?
			e.printStackTrace();
		}
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

// DEPRECATED
//	protected Intent getIntent(Class<?> activityClass) {
//		
//		Intent intent = new Intent(this, activityClass);
//		return intent;
//	}
	
	public void goAccountHome(View view) {
		
		startActivity(new Intent(this, LogInActivity.class));
	}

}

