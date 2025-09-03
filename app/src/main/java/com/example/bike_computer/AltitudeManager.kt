package com.example.bike_computer

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class AltitudeManager(
    context: Context,
    private val onAltitude: (Double) -> Unit
) : SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val pressureSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)


    private var emaAltitude: Double? = null
    private val alpha = 0.1

    fun hasSensor(): Boolean = pressureSensor != null

    fun start() {
        pressureSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)
        }
    }

    fun stop() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        val pressureHpa = event.values[0]
        val alt = android.hardware.SensorManager.getAltitude(
            android.hardware.SensorManager.PRESSURE_STANDARD_ATMOSPHERE,
            pressureHpa
        ).toDouble()

        emaAltitude = if (emaAltitude == null) alt else (emaAltitude!! + alpha * (alt - emaAltitude!!))
        emaAltitude?.let(onAltitude)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
