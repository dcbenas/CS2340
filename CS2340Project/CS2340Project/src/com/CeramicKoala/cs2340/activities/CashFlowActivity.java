package com.CeramicKoala.cs2340.activities;

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


//TODO Casey make the giant space between date and type go away

/**
 * Activity that displays transaction history for an account
 * 
 * @author Matthew Berman
 *
 */
public class CashFlowActivity extends Activity {
	
	private ReportGenerator reportMaker;
	private SessionManager sessionManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cash_flow);
		
		// instantiate helper objects
		sessionManager = new SessionManager(this);
		reportMaker = new ReportGenerator(this);
		
		// set textview with appropriate report header
		// defaults to withdrawal report header right now
		TextView reportHeader = (TextView) findViewById(R.id.cash_flow_header);
		String loginMessage = getString(R.string.cashflow_history) + " "
				+ sessionManager.getAccount().getName();
		reportHeader.setText(loginMessage);
		
		//For start / end dates to go with report generator functionality
		// get start and end date strings from intent 
		String start = getIntent().getExtras().getString("startDate");
		String end = getIntent().getExtras().getString("endDate");
		
		// set textview with info about date range
		TextView dateInfo = (TextView) findViewById(R.id.cash_flow_date);
		String dateMessage = "From " + start + " to " + getString(R.string.date_start_to_end) + end;
		dateInfo.setText(dateMessage);
		
		try {

			double[] cashFlowData = reportMaker.generateCashFlowReport();
			
			// get textviews to update with report info
			TextView reportTitles = (TextView) findViewById(R.id.cash_flow_titles);
			TextView reportBalances = (TextView) findViewById(R.id.cash_flow_balances);
			
			// set textviews with transaction report strings that were just populated
			reportTitles.setText(reportMaker.getTitles());
			reportBalances.setText(reportMaker.formatValues(cashFlowData));
			
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
	
	/**
	 * Returns user to the account home screen 
	 * 
	 * @param view the current view
	 */
	public void goUserHome(View view) {
		
		startActivity(new Intent(this, LogInActivity.class));
	}

}
