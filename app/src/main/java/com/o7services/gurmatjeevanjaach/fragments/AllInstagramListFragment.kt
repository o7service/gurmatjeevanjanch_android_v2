package com.o7services.gurmatjeevanjaach.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.o7services.gurmatjeevanjaach.R
import com.o7services.gurmatjeevanjaach.activity.MainActivity
import com.o7services.gurmatjeevanjaach.adapter.InstagramAdapter
import com.o7services.gurmatjeevanjaach.databinding.FragmentAllInstagramListBinding
import com.o7services.gurmatjeevanjaach.dataclass.AudioDataClass

class AllInstagramListFragment : Fragment() {
    lateinit var binding : FragmentAllInstagramListBinding
    lateinit var adapter : InstagramAdapter
    lateinit var mainActivity: MainActivity
    lateinit var linearLayoutManager: LinearLayoutManager
    var item = ArrayList<AudioDataClass>()
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
        binding = FragmentAllInstagramListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = InstagramAdapter(item)
        linearLayoutManager = LinearLayoutManager(mainActivity)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = linearLayoutManager
        item.add(AudioDataClass(name = "Sahej Path", description = "20/10, Guru Teg Bahadur Nagar, Jalandhar"))
        item.add(AudioDataClass(name = "Sahej Path", description = "20/10, Guru Teg Bahadur Nagar, Jalandhar"))
        item.add(AudioDataClass(name = "Sahej Path", description = "20/10, Guru Teg Bahadur Nagar, Jalandhar"))
        item.add(AudioDataClass(name = "Sahej Path", description = "20/10, Guru Teg Bahadur Nagar, Jalandhar"))
        adapter.notifyDataSetChanged()

    }

}