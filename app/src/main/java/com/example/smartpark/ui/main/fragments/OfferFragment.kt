package com.example.smartpark.ui.main.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.smartpark.BuildConfig
import com.example.smartpark.R
import com.example.smartpark.databinding.FragmentOfferBinding
import com.example.smartpark.model.PaymentResponse
import com.example.smartpark.util.Constants
import com.google.gson.Gson
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class OfferFragment : Fragment() {

    private var _binding: FragmentOfferBinding? = null
    private val binding get() = _binding!!

    private var selectedButton: Int = -1
    private lateinit var paymentSheet: PaymentSheet

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOfferBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupButtonInteraction()

        initializePaymentSheet()

        binding.btnSubscribe.setOnClickListener {
            val priceId = if (selectedButton == 0) BuildConfig.monthlyPriceId else BuildConfig.yearlyPriceId
            val licensePlate = binding.tiePlate.text.toString()
            coroutineScope.launch {
                val paymentIntentClientSecret = createSubscription(priceId, licensePlate)
                paymentIntentClientSecret?.let {
                    paymentSheet.presentWithPaymentIntent(it.clientSecret, PaymentSheet.Configuration(
                        merchantDisplayName = "SmartPark",
                    ))
                } ?: run {
                }
            }
        }
    }

    private fun initializePaymentSheet() {
        val paymentConfiguration = PaymentConfiguration.getInstance(requireContext())
        paymentSheet = PaymentSheet(this) { paymentSheetResult ->
            onPaymentSheetResult(paymentSheetResult)
        }
    }

    private fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        when (paymentSheetResult) {
            is PaymentSheetResult.Canceled -> Log.d("PaymentSheet", "Canceled")
            is PaymentSheetResult.Failed -> Log.e("PaymentSheet", "Failed: ${paymentSheetResult.error}")
            is PaymentSheetResult.Completed -> {
                Log.d("PaymentSheet", "Completed")

            }
        }
    }

    private fun setupButtonInteraction() {
        val monthlyButton = binding.btnImgMonthly
        val yearlyButton = binding.btnImgYearly

        monthlyButton.setBackgroundResource(android.R.color.black)
        yearlyButton.setBackgroundResource(android.R.color.black)

        val buttons = listOf(monthlyButton, yearlyButton)

        for (button in buttons) {
            button.setOnClickListener {
                if (selectedButton > -1) {
                    buttons[selectedButton].setBackgroundResource(android.R.color.black)
                }
                selectedButton = buttons.indexOf(button)
                it.setBackgroundResource(R.drawable.offer_button_background)
            }
        }
    }

    private suspend fun createSubscription(priceId: String, customerId: String): PaymentResponse? = withContext(Dispatchers.IO) {
        val accessToken = getAccessToken()

        val body = JSONObject()
            .put("priceId", priceId)
            .put("licensePlate", customerId)
            .toString()
            .toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("https://parking.gonemesis.org/api/v1/payment/subscribe")
            .post(body)
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        OkHttpClient().newCall(request).execute().use { response ->
            if (response.isSuccessful) {
                val clientSecret = response.body!!.string()
                Log.d("Subscription", clientSecret)
                return@withContext PaymentResponse(clientSecret)
            } else {
                Log.d("Subscription", response.toString())
                null
            }
        }
    }

    private fun getAccessToken(): String? {
        val sharedPreferences = requireActivity().getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(Constants.ACCESS_TOKEN_KEY, null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        coroutineScope.cancel()
        _binding = null
    }
}
