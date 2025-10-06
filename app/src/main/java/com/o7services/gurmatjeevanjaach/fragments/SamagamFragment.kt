package com.o7services.gurmatjeevanjaach.fragments

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.o7services.gurmatjeevanjaach.R
import com.o7services.gurmatjeevanjaach.activity.MainActivity
import com.o7services.gurmatjeevanjaach.adapter.SamagamAdapter
import com.o7services.gurmatjeevanjaach.databinding.FragmentSamagamBinding
import com.o7services.gurmatjeevanjaach.dataclass.AudioDataClass


class SamagamFragment : Fragment() {
   lateinit var binding : FragmentSamagamBinding
   var item = ArrayList<AudioDataClass>()
   lateinit var adapter : SamagamAdapter
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
        binding = FragmentSamagamBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = SamagamAdapter(item)
        linearLayoutManager = LinearLayoutManager(mainActivity)
        binding.recyclerView.layoutManager = linearLayoutManager
        binding.recyclerView.adapter = adapter
        item.add(AudioDataClass(name = "06/03/23"))
        item.add(AudioDataClass(name = "06/03/23"))
        item.add(AudioDataClass(name = "06/03/23"))
        adapter.notifyDataSetChanged()
    }

    fun setFormattedDateText(
        textView: TextView,
        day: String,
        month: String,
        year: String
    ) {
        val fullText = "$day\n$month\n$year"
        val dayEnd = day.length
        val monthStart = dayEnd + 1
        val monthEnd = monthStart + month.length + 1
        val yearStart = monthEnd
        val yearEnd = fullText.length

        val spannable = SpannableStringBuilder(fullText)
        spannable.setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            dayEnd,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            RelativeSizeSpan(1.4f),
            0,
            dayEnd,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannable.setSpan(
            RelativeSizeSpan(0.8f),
            yearStart,
            yearEnd,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        textView.text = spannable
    }

}