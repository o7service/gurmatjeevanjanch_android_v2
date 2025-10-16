package com.o7services.gurmatjeevanjaach.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.o7services.gurmatjeevanjaach.R
import com.o7services.gurmatjeevanjaach.activity.MainActivity
import com.o7services.gurmatjeevanjaach.databinding.FragmentMoreBinding


class MoreFragment : Fragment() {
    lateinit var mainActivity: MainActivity
    lateinit var binding : FragmentMoreBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity as MainActivity
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentMoreBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity.hideNoData()
        binding.btnRequestSangam.setOnClickListener {
            findNavController().navigate(R.id.requestSamagamFragment)
        }
        binding.tvNumber.setOnClickListener {
            var intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:+1-77888-31925")
            startActivity(intent)
        }
        binding.tvNumber1.setOnClickListener {
            var intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:8728006100")
            startActivity(intent)
        }
        binding.tvNumber2.setOnClickListener {
            var intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:+91-78888-75285")
            startActivity(intent)
        }
        binding.tvChangeLanguage.setOnClickListener {
                val prefs = requireContext().getSharedPreferences("settings", Context.MODE_PRIVATE)
                val currentLang = prefs.getString("language", "en")
                val newLang = if (currentLang == "en") "pa" else "en"

                prefs.edit().putString("language", newLang).apply()

                // Restart the activity to apply language change
                val intent = requireActivity().intent
                requireActivity().finish()
                startActivity(intent)

        }
    }

}