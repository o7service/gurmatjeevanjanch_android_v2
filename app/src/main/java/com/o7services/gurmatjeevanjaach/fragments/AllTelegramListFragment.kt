package com.o7services.gurmatjeevanjaach.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.o7services.gurmatjeevanjaach.R
import com.o7services.gurmatjeevanjaach.activity.MainActivity
import com.o7services.gurmatjeevanjaach.adapter.TelegramAdapter
import com.o7services.gurmatjeevanjaach.databinding.FragmentAllTelegramListBinding
import com.o7services.gurmatjeevanjaach.dataclass.AudioDataClass

class AllTelegramListFragment : Fragment() {
    lateinit var binding : FragmentAllTelegramListBinding
    lateinit var adapter: TelegramAdapter
    lateinit var mainActivity: MainActivity
    var item = ArrayList<AudioDataClass>()
    lateinit var linearLayoutManager: LinearLayoutManager
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
        binding = FragmentAllTelegramListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = TelegramAdapter(item)
        linearLayoutManager = LinearLayoutManager(mainActivity)
        binding.recyclerView.layoutManager = linearLayoutManager
        binding.recyclerView.adapter = adapter
        item.add(AudioDataClass(name = "Sahej Path", description = "20/10, Guru Teg Bahadur Nagar, Jalandhar"))
        item.add(AudioDataClass(name = "Sahej Path", description = "20/10, Guru Teg Bahadur Nagar, Jalandhar"))
        item.add(AudioDataClass(name = "Sahej Path", description = "20/10, Guru Teg Bahadur Nagar, Jalandhar"))
        item.add(AudioDataClass(name = "Sahej Path", description = "20/10, Guru Teg Bahadur Nagar, Jalandhar"))
        adapter.notifyDataSetChanged()
    }


}