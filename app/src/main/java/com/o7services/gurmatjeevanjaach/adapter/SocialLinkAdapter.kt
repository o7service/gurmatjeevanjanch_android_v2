package com.o7services.gurmatjeevanjaach.adapter


import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.o7services.gurmatjeevanjaach.R
import com.o7services.gurmatjeevanjaach.consts.AppConst
import com.o7services.gurmatjeevanjaach.databinding.ItemConnectCardBinding
import com.o7services.gurmatjeevanjaach.databinding.ItemInstagramBinding
import com.o7services.gurmatjeevanjaach.dataclass.SocialLinkResponse
class SocialLinkAdapter(var item : ArrayList<SocialLinkResponse.Data>, val listener: itemClickListener) : RecyclerView.Adapter<SocialLinkAdapter.ViewHolder>() {
    class ViewHolder (val binding : ItemConnectCardBinding) : RecyclerView.ViewHolder(binding.root){
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SocialLinkAdapter.ViewHolder {
        val binding = ItemConnectCardBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SocialLinkAdapter.ViewHolder, position: Int) {
        holder.binding.tvTitle.text = item[position].title
//        holder.binding.tvSubTitle.text = item[position].description
        var imageIcon = AppConst.imageBaseUrl + item[position].icon
        Glide.with(holder.itemView)
            .load(imageIcon)
            .error(R.drawable.no_image)
            .placeholder(R.drawable.no_image)
//            .placeholder()
            .into(holder.binding.ivMusic)
        holder.itemView.setOnClickListener {
            Log.d("Response", item[position].id.toString())
            var id = item[position].id
            listener.onSocialClick(id.toString(), item[position].icon.toString(), item[position].title.toString())
        }
    }

    override fun getItemCount(): Int {
        return item.size
    }

    interface itemClickListener{
        fun onSocialClick(id : String, categoryImage : String , title : String)
    }
}