package com.example.smartpark.ui.main

import android.app.Application
import com.stripe.android.PaymentConfiguration

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeStripe()
    }

    private fun initializeStripe() {
        PaymentConfiguration.init(
            applicationContext,
            "pk_test_51PATeDRsAXq8UeQDv6ucKJ7odtN1LaifKwrFcjiPiftXrBaJCD5n5PIPKiAH1sxSpv0kh85Lev9LBXoQtmk01bvO00eiwrhhvh"
        )
    }
}
