package com.o7services.gurmatjeevanjaach.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.o7services.gurmatjeevanjaach.R
import com.o7services.gurmatjeevanjaach.activity.MainActivity
import com.o7services.gurmatjeevanjaach.adapter.FacebookAdapter
import com.o7services.gurmatjeevanjaach.databinding.FragmentAllFacebookListBinding
import com.o7services.gurmatjeevanjaach.dataclass.AudioDataClass

class AllFacebookListFragment : Fragment() {
    lateinit var binding : FragmentAllFacebookListBinding
    lateinit var adapter : FacebookAdapter
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
       binding = FragmentAllFacebookListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = FacebookAdapter(item)
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