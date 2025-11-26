package com.o7services.gurmatjeevanjaach.adapter

import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.o7services.gurmatjeevanjaach.R
import com.o7services.gurmatjeevanjaach.adapter.AudioAdapter.ViewHolder
import com.o7services.gurmatjeevanjaach.databinding.ItemSamagamListBinding
import com.o7services.gurmatjeevanjaach.databinding.ItemPlayAudioListBinding
import com.o7services.gurmatjeevanjaach.databinding.ItemSamagamBinding
import com.o7services.gurmatjeevanjaach.dataclass.AllProgramResponse
import com.o7services.gurmatjeevanjaach.dataclass.AudioDataClass
import com.o7services.gurmatjeevanjaach.fragments.PlayAudioFragment

class SamagamAdapter(val item : ArrayList<Map<String , List<AllProgramResponse.SamagamItem>>>, val listener: onItemSamagamListener) : RecyclerView.Adapter<SamagamAdapter.ViewHolder>(){
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
        holder.binding.tvTitle.text = item[position].keys.toString()
        val dateString = item[position].keys.first()
        Log.d("Response", dateString)
        val parts = dateString?.split("-")
        Log.d("Response", parts.toString())
        if (parts?.size == 3) {
            val year = parts[0]
            val month = getMonthName(holder.itemView.context , parts[1])
            val day = parts[2]
            setFormattedDateText(holder.binding.tvTitle, day, month, year)
        }
        holder.itemView.setOnClickListener {
            Log.d("Values", item[position].toString())
            listener.onSamagamClick(item[position].keys.first(), item[position].values.first())
        }
    }
    private fun getMonthName(context: android.content.Context, monthNumber: String): String {
        val monthInt = monthNumber.toIntOrNull()
        return if (monthInt != null && monthInt in 1..12) {
            try {
                val monthNamesArray = context.resources.getStringArray(R.array.month_names_full)
                monthNamesArray[monthInt - 1]
            } catch (e: Exception) {
                ""
            }
        } else {
            ""
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
        fun onSamagamClick(date : String, values : List<AllProgramResponse.SamagamItem>)
    }

}