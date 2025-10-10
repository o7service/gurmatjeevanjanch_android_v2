package com.o7services.gurmatjeevanjaach.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.o7services.gurmatjeevanjaach.R
import com.o7services.gurmatjeevanjaach.activity.MainActivity
import com.o7services.gurmatjeevanjaach.adapter.WhatsappAdapter
import com.o7services.gurmatjeevanjaach.databinding.FragmentAllWhatsappListBinding
import com.o7services.gurmatjeevanjaach.dataclass.AudioDataClass

class AllWhatsappListFragment : Fragment() {
   lateinit var binding : FragmentAllWhatsappListBinding
   lateinit var adapter : WhatsappAdapter
   var item = ArrayList<AudioDataClass>()
    lateinit var mainActivity: MainActivity
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
        binding = FragmentAllWhatsappListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = WhatsappAdapter(item)
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