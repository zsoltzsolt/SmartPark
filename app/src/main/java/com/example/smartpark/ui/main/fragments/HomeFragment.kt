package com.example.smartpark.ui.main.fragments

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.smartpark.R
import com.example.smartpark.data.network.WebSocketManager
import com.example.smartpark.databinding.FragmentHomeBinding
import com.example.smartpark.model.WebSocketResponse

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var selectedButton: Int = -1

    private val webSocketManager by lazy {
        context?.let { WebSocketManager(it) }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val webSocketUrl = "wss://parking.gonemesis.org/config"

        webSocketManager?.connectToWebSocket(webSocketUrl){ response ->

            updateSpotButtons(response)

            Log.d("WebSocket", "Received response: $response")
        }


        binding.btnPark.isEnabled = false

        val buttons = listOf(
            binding.button01,
            binding.button02,
            binding.button03,
            binding.button04,
            binding.button05,
            binding.button06,
            binding.button07,
            binding.button08
        )

        for (button in buttons) {
            button.setOnClickListener {
                Log.d("Button", selectedButton.toString())
                if(selectedButton > -1){
                    buttons[selectedButton].setBackgroundResource(android.R.color.transparent)
                    (buttons[selectedButton] as Button).setTextColor(resources.getColor(R.color.white))
                }
                selectedButton = buttons.indexOf(button)
                it.setBackgroundResource(R.drawable.spots_button_background)
                (it as Button).setTextColor(resources.getColor(R.color.robin_egg_blue))
                binding.btnPark.isEnabled = true
                binding.btnPark.text = "Park at slot ${selectedButton + 1}"
            }
        }
    }

    private fun updateSpotButtons(response: WebSocketResponse) {
        val spotButtons = listOf(
            binding.spot1,
            binding.spot2,
            binding.spot3,
            binding.spot4,
            binding.spot5,
            binding.spot6,
            binding.spot7,
            binding.spot8
        )

        val buttons = listOf(
            binding.button01,
            binding.button02,
            binding.button03,
            binding.button04,
            binding.button05,
            binding.button06,
            binding.button07,
            binding.button08
        )

        for ((index, button) in spotButtons.withIndex()) {
            val isSpotOccupied = when (index) {
                0 -> response.parkingSpot1
                1 -> response.parkingSpot2
                2 -> response.parkingSpot3
                3 -> response.parkingSpot4
                4 -> response.parkingSpot5
                5 -> response.parkingSpot6
                6 -> response.parkingSpot7
                7 -> response.parkingSpot8
                else -> false
            }

            if (isSpotOccupied) {
                if(index % 2 == 0) {
                    button.setImageResource(R.drawable.car2)
                    buttons.get(index).setBackgroundResource(android.R.color.transparent)
                    buttons.get(index).setTextColor(getResources().getColor(R.color.white))
                }
                else
                    button.setImageResource(R.drawable.car)
            } else {
                button.setImageDrawable(null)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
