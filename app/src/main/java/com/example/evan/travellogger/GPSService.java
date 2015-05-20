package com.example.evan.travellogger;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.example.evan.travellogger.interfaces.GPSListener;

public class GPSService extends Service implements LocationListener {

    private LocationManager locationManager;
    int mStartMode;
    IBinder mBinder;
    boolean mAllowRebind;

    private Location location = null;

    private GPSListener listener;
    private final IBinder localBinder = new LocalBinder();

    public void onCreate() {}

    public int onStartCommand(Intent intent, int flags, int startId) {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        return 1;
    }

    public void onDestroy() {
        Log.i("GPSService", "ondestroy called");
        if(locationManager != null) {
            locationManager.removeUpdates(this);
            locationManager = null;
        }
        super.onDestroy();

    }

    public void addGPSListener(GPSListener listener) {
        this.listener = listener;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return localBinder;
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        //txtLat.setText("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
        Log.v("latitude", String.valueOf(location.getLatitude()));
        Log.v("longitude", String.valueOf(location.getLongitude()));
        if(listener != null) {
            listener.locationUpdate(location);
        }
    }

    public boolean hasLocation() {
        return location != null;
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude", "disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }

    public Location getLocation() {
        return location;
    }

    public class LocalBinder extends Binder {
        GPSService getService() {
            return GPSService.this;
        }
    }

}
