package com.example.bike_computer

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.bike_computer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
            return
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MapsFragment())
                .commit()

            binding.bottomNavigation.selectedItemId = R.id.nav_start
        }


        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_start -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, MapsFragment())
                        .commit()
                    true
                }

                R.id.nav_compass -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, CompassFragment())
                        .commit()
                    true
                }

                R.id.nav_training -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, TrainingFragment())
                        .commit()
                    true
                }

                else -> {
                    false
                }
            }

        }


    }

}