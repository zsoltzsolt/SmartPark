package com.example.smartpark.ui.main.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.smartpark.R
import com.example.smartpark.databinding.FragmentHomeBinding
import com.example.smartpark.databinding.FragmentOfferBinding


class OfferFragment : Fragment() {

    private var _binding: FragmentOfferBinding? = null
    private val binding get() = _binding!!

    private var selectedButton: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOfferBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

}