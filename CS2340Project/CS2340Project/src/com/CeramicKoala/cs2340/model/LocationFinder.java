package com.CeramicKoala.cs2340.model;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.model.LatLng;

public class LocationFinder implements GooglePlayServicesClient.ConnectionCallbacks, 
	GooglePlayServicesClient.OnConnectionFailedListener {
	
	private LocationClient locClient;
	private Context context;
	
	public LocationFinder(Context context) {
		this.context = context;
		locClient = new LocationClient(context, this, this);
	}
	
	/**
	 * moves camera to user's current location (last recorded location)
	 */
	public LatLng getCurrentLocation() {
		Location location = locClient.getLastLocation();
		LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
		
		return latlng;
	}

	/**
	 * @see {@link GooglePlayServicesClient.OnConnectionFailedListener}
	 */
	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		Toast.makeText(context, "Location client connection failed", Toast.LENGTH_SHORT).show();
	}

	/**
	 * @see {@link GooglePlayServicesClient.ConnectionCallbacks}
	 */
	@Override
	public void onConnected(Bundle arg0) {
		// do nothing
	}
	
	/**
	 * @see {@link GooglePlayServicesClient.ConnectionCallbacks}
	 */
	@Override
	public void onDisconnected() {
		// do nothing
	}

}
