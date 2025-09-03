package com.example.bike_computer

import android.Manifest
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import android.util.Log


class LocationService : Service() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private var isPaused = false

    private var altitudeManager: AltitudeManager? = null


    override fun onCreate() {
        super.onCreate()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            5000
        ).build()

        Log.d("AltitudeManager", "Hat Barometer: ${altitudeManager?.hasSensor()}")


        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (!isPaused) {
                    for (location in locationResult.locations) {
                        val locData = LocationData(
                            location.latitude,
                            location.longitude,
                            if (location.hasAltitude()) location.altitude else null
                        )
                        LocationViewModelProvider.getInstance().updateLocation(locData)
                    }
                }
            }
        }




        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            mainLooper
        )
        altitudeManager = AltitudeManager(applicationContext) { altMeters ->
            if (!isPaused) {
                LocationViewModelProvider.getInstance().updateAltitude(altMeters)
            }
        }.also { it.start() }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            "ACTION_PAUSE" -> pauseTracking()
            "ACTION_RESUME" -> resumeTracking()
        }

        startForeground(1, createNotification(this))
        return START_STICKY
    }
    private fun pauseTracking() {
        isPaused = true
        altitudeManager?.stop()
    }

    private fun resumeTracking() {
        isPaused = false
        altitudeManager?.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(locationCallback)
        altitudeManager?.stop()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
