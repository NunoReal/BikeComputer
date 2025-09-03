package com.example.bike_computer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.content.Intent
import com.google.android.material.button.MaterialButton
import android.os.SystemClock
import android.widget.Chronometer



class TrackingDataFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_tracking_data, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val altitudeCard = view.findViewById<View>(R.id.card_altitude)
        val elevationCard = view.findViewById<View>(R.id.card_elevation_gain)

        val altTitle = altitudeCard.findViewById<TextView>(R.id.card_title)
        val altValue = altitudeCard.findViewById<TextView>(R.id.card_value)

        val elevTitle = elevationCard.findViewById<TextView>(R.id.card_title)
        val elevValue = elevationCard.findViewById<TextView>(R.id.card_value)

        altTitle.text = "Aktuelle Höhe"
        elevTitle.text = "Höhenmeter"

        val latitudeCard = view.findViewById<View>(R.id.card_latitude)
        val longitudeCard = view.findViewById<View>(R.id.card_longitude)
        val latTitle = latitudeCard.findViewById<TextView>(R.id.card_title)
        val latValue = latitudeCard.findViewById<TextView>(R.id.card_value)
        val lonTitle = longitudeCard.findViewById<TextView>(R.id.card_title)
        val lonValue = longitudeCard.findViewById<TextView>(R.id.card_value)
        latTitle.text = "Latitude"
        lonTitle.text = "Longitude"

        val locationViewModel = LocationViewModelProvider.getInstance()

        locationViewModel.altitude.observe(viewLifecycleOwner) { h ->
            altValue.text = if (h != null) {
                String.format("%.1f m", h)
            } else {
                "--"
            }
        }

        locationViewModel.elevationGain.observe(viewLifecycleOwner) { gain ->
            elevValue.text = String.format("%.1f m", gain)
        }

        locationViewModel.locationData.observe(viewLifecycleOwner) { location ->
            latValue.text = location.latitude.toString()
            lonValue.text = location.longitude.toString()
        }


        val timer = view.findViewById<Chronometer>(R.id.tracking_timer)
        var pauseOffset: Long = 0L

        timer.base = SystemClock.elapsedRealtime()
        timer.start()

        val pauseButton = view.findViewById<MaterialButton>(R.id.pause_button)
        var isPaused = false

        pauseButton.setOnClickListener {
            val intent = Intent(requireContext(), LocationService::class.java)
            if (isPaused) {
                intent.action = "ACTION_RESUME"
                requireContext().startService(intent)

                timer.base = SystemClock.elapsedRealtime() - pauseOffset
                timer.start()

                pauseButton.setIconResource(R.drawable.icon_pause)
            } else {
                intent.action = "ACTION_PAUSE"
                requireContext().startService(intent)

                pauseOffset = SystemClock.elapsedRealtime() - timer.base
                timer.stop()

                pauseButton.setIconResource(R.drawable.icon_play_arrow)
            }
            isPaused = !isPaused
        }

        val stopButton = view.findViewById<View>(R.id.stop_button)
        stopButton.setOnClickListener {
            requireContext().stopService(Intent(requireContext(), LocationService::class.java))

            timer.stop()
            timer.base = SystemClock.elapsedRealtime()
            pauseOffset = 0L
            isPaused = false

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MapsFragment())
                .commit()
        }
    }


}