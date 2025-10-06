package com.o7services.gurmatjeevanjaach.adapter

import android.R
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.o7services.gurmatjeevanjaach.adapter.AudioAdapter.ViewHolder
import com.o7services.gurmatjeevanjaach.databinding.ItemAudioListBinding
import com.o7services.gurmatjeevanjaach.databinding.ItemPlayAudioListBinding
import com.o7services.gurmatjeevanjaach.dataclass.AudioDataClass
import com.o7services.gurmatjeevanjaach.fragments.PlayAudioFragment

class PlayAudioAdapter( val item : ArrayList<AudioDataClass>) : RecyclerView.Adapter<PlayAudioAdapter.ViewHolder>(){
    class ViewHolder (val binding : ItemAudioListBinding) : RecyclerView.ViewHolder(binding.root){
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlayAudioAdapter.ViewHolder {
        val binding = ItemAudioListBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlayAudioAdapter.ViewHolder, position: Int) {
        holder.binding.tvTitle.text = item[position].name
        holder.binding.tvSubTitle.text = item[position].description
        Glide.with(holder.itemView)
            .load(item[position].image)
//            .placeholder()
            .into(holder.binding.ivAudio)
        holder.itemView.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return item.size
    }

}