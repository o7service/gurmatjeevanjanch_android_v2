package com.o7services.gurmatjeevanjaach.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.o7services.gurmatjeevanjaach.databinding.ItemTelegramBinding
import com.o7services.gurmatjeevanjaach.dataclass.AudioDataClass

class TelegramAdapter(var item : ArrayList<AudioDataClass>) : RecyclerView.Adapter<TelegramAdapter.ViewHolder>() {
    class ViewHolder (val binding : ItemTelegramBinding) : RecyclerView.ViewHolder(binding.root){
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TelegramAdapter.ViewHolder {
        val binding = ItemTelegramBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TelegramAdapter.ViewHolder, position: Int) {
        holder.binding.tvTitle.text = item[position].name
        holder.binding.tvSubTitle.text = item[position].description
//        Glide.with(holder.itemView)
//            .load(item[position].image)
////            .placeholder()
//            .into(holder.binding.ivAudio)
        holder.itemView.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return item.size
    }
}