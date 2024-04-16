import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.smartpark.R
import com.example.smartpark.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val selectedButtons = mutableSetOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                val buttonId = it.id
                if (selectedButtons.contains(buttonId)) {
                    selectedButtons.remove(buttonId)
                    it.setBackgroundResource(android.R.color.transparent)
                } else {
                    selectedButtons.add(buttonId)
                    //it.setBackgroundColor(R.drawable.spots_button_background)
                    it.setBackgroundResource(R.drawable.spots_button_background)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
