package com.CeramicKoala.cs2340.activities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.CeramicKoala.cs2340.R;
import com.CeramicKoala.cs2340.activities.MapFragment;
import com.CeramicKoala.cs2340.model.DatabaseException;
import com.CeramicKoala.cs2340.model.ReportGenerator;
import com.CeramicKoala.cs2340.model.SessionManager;
import com.CeramicKoala.cs2340.model.Transaction;
import com.google.android.gms.maps.model.Marker;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MapActivity extends FragmentActivity {

	private List<Marker> markers;
	private MapFragment mapFrag;
	private SessionManager sessionManager;
	private ReportGenerator reportMaker;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		sessionManager = new SessionManager(this);
		reportMaker = new ReportGenerator(this);
		markers = new ArrayList<Marker>();
		
		mapFrag = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
		showTransactions();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void showTransactions() {
		
		//For start / end dates to go with report generator functionality
		// get start and end date strings from intent 
		String start = getIntent().getExtras().getString("start");
		String end = getIntent().getExtras().getString("end");
		
		// format start and end date strings into Date objects
		SimpleDateFormat formatter = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
		Date beginning = null;
		Date endDate = null;
		try {
			beginning = formatter.parse(start);
			endDate = formatter.parse(end);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		
		// get list of transactions specific to report
		// defaults to SPENDING REPORT right now
		List<Transaction> transactionHistory = null;
		try {
			transactionHistory = reportMaker.generateReport(
					ReportGenerator.ReportType.TRANSACTION_HISTORY, 
					beginning, 
					endDate,
					sessionManager.getAccountId());
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		
		for (Transaction trans : transactionHistory) {
			markers.add(mapFrag.goToLocation(trans.getLocation(), MapFragment.DEFAULT_ZOOM, true));
		}
	}

}
