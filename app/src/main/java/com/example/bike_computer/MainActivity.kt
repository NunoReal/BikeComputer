package com.example.bike_computer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bike_computer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_start -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment_content_main, SecondFragment()).commit()
                    true
                }
                R.id.nav_kompass -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment_content_main, KompassFragment()).commit()
                    true
                }
                R.id.nav_training -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment_content_main, TrainingFragment()).commit()
                    true
                }

                else -> {
                    false
                }
            }

        }


    }

}