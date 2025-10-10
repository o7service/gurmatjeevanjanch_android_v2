package com.o7services.gurmatjeevanjaach.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.o7services.gurmatjeevanjaach.consts.AppConst
import com.o7services.gurmatjeevanjaach.databinding.ItemSamagamProgramListBinding
import com.o7services.gurmatjeevanjaach.dataclass.ProgramSingleDateResponse


class SamagamListAdapter(var item : ArrayList<ProgramSingleDateResponse.Data> , val listener : samagamListInterface) : RecyclerView.Adapter<SamagamListAdapter.ViewHolder>() {
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
        val imageIcon = AppConst.imageBaseUrl + item[position].imageUrl
        Glide.with(holder.itemView)
            .load(imageIcon)
//            .placeholder()
            .into(holder.binding.ivAudio)
        holder.itemView.setOnClickListener {
           listener.onMapClick(item[position].mapLink.toString())
        }
    }

    interface samagamListInterface{
        fun onMapClick(mapLink : String){
        }
    }

    override fun getItemCount(): Int {
        return item.size
    }
}