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

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var selectedButton: Int = -1


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val webSocketManager = WebSocketManager()

        val webSocketUrl = "wss://parking.gonemesis.org/config"

        webSocketManager.connectToWebSocket(webSocketUrl)


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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
