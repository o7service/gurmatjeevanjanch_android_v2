package com.o7services.gurmatjeevanjaach.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.o7services.gurmatjeevanjaach.R
import com.o7services.gurmatjeevanjaach.adapter.AudioAdapter.ViewHolder
import com.o7services.gurmatjeevanjaach.consts.AppConst
import com.o7services.gurmatjeevanjaach.databinding.ItemPlayAudioListBinding
import com.o7services.gurmatjeevanjaach.dataclass.SingleSingerAudioResponse
import com.o7services.gurmatjeevanjaach.dataclass.SingleSingerResponse
import com.o7services.gurmatjeevanjaach.fragments.PlayAudioFragment
import com.o7services.gurmatjeevanjaach.retrofit.MediaManager
class PlayAudioAdapter( val item : ArrayList<SingleSingerAudioResponse.Data>, var categoryImage : String, var listener : playAudioInterface) : RecyclerView.Adapter<PlayAudioAdapter.ViewHolder>(){
    private var isPlaying: Boolean = false
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
        if (position == 0){
            holder.binding.view1.visibility = View.VISIBLE
        }else{
            holder.binding.view1.visibility = View.GONE
        }
        holder.binding.tvTitle.text = item[position].title
        if (item[position].id.toString() == currentAudioId && MediaManager.isAudioPlaying()) {
            Glide.with(holder.itemView)
                .load(com.o7services.gurmatjeevanjaach.R.drawable.icon_pause2_final)
                .into(holder.binding.ivPlay)
        } else {
            Glide.with(holder.itemView)
                .load(com.o7services.gurmatjeevanjaach.R.drawable.icon_play2_final)
                .into(holder.binding.ivPlay)
        }
        //holder.binding.tvSubTitle.text = item[position].description
        var imageUrl = AppConst.imageBaseUrl + categoryImage
        Glide.with(holder.itemView)
            .load(imageUrl)
            .error(R.drawable.no_image)
            .placeholder(R.drawable.no_image)
            .into(holder.binding.ivAudio)
        holder.itemView.setOnClickListener {
            listener.onAudioClick(item[position].title.toString(),item[position].audioLink.toString(), item[position].id.toString(),item[position].singerId.toString())
        }
        if (position == selectedIndex){
            holder.itemView.setBackgroundColor(Color.parseColor("#000000"))
            holder.binding.tvTitle.setTextColor(Color.parseColor("#FFFFFFFF"))
            var imageUrl = AppConst.imageBaseUrl + categoryImage
            Glide.with(holder.itemView)
                .load(imageUrl)
                .error(R.drawable.no_image)
                .placeholder(R.drawable.no_image)
                .into(holder.binding.ivAudio)
            if (MediaManager.isPlaying){
                    Glide.with(holder.itemView)
                        .load(com.o7services.gurmatjeevanjaach.R.drawable.icon_pause2_final)
                        .into(holder.binding.ivPlay)
                }else{
                    Glide.with(holder.itemView)
                        .load(com.o7services.gurmatjeevanjaach.R.drawable.icon_play2_final)
                        .into(holder.binding.ivPlay)
                }
            holder.binding.ivPlay.visibility = View.VISIBLE
        }else{
            var imageUrl = AppConst.imageBaseUrl + categoryImage
            Glide.with(holder.itemView)
                .load(imageUrl)
                .error(R.drawable.no_image)
                .placeholder(R.drawable.no_image)
                .into(holder.binding.ivAudio)
            holder.itemView.setBackgroundColor(Color.parseColor("#eef3ff"))
            holder.binding.tvTitle.setTextColor(Color.parseColor("#000000"))
            holder.binding.ivPlay.visibility = View.GONE
        }
        val isCurrent = item[position].id.toString() == currentAudioId
        if (isCurrent) {
            holder.itemView.setBackgroundColor(Color.parseColor("#000000"))
            holder.binding.tvTitle.setTextColor(Color.parseColor("#FFFFFFFF"))
            holder.binding.ivPlay.visibility = View.VISIBLE
            if(MediaManager.isPlaying){
                Glide.with(holder.itemView)
                    .load(com.o7services.gurmatjeevanjaach.R.drawable.icon_pause2_final)
                    .into(holder.binding.ivPlay)
            }else{
                Glide.with(holder.itemView)
                    .load(com.o7services.gurmatjeevanjaach.R.drawable.icon_play2_final)
                    .into(holder.binding.ivPlay)
            }
            holder.binding.ivPlay.setOnClickListener {
                listener.togglePlayPause()
                if(MediaManager.isPlaying){
                    Glide.with(holder.itemView)
                        .load(com.o7services.gurmatjeevanjaach.R.drawable.icon_pause2_final)
                        .into(holder.binding.ivPlay)
                }else{
                    Glide.with(holder.itemView)
                        .load(com.o7services.gurmatjeevanjaach.R.drawable.icon_play2_final)
                        .into(holder.binding.ivPlay)
                }
            }

        } else {
            holder.binding.ivPlay.visibility = View.GONE
            holder.itemView.setBackgroundColor(Color.parseColor("#eef3ff"))
            holder.binding.tvTitle.setTextColor(Color.parseColor("#000000"))
        }
    }

    override fun getItemCount(): Int {
        return item.size
    }

    private var selectedIndex = -1
    private var currentAudioId: String? = null

    fun updateSelectedIndex(index: Int) {
        val oldIndex = selectedIndex
        selectedIndex = index
        notifyItemChanged(oldIndex)
        notifyItemChanged(selectedIndex)
    }

    fun updateCurrentAudioId(audioId: String?) {
        val oldIndex = selectedIndex
        currentAudioId = audioId
        selectedIndex = item.indexOfFirst { it.id.toString() == audioId }
        if (oldIndex != -1) notifyItemChanged(oldIndex)
        if (selectedIndex != -1) notifyItemChanged(selectedIndex)
    }

//    fun updateCurrentAudioId(audioId: String?) {
//        currentAudioId = audioId
//        notifyDataSetChanged()
//    }


    interface playAudioInterface{
        fun onAudioClick(title : String , audioLink : String , audioId : String,singerId : String )
        fun togglePlayPause()
    }
}

