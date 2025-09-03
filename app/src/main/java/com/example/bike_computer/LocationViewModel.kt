package com.example.bike_computer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LocationViewModel : ViewModel() {
    private val _locationData = MutableLiveData<LocationData>()
    val locationData: LiveData<LocationData> = _locationData

    private val _altitude = MutableLiveData<Double>()
    val altitude: LiveData<Double> = _altitude

    private val _elevationGain = MutableLiveData<Double>(0.0)
    val elevationGain: LiveData<Double> = _elevationGain

    private var lastAltitude: Double? = null
    private var baseAltitude: Double? = null


    private val gainThresholdMeters = 0.3

    fun updateLocation(location: LocationData) {
        _locationData.postValue(location)
        if (_altitude.value == null && location.altitude != null) {
            _altitude.postValue(location.altitude)
        }
    }


    fun updateAltitude(smoothedAltMeters: Double) {
        if (baseAltitude == null) baseAltitude = smoothedAltMeters


        lastAltitude?.let { last ->
            val diff = smoothedAltMeters - last
            if (diff > gainThresholdMeters) {
                _elevationGain.postValue((_elevationGain.value ?: 0.0) + diff)
            }
        }
        lastAltitude = smoothedAltMeters




        _altitude.postValue(smoothedAltMeters)
    }

    fun resetElevation() {
        _elevationGain.postValue(0.0)
        lastAltitude = null
        baseAltitude = null
    }
}
