package com.example.smartpark.ui.main

import com.example.smartpark.R
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartpark.databinding.ActivityMainBinding
import com.example.smartpark.ui.main.fragments.HomeFragment
import com.example.smartpark.ui.main.fragments.MapsFragment
import com.example.smartpark.ui.main.fragments.ProfileFragment
import com.example.smartpark.ui.main.fragments.SessionsFragment


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )


        //binding.bottomNavigationView.background = null
        //binding.bottomNavigationView.menu.getItem(2).isEnabled = false
        Toast.makeText(this, "Okkk", Toast.LENGTH_SHORT).show()
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, HomeFragment())
        transaction.commit()

        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.mi_home -> {
                    Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
                    val fragmentManager = supportFragmentManager
                    val transaction = fragmentManager.beginTransaction()
                    transaction.replace(R.id.frame_layout, HomeFragment())
                    transaction.commit()
                    true
                }

                R.id.mi_sessions -> {
                    Toast.makeText(this, "Sessions", Toast.LENGTH_SHORT).show()
                    val fragmentManager = supportFragmentManager
                    val transaction = fragmentManager.beginTransaction()
                    transaction.replace(R.id.frame_layout, SessionsFragment())
                    transaction.commit()
                    true
                }

                R.id.mi_map -> {
                    Toast.makeText(this, "Maps", Toast.LENGTH_SHORT).show()
                    val fragmentManager = supportFragmentManager
                    val transaction = fragmentManager.beginTransaction()
                    transaction.replace(R.id.frame_layout, MapsFragment())
                    transaction.commit()
                    true
                }

                R.id.mi_profile -> {
                    Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show()
                    val fragmentManager = supportFragmentManager
                    val transaction = fragmentManager.beginTransaction()
                    transaction.replace(R.id.frame_layout, ProfileFragment())
                    transaction.commit()
                    true
                }
                else -> false
            }
        }
    }
}



