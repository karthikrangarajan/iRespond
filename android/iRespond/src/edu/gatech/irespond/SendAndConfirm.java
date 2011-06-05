package edu.gatech.irespond;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;

public class SendAndConfirm extends Activity implements LocationListener {

	SMSObject smsO;
	final String number = "4089156138";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sendandconfirm);

		Intent i = getIntent();
		smsO = (SMSObject) i.getParcelableExtra("sms");

		loadCoords();
		smsO.makefinalMsg();

//		Toast.makeText(this.getBaseContext(),
//				smsO.toString(),
//				Toast.LENGTH_LONG).show();

		// Log.v("FINAL SMS: ",smsO.toString());

		SmsManager sm = SmsManager.getDefault();
		// here is where the destination of the text should go
		
		sm.sendTextMessage(number, null, smsO.finalMsg, null, null);
		
	}

	private LocationManager mgr;
	private String best;

	double lat = 0, lon = 0;

	// String slat = "", slon = "";

	// Getting the GPS coordinates
	public void loadCoords() {
		// TextView latText = (TextView) findViewById(R.id.latText);
		// TextView lngText = (TextView) findViewById(R.id.lngText);

		mgr = (LocationManager) getSystemService(LOCATION_SERVICE);

		Criteria criteria = new Criteria();
		best = mgr.getBestProvider(criteria, true);
		mgr.requestLocationUpdates(best, 0, 0, this);

		Location location = mgr.getLastKnownLocation(best);

		lat = location.getLatitude();
		lon = location.getLongitude();

		smsO.lat = lat;
		smsO.lon = lon;

		// slat = Double.toString(lat);
		// slon = Double.toString(lon);

		Log.d(" @Home lat: ", "" + lat);
		Log.d("@Home lon: ", "" + lon);

		// Log.d("@Home slat: ", "" + slat);
		// Log.d("@Home slon: ", "" + slon);

		LocationManager myManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		String best = myManager.getBestProvider(criteria, true);
		double latPoint = myManager.getLastKnownLocation(best).getLatitude();
		double lngPoint = myManager.getLastKnownLocation(best).getLongitude();
		// latText.setText(Double.toString(latPoint));
		// lngText.setText(Double.toString(lngPoint));
		Log.d("last known  lat: ", "" + latPoint);
		Log.d("last known lon: ", "" + lngPoint);

	}

	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub

	}

	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

}
