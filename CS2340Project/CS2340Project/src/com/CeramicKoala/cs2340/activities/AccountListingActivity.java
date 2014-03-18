package com.CeramicKoala.cs2340.activities;

import java.text.DateFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.CeramicKoala.cs2340.R;
import com.CeramicKoala.cs2340.model.Account;
import com.CeramicKoala.cs2340.model.ReportGenerator;
import com.CeramicKoala.cs2340.model.SessionManager;
import com.CeramicKoala.cs2340.model.Transaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class AccountListingActivity extends Activity {
	private ReportGenerator reportMaker;
	private SessionManager sessionManager;
	private Calendar calendar = Calendar.getInstance();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_listing);
		
		// instantiate helper objects
		sessionManager = new SessionManager(this);
		reportMaker = new ReportGenerator(this);
		

		TextView dateInfo = (TextView) findViewById(R.id.listing_date_info);
		String stringMonth = new DateFormatSymbols().getMonths()[calendar.get(calendar.MONTH)];
		String dateString = (stringMonth + " " + calendar.get(calendar.DATE) + ", " + calendar.get(calendar.YEAR));
		String dateMessage = getString(R.string.as_of) + " " + dateString;
		dateInfo.setText(dateMessage);
		
		try {
			
			// get list of transactions specific to report
			// defaults to SPENDING REPORT right now
			List<Account> accList = reportMaker.generateAccountListingReport();
			
			// get textviews to update with report info
			TextView reportName = (TextView) findViewById(R.id.account_listing_name);
			TextView reportBal = (TextView) findViewById(R.id.account_listing_bal);
			if (accList.size() <= 0) {
				
			} else {
				String reportNameMessage = "";
				String reportBalMessage = "";
				for (Account a: accList) {
					reportNameMessage = (reportNameMessage + a.formattedAccountName());
					
					//There's probably an easier way to get this formatted right
					double bal = a.getBalance();
					NumberFormat curr = NumberFormat.getCurrencyInstance();
					String formattedBal = curr.format(bal);
					StringBuilder x = new StringBuilder();
					x.append(formattedBal + "\n");
					String finishedBal = x.toString();
					reportBalMessage = (reportBalMessage + finishedBal);
				}
				reportName.setText(reportNameMessage);
				reportBal.setText(reportBalMessage);
			}
			
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
		
		startActivity(new Intent(this, AccountHomeActivity.class));
	}

}

