/******************************************************************************
 *
 *  2015 (C) Copyright Open-RnD Sp. z o.o.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 ******************************************************************************/

package pl.openrnd.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.List;

/**
 * LocationManagera
 */
public class LocationManager {
    private static final String TAG = LocationManager.class.getSimpleName();

    public static final int REQUEST_LOCATION_ERROR = 9000;

    private final long ONE_SECOND = 1000;
    private long mLocationInterval = ONE_SECOND * 15;
    private long mFastLocationInterval = ONE_SECOND * 5;
    private int mPriority = LocationRequest.PRIORITY_HIGH_ACCURACY;
    private int mSmallestDisplacement;

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    private ObjectListenerHandler<LocationListener> mExternalLocationListeners;

    private Dialog mDialogError;

    private Boolean mAreLocationUpdatesRequested = false;
    private boolean mIsRegistrationPending = false;

    private Context mContext;

    // Bool to track whether the app is already resolving an error
    private boolean mResolvingError = false;


    /**
     * Class constructor
     *
     * @param context Application context
     * @param interval Value in millis. Default is set 15 sec
     * @param fastInterval Value in millis. Default is set 5 sec
     * @param priority Priority of location
     */
    public LocationManager(Context context, long interval, long fastInterval, int priority) {
        mContext = context;
        mFastLocationInterval = fastInterval;
        mLocationInterval = interval;
        mPriority = priority;
        mSmallestDisplacement = 0;
        initData();
    }

    /**
     * Class constructor
     *
     * @param context Application context
     * @param smallestDisplacement Smallest displacement in meters
     */
    public LocationManager(Context context, int smallestDisplacement) {
        mContext = context;
        mSmallestDisplacement = smallestDisplacement;
        initData();
    }

    /**
     * Class constructor
     *
     * user location interval -  Default is set 15 sec
     * user fast location interval - Default is set 5 sec
     * priority = Default is set to PRIORITY_HIGH_ACCURACY
     * @param context - - app context
     */
    public LocationManager(Context context) {
        mContext = context;
        initData();
    }

    private void initData() {
        Log.d(TAG, "initData()");
        mExternalLocationListeners = new ObjectListenerHandler<LocationListener>();
        setUpGoogleApiClientIfNeeded();
        createLocationRequest();
        connect();
    }

    private void setUpGoogleApiClientIfNeeded() {
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(mConnectionCallbacks)
                .addOnConnectionFailedListener(mConnectionFailedListener)
                .build();
    }

    private void connect() {
        Log.d(TAG, "connect()");
        if(mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }else{
            Log.e(TAG, "connect(), cannot create GoogleApiClient");
        }
    }

    public void reconnect(){
        stopLocation();
        setUpGoogleApiClientIfNeeded();
        createLocationRequest();
        connect();
    }

    public void stopLocation() {
        Log.d(TAG, "stopLocation()");
        if(mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected()) {
                unregisterLocationUpdates();
            }
            mGoogleApiClient.disconnect();
        }
    }

    private void createLocationRequest() {
        Log.d(TAG, "createLocationRequest()");
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(mPriority);
        mLocationRequest.setInterval(mLocationInterval);
        mLocationRequest.setFastestInterval(mFastLocationInterval);
        mLocationRequest.setSmallestDisplacement(mSmallestDisplacement);
    }

    private boolean areLocationUpdatesRequested() {
        synchronized (mAreLocationUpdatesRequested) {
            return mAreLocationUpdatesRequested;
        }
    }

    private void registerLocationUpdates() {
        synchronized (mAreLocationUpdatesRequested) {
            if (mGoogleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.requestLocationUpdates(
                        mGoogleApiClient,
                        mLocationRequest,
                        mLocationListener);
            } else {
                mIsRegistrationPending = true;
            }

            mAreLocationUpdatesRequested = true;
        }
    }

    private void unregisterLocationUpdates() {
        synchronized (mAreLocationUpdatesRequested) {
            if (mGoogleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, mLocationListener);
            }

            mIsRegistrationPending = false;
            mAreLocationUpdatesRequested = false;
        }
    }

    private LocationListener mLocationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            Log.d(TAG, String.format("onLocationChanged(): location[%s]", location));
            mLastLocation = location;
            notifyLocationChange(mLastLocation);
        }
    };

    private GoogleApiClient.ConnectionCallbacks mConnectionCallbacks = new GoogleApiClient.ConnectionCallbacks() {

        @Override
        public void onConnected(Bundle dataBundle) {
            Log.d(TAG, "onConnected()");

            synchronized (mAreLocationUpdatesRequested) {
                if (mIsRegistrationPending) {
                    registerLocationUpdates();
                    mIsRegistrationPending = false;
                    mLocationListener.onLocationChanged(getLocation());

                }
            }
        }

        @Override
        public void onConnectionSuspended(int i) {
            Log.d(TAG, "onConnectionSuspended(), errorCode " + i);

        }
    };

    private Location getLocation() {
        Location lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        return lastKnownLocation;
    }

    private GoogleApiClient.OnConnectionFailedListener mConnectionFailedListener = new GoogleApiClient.OnConnectionFailedListener() {

        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {
            Log.d(TAG, String.format("onConnectionFailed(): errorCode[%d]", connectionResult.getErrorCode()));
            if (mResolvingError) {
                // Already attempting to resolve an error.
                return;
            }
            if (connectionResult.hasResolution()) {
                try {
                    mResolvingError = true;
                    connectionResult.startResolutionForResult((Activity) mContext, REQUEST_LOCATION_ERROR);
                } catch (IntentSender.SendIntentException e) {
                    // There was an error with the resolution intent. Try again.
                    mGoogleApiClient.connect();
                }
            } else {
                showErrorDialogs(connectionResult.getErrorCode());
                mResolvingError = true;
            }
        }
    };

    private void showErrorDialogs(int error) {
        if (mDialogError != null) {
            mDialogError.hide();
        }
        mDialogError = GooglePlayServicesUtil.getErrorDialog(error, (Activity) mContext, REQUEST_LOCATION_ERROR);
        mDialogError.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_LOCATION_ERROR) {
            mResolvingError = false;
            if (resultCode == Activity.RESULT_OK) {
                // Make sure the app is not already connected or attempting to connect
                if (!mGoogleApiClient.isConnecting() &&
                        !mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }
            }
        }
    }

    /**
     * @param listener which return location data, when return null Options Location in system is disabled
     */
    public void addLocationListener(LocationListener listener) {
        if ((mExternalLocationListeners.registerObjectWeakListener(listener) > 0) && !areLocationUpdatesRequested()) {
            registerLocationUpdates();
        }
    }

    public void removeLocationListener(LocationListener listener) {
        if ((mExternalLocationListeners.unregisterObjectWeakListener(listener) == 0) && areLocationUpdatesRequested()) {
            unregisterLocationUpdates();
        }
    }

    public Location getLastKnowLocation() {
        return mLastLocation;
    }

    private void notifyLocationChange(final Location location) {
        mExternalLocationListeners.notifyObjectChange(new ObjectListenerHandler.NotificationHandler<LocationListener>() {
            @Override
            public void runOnListener(LocationListener listener) {
                listener.onLocationChanged(location);
            }
        });
    }

    public boolean isLocationEnable() {
        android.location.LocationManager locationManager = (android.location.LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getAllProviders();
        try {
            if (locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
                return true;
            }
        } catch (SecurityException e) {
            //Before Lollipop system throw this exception
        }
        try {
            if (locationManager.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)) {
                return true;
            }
        } catch (SecurityException e) {

        }
        return false;
    }
}
