package com.example.bike_computer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.content.Intent
import com.google.android.material.button.MaterialButton

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class TrackingDataFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_tracking_data, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val latitudeCard = view.findViewById<View>(R.id.card_latitude)
        val longitudeCard = view.findViewById<View>(R.id.card_longitude)

        val latTitle = latitudeCard.findViewById<TextView>(R.id.card_title)
        val latValue = latitudeCard.findViewById<TextView>(R.id.card_value)

        val lonTitle = longitudeCard.findViewById<TextView>(R.id.card_title)
        val lonValue = longitudeCard.findViewById<TextView>(R.id.card_value)

        latTitle.text = "Latitude"
        lonTitle.text = "Longitude"

        val locationViewModel = LocationViewModelProvider.getInstance()
        locationViewModel.locationData.observe(viewLifecycleOwner) { location ->
            latValue.text = location.latitude.toString()
            lonValue.text = location.longitude.toString()
        }

        val pauseButton = view.findViewById<MaterialButton>(R.id.pause_button)
        var isPaused = false

        pauseButton.setOnClickListener {
            val intent = Intent(requireContext(), LocationService::class.java)
            if (isPaused) {
                intent.action = "ACTION_RESUME"
                requireContext().startService(intent)
                pauseButton.setIconResource(R.drawable.icon_pause)
            } else {
                intent.action = "ACTION_PAUSE"
                requireContext().startService(intent)
                pauseButton.setIconResource(R.drawable.icon_play_arrow)
            }
            isPaused = !isPaused
        }

        val stopButton = view.findViewById<View>(R.id.stop_button)
        stopButton.setOnClickListener {
            requireContext().stopService(Intent(requireContext(), LocationService::class.java))

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MapsFragment())
                .commit()
        }
    }




    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TrackingDataFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}