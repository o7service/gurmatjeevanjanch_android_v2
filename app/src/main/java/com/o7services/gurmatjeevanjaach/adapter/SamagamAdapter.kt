package com.o7services.gurmatjeevanjaach.adapter

import android.R
import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.o7services.gurmatjeevanjaach.adapter.AudioAdapter.ViewHolder
import com.o7services.gurmatjeevanjaach.databinding.ItemSamagamListBinding
import com.o7services.gurmatjeevanjaach.databinding.ItemPlayAudioListBinding
import com.o7services.gurmatjeevanjaach.databinding.ItemSamagamBinding
import com.o7services.gurmatjeevanjaach.dataclass.AllProgramResponse
import com.o7services.gurmatjeevanjaach.dataclass.AudioDataClass
import com.o7services.gurmatjeevanjaach.fragments.PlayAudioFragment

class SamagamAdapter(val item : ArrayList<AllProgramResponse.Data>, val listener: onItemSamagamListener) : RecyclerView.Adapter<SamagamAdapter.ViewHolder>(){
    class ViewHolder (val binding : ItemSamagamBinding) : RecyclerView.ViewHolder(binding.root){
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SamagamAdapter.ViewHolder {
        val binding = ItemSamagamBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SamagamAdapter.ViewHolder, position: Int) {
        holder.binding.tvTitle.text = item[position].startDate
        val dateString = item[position].startDate // e.g., "06/03/23"

        val parts = dateString?.split("-")
        if (parts?.size == 3) {
            val year = parts[0]
            val month = getMonthName(parts[1])
            val day = parts[2] // Convert "23" to "2023"
            setFormattedDateText(holder.binding.tvTitle, day, month, year)
        }
        holder.itemView.setOnClickListener {
            listener.onSamagamClick(item[position].startDate.toString())
        }
    }
    private fun getMonthName(monthNumber: String): String {
        return when (monthNumber.toIntOrNull()) {
            1 -> "January"
            2 -> "February"
            3 -> "March"
            4 -> "April"
            5 -> "May"
            6 -> "June"
            7 -> "July"
            8 -> "August"
            9 -> "September"
            10 -> "October"
            11 -> "November"
            12 -> "December"
            else -> ""
        }
    }

    private fun setFormattedDateText(textView: TextView, day: String, month: String, year: String) {
        val fullText = "$day\n$month\n$year"
        val dayEnd = day.length
        val monthStart = dayEnd + 1
        val monthEnd = monthStart + month.length + 1
        val yearStart = monthEnd
        val yearEnd = fullText.length
        val spannable = SpannableStringBuilder(fullText)
        spannable.setSpan(StyleSpan(Typeface.BOLD), 0, dayEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(RelativeSizeSpan(1.4f), 0, dayEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(RelativeSizeSpan(0.8f), monthStart, monthEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(RelativeSizeSpan(0.8f), yearStart, yearEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = spannable
    }

    override fun getItemCount(): Int {
        return item.size
    }

    interface onItemSamagamListener{
        fun onSamagamClick(date : String)
    }

}