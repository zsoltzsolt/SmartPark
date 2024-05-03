package com.example.smartpark.ui.main.fragments

import android.R
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import com.example.smartpark.databinding.FragmentProfileBinding
import com.example.smartpark.model.ProfileResponse
import com.example.smartpark.ui.login.LoginActivity
import com.example.smartpark.ui.register.RegisterViewModel
import retrofit2.http.Tag


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var profile: ProfileResponse? = null

        profileViewModel.me()

        profileViewModel.profileResponse.observe(viewLifecycleOwner) { response ->
            profile = response
            binding.tvName.text = profile!!.username
        }



        binding.btnLogout.setOnClickListener {
            val sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.remove("access_token")
            editor.apply()
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        binding.btnSubscriptions.setOnClickListener {

            var fragmentTransaction: FragmentTransaction =
                activity?.supportFragmentManager?.beginTransaction()!!
            fragmentTransaction.replace(com.example.smartpark.R.id.fl_profile, SubscriptionFragment() )
            fragmentTransaction.commit()
        }
    }
}