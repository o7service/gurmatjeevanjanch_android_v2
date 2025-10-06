package com.o7services.gurmatjeevanjaach.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.o7services.gurmatjeevanjaach.R
import com.o7services.gurmatjeevanjaach.activity.MainActivity
import com.o7services.gurmatjeevanjaach.adapter.AudioAdapter
import com.o7services.gurmatjeevanjaach.databinding.FragmentAudioBinding
import com.o7services.gurmatjeevanjaach.dataclass.AudioDataClass

class AudioFragment : Fragment() {
    lateinit var binding : FragmentAudioBinding
    lateinit var adapter : AudioAdapter
    lateinit var mainActivity: MainActivity
    lateinit var linearLayoutManager: LinearLayoutManager
    var item =  ArrayList<AudioDataClass>()
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
        binding = FragmentAudioBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = AudioAdapter(item)
        linearLayoutManager = LinearLayoutManager(mainActivity)
        binding.recyclerView.layoutManager = linearLayoutManager
        binding.recyclerView.adapter = adapter
        item.add(AudioDataClass(name = "ਭਾਈ ਅਜੀਤ ਸਿੰਘ - ਲੜੀਵਾਰ ਕਥਾ..." , description = "Bhai Amarjit Singh - Larivaar katha..."))
        item.add(AudioDataClass(name = "ਭਾਈ ਅਜੀਤ ਸਿੰਘ - ਲੜੀਵਾਰ ਕਥਾ..." , description = "Bhai Amarjit Singh - Larivaar katha..."))
        item.add(AudioDataClass(name = "ਭਾਈ ਅਜੀਤ ਸਿੰਘ - ਲੜੀਵਾਰ ਕਥਾ..." , description = "Bhai Amarjit Singh - Larivaar katha..."))
        adapter.notifyDataSetChanged()

    }
}