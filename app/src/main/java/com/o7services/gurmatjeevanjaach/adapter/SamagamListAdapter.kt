package com.o7services.gurmatjeevanjaach.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.o7services.gurmatjeevanjaach.R
import com.o7services.gurmatjeevanjaach.consts.AppConst
import com.o7services.gurmatjeevanjaach.databinding.ItemSamagamProgramListBinding
import com.o7services.gurmatjeevanjaach.dataclass.AllProgramResponse
import com.o7services.gurmatjeevanjaach.dataclass.ProgramSingleDateResponse
import java.text.SimpleDateFormat
import java.util.Locale


class SamagamListAdapter(var item : ArrayList<ProgramSingleDateResponse.Data>, val listener : samagamListInterface) : RecyclerView.Adapter<SamagamListAdapter.ViewHolder>() {
    class ViewHolder (val binding : ItemSamagamProgramListBinding) : RecyclerView.ViewHolder(binding.root){
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SamagamListAdapter.ViewHolder {
        val binding = ItemSamagamProgramListBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SamagamListAdapter.ViewHolder, position: Int) {
        holder.binding.tvTitle.text = item[position].title
        holder.binding.tvSubTitle.text = item[position].address
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val startDateFormat = inputFormat.parse(item[position].startDate)
        val inputFormatDay = SimpleDateFormat("dd",  Locale.getDefault())
        val inputFormatMonth = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        var dayFormat = SimpleDateFormat("dd", Locale.getDefault())
        var monthFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        val endDateFormat = inputFormat.parse(item[position].endDate)
        val formattedStartDate = outputFormat.format(startDateFormat)
        val formattedEndDate = outputFormat.format(endDateFormat)
        val formatDate= inputFormatDay.format(startDateFormat)
        val formatMonth = inputFormatMonth.format(startDateFormat)
        holder.binding.tvDate.setText(formatDate)
        holder.binding.tvMonth.setText(formatMonth)
        holder.binding.tvSubTitleDate.setText(formattedStartDate + " to "+ formattedEndDate)
        val imageIcon = AppConst.imageBaseUrl + item[position].imageUrl
//        Glide.with(holder.itemView)
//            .load(imageIcon)
//            .error(R.drawable.no_image)
//            .placeholder(R.drawable.no_image)
//            .into(holder.binding.ivAudio)
//        holder.binding.tvDate.setText(startDateFormatDate.toString())
//        holder.binding.tvMonth.setText(startDateFormatMonth.toString())
        holder.itemView.setOnClickListener {
            listener.onSamagamDetail(imageLink = item[position].imageUrl.toString() , title = item[position].title.toString(), contactNo1 = item[position].contactNumber1?.toLong().toString()
            , contact2 = item[position].contactNumber2?.toLong().toString(), mapLink = item[position].mapLink.toString(), description = item[position].details.toString() , startDate = item[position].startDate.toString() , endDate = item[position].endDate.toString(), address = item[position].address.toString())
        }
        holder.binding.ivAudio.setOnClickListener {
            listener.onImageClick(item[position].imageUrl.toString())
        }
        holder.binding.ivCurrentMap.setOnClickListener {
            listener.onMapClick(item[position].mapLink.toString())
        }
        holder.binding.tvSubTitlePhone.text = "${item[position].contactNumber1?.toLong().toString()} , ${item[position].contactNumber2?.toLong().toString()}"
    }

    interface samagamListInterface{
        fun onMapClick(mapLink : String){
        }
        fun onImageClick(imageLink: String){}
        fun onSamagamDetail(imageLink : String , title: String , contactNo1 : String, contact2: String , address: String , mapLink: String ,
                            description: String , startDate : String , endDate: String)
    }

    override fun getItemCount(): Int {
        return item.size
    }
}
// how to format date from yyyy-mm-dd to dd-mm-yyyy