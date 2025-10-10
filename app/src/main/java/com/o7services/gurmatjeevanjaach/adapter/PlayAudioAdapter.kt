package com.o7services.gurmatjeevanjaach.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.R
import com.o7services.gurmatjeevanjaach.adapter.AudioAdapter.ViewHolder
import com.o7services.gurmatjeevanjaach.consts.AppConst
import com.o7services.gurmatjeevanjaach.databinding.ItemPlayAudioListBinding
import com.o7services.gurmatjeevanjaach.dataclass.SingleSingerAudioResponse
import com.o7services.gurmatjeevanjaach.dataclass.SingleSingerResponse
import com.o7services.gurmatjeevanjaach.fragments.PlayAudioFragment

class PlayAudioAdapter( val item : ArrayList<SingleSingerAudioResponse.Data>, var categoryImage : String, var listener : playAudioInterface) : RecyclerView.Adapter<PlayAudioAdapter.ViewHolder>(){
    class ViewHolder (val binding : ItemPlayAudioListBinding) : RecyclerView.ViewHolder(binding.root){
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlayAudioAdapter.ViewHolder {
        val binding = ItemPlayAudioListBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlayAudioAdapter.ViewHolder, position: Int) {
        holder.binding.tvTitle.text = item[position].title
        //holder.binding.tvSubTitle.text = item[position].description
        var imageUrl = AppConst.imageBaseUrl + categoryImage
        Glide.with(holder.itemView)
            .load(imageUrl)
            .into(holder.binding.ivAudio)
        holder.itemView.setOnClickListener {
            listener.onAudioClick(item[position].title.toString(),item[position].audioLink.toString())
        }
        if (position == selectedIndex){
            holder.itemView.setBackgroundColor(Color.parseColor("#000000"))
            holder.binding.tvTitle.setTextColor(Color.parseColor("#FFFFFFFF"))
            holder.binding.ivPlay.visibility = View.VISIBLE
        }else{
            holder.itemView.setBackgroundColor(Color.parseColor("#eef3ff"))
            holder.binding.tvTitle.setTextColor(Color.parseColor("#000000"))
            holder.binding.ivPlay.visibility = View.GONE
        }


    }


    override fun getItemCount(): Int {
        return item.size
    }
    private var selectedIndex = -1

    fun updateSelectedIndex(index: Int) {
        val oldIndex = selectedIndex
        selectedIndex = index
        notifyItemChanged(oldIndex)
        notifyItemChanged(selectedIndex)
    }

    interface playAudioInterface{
        fun onAudioClick(title : String , audioLink : String)
    }
}

