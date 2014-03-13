package com.CeramicKoala.cs2340.activities;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.CeramicKoala.cs2340.R;
import com.CeramicKoala.cs2340.model.AccountOpenHelper;
import com.CeramicKoala.cs2340.model.DatabaseException;
import com.CeramicKoala.cs2340.model.ReportGenerator;
import com.CeramicKoala.cs2340.model.ReportGenerator.ReportType;
import com.CeramicKoala.cs2340.model.Transaction;

public class DisplayReportActivity extends AccountManagementActivity {
	private ReportGenerator reportMaker;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		intent = getIntent();
		setContentView(R.layout.activity_display_report);
		TextView reportHeader = (TextView) findViewById(R.id.withdrawal_report_header);
		String loginMessage = getString(R.string.withdrawal_report) + " "+ loginHelper.getCurrentUser().getFullName();
		System.out.println(loginMessage);
		reportHeader.setText(loginMessage);
		
		//For start / end dates to go with report generator functionality
		String start = getIntent().getExtras().getString("startDate");
		String end = getIntent().getExtras().getString("endDate");
		TextView dateInfo = (TextView) findViewById(R.id.report_date_info);
		String dateMessage = "From " + start + " to " + getString(R.string.date_start_to_end) + end;
		dateInfo.setText(dateMessage);
		
		
		reportMaker = new ReportGenerator(this, loginHelper.getCurrentUser());
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
			Date beginning = formatter.parse(start);
			Date endDate = formatter.parse(end);
			
			List<Transaction> spendingReport = reportMaker.generateReport(
					ReportGenerator.ReportType.SPENDING_REPORT, 
					beginning, 
					endDate);
			TextView report = (TextView) findViewById(R.id.spending_report);
			String reportMessage = null;
			for (Transaction t: spendingReport) {
				reportMessage = (reportMessage + t.toString());
				reportMessage = (reportMessage + "/n");
			}
			report.setText(reportMessage);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
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
	
	public void goAccountHome(View view) {
		startActivity(getIntent(AccountHomeActivity.class));
	}

}
