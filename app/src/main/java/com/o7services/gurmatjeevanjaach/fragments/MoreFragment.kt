package com.o7services.gurmatjeevanjaach.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.o7services.gurmatjeevanjaach.R
import com.o7services.gurmatjeevanjaach.databinding.FragmentMoreBinding


class MoreFragment : Fragment() {
    lateinit var binding : FragmentMoreBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        binding.btnRequestSangam.setOnClickListener {
            findNavController().navigate(R.id.requestSamagamFragment)
        }
    }

}