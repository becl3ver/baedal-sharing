package com.fourfifths.android.baedalsharing

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.naver.maps.geometry.LatLng

class LocationProvider(val context: Context) {
    private var location: Location? = null
    private var locationManager: LocationManager? = null

    init {
        setLocation()
    }

    private fun setLocation() {
        try {
            locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            var gpsLocation: Location? = null
            var networkLocation: Location? = null

            val isGPSEnabled = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled =
                locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            if (!isGPSEnabled && !isNetworkEnabled) {
                return
            }

            val hasFineLocationPermission =
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

            if (!hasFineLocationPermission && !hasCoarseLocationPermission) {
                return
            }

            if (isGPSEnabled) {
                gpsLocation = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            }

            if(isNetworkEnabled)  {
                networkLocation = locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            }

            location = if(gpsLocation != null && networkLocation != null) {
                if(gpsLocation.accuracy > networkLocation.accuracy) gpsLocation else networkLocation
            } else {
                gpsLocation ?: networkLocation
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getLatitude(): Double? {
        return location?.latitude
    }

    fun getLongitude(): Double? {
        return location?.longitude
    }

    fun getLocation(): Location? {
        return location
    }

    fun getLatLng(): LatLng? {
        return if(location == null) {
            null
        } else {
            LatLng(location!!.latitude, location!!.longitude)
        }
    }
}