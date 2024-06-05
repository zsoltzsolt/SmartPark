package com.example.smartpark.ui.main


import com.example.smartpark.R
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartpark.databinding.ActivityMainBinding
import com.example.smartpark.ui.main.fragments.HomeFragment
import com.example.smartpark.ui.main.fragments.MapsFragment
import com.example.smartpark.ui.main.fragments.OfferFragment
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


        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, HomeFragment())
        transaction.commit()

        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.mi_home -> {
                    val fragmentManager = supportFragmentManager
                    val transaction = fragmentManager.beginTransaction()
                    transaction.replace(R.id.frame_layout, HomeFragment())
                    transaction.commit()
                    true
                }

                R.id.mi_sessions -> {
                    val fragmentManager = supportFragmentManager
                    val transaction = fragmentManager.beginTransaction()
                    transaction.replace(R.id.frame_layout, SessionsFragment())
                    transaction.commit()
                    true
                }

                R.id.mi_map -> {
                    val fragmentManager = supportFragmentManager
                    val transaction = fragmentManager.beginTransaction()
                    transaction.replace(R.id.frame_layout, MapsFragment())
                    transaction.commit()
                    true
                }

                R.id.mi_profile -> {
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



