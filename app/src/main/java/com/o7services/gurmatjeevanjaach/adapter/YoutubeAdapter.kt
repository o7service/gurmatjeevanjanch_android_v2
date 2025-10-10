package com.o7services.gurmatjeevanjaach.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.o7services.gurmatjeevanjaach.consts.AppConst
import com.o7services.gurmatjeevanjaach.databinding.ItemYoutubeBinding
import com.o7services.gurmatjeevanjaach.dataclass.AllLinkResponse
import com.o7services.gurmatjeevanjaach.dataclass.SocialLinkSingleResponse
import com.o7services.gurmatjeevanjaach.dataclass.YoutubeResponse
import com.o7services.gurmatjeevanjaach.dataclass.YoutubeResponse.Data

class YoutubeAdapter(var item : ArrayList<AllLinkResponse.Data>,  private val categoryImage: String,val listner : itemClickListener) : RecyclerView.Adapter<YoutubeAdapter.ViewHolder>() {
    class ViewHolder (val binding : ItemYoutubeBinding) : RecyclerView.ViewHolder(binding.root){
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): YoutubeAdapter.ViewHolder {
        val binding = ItemYoutubeBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: YoutubeAdapter.ViewHolder, position: Int) {
        holder.binding.tvTitle.text = item[position].title
        val imageBaseUrl = AppConst.imageBaseUrl + categoryImage
        Glide.with(holder.itemView)
            .load(imageBaseUrl)
//            .placeholder()
            .into(holder.binding.ivAudio)
        holder.binding.tvSubTitle.text = item[position].link
        holder.itemView.setOnClickListener {
            listner.onItemClick(item[position].link.toString())
        }
    }

    override fun getItemCount(): Int {
        return item.size
    }
    interface itemClickListener{
        fun onItemClick(link : String)
    }
}