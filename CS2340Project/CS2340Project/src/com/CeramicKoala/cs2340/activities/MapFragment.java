package com.CeramicKoala.cs2340.activities;

import java.util.List;

import android.os.Bundle;
import android.util.Log;

import com.CeramicKoala.cs2340.model.LocationFinder;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * MapFragment contains the layout for the map
 * @author Benjamin Newcomer
 */
public class MapFragment extends SupportMapFragment {

	/**
	 * tag for use with {@link android.util.Log}
	 */
	private static final String TAG = "MapFragment";
	
	/**
	 * constant for use with {@link MapFragment#goToLocation(Address, boolean))}
	 */
	static final float DEFAULT_ZOOM = 13;
	
	/**
	 * instance fields
	 */
	private GoogleMap map;
	private LocationFinder locFinder;
	private boolean animate;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		// instantiate instance fields
		animate = false;
		locFinder = new LocationFinder(getActivity());
		map = getMap();
		
		// emable location finder button for map
		map.setMyLocationEnabled(true);
	}
	
	/**
	 * @see {@link MapFragment#goToLocation(CameraUpdate, boolean)
	 */
	public Marker goToLocation(LatLng latlng, float zoom, boolean animateLocation) {
		
		if (map == null) map = getMap();
		
		CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latlng, zoom);
		MarkerOptions options = new MarkerOptions().position(latlng).draggable(false);
		
		if (animate) {
			map.animateCamera(update);
		} else {
			map.moveCamera(update);
		}
		
		return addMarker(options);
	}
	
	/**
	 * adds a marker to the map, specified by
	 * MarkerOptions options
	 * @param options
	 */
	public Marker addMarker(MarkerOptions options) {
		return map.addMarker(options);
	}
	
	/**
	 * moves the map to the user's current location using {@link LocationFinder}
	 */
	public Marker goToCurrentLocation() {
		return goToLocation(locFinder.getCurrentLocation(), DEFAULT_ZOOM, false);
	}
}
