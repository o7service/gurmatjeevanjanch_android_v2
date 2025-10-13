package com.o7services.gurmatjeevanjaach.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.o7services.gurmatjeevanjaach.R

import com.o7services.gurmatjeevanjaach.dataclass.SliderItem
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class SliderAdapter(
    private var items: List<SliderItem>,
    private val lifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_YOUTUBE = 0
        private const val TYPE_IMAGE = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is SliderItem.YouTubeVideo -> TYPE_YOUTUBE
            is SliderItem.CustomImageWithId -> TYPE_IMAGE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_YOUTUBE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_youtube_live_pager, parent, false)
                YouTubeViewHolder(view)
            }
            TYPE_IMAGE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_slider_image, parent, false)
                ImageViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is SliderItem.YouTubeVideo -> (holder as YouTubeViewHolder).bind(item)
            is SliderItem.CustomImageWithId -> (holder as ImageViewHolder).bind(item)
        }
    }
    fun updateItems(newItems: List<SliderItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    inner class YouTubeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val youTubePlayerView: YouTubePlayerView = itemView.findViewById(R.id.youtubePlayerView)
        fun bind(item: SliderItem.YouTubeVideo) {
            // Remove previous lifecycle observers (important for RecyclerView)
            lifecycleOwner.lifecycle.removeObserver(youTubePlayerView)
            // Disable auto initialization to control it manually
            youTubePlayerView.enableAutomaticInitialization = false
            // Add current observer again
            lifecycleOwner.lifecycle.addObserver(youTubePlayerView)
            youTubePlayerView.initialize(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.cueVideo(item.videoId, 0f)
                }
            }, true)
        }
    }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)

        fun bind(item: SliderItem.CustomImageWithId) {
            Glide.with(itemView.context)
                .load(item.imageResId)
                .into(imageView)
            imageView.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.link))
                itemView.context.startActivity(intent)
            }
        }
    }
}
