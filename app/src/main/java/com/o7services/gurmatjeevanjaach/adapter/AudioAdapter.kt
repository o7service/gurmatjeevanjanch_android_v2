package com.o7services.gurmatjeevanjaach.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.R
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import com.o7services.gurmatjeevanjaach.consts.AppConst
import com.o7services.gurmatjeevanjaach.databinding.ItemAudioListBinding
import com.o7services.gurmatjeevanjaach.dataclass.AllSingerResponse
import com.o7services.gurmatjeevanjaach.dataclass.AudioDataClass


class AudioAdapter(var item : ArrayList<AllSingerResponse.Data>, var listener: onItemClickListener) : RecyclerView.Adapter<AudioAdapter.ViewHolder>() {
    val translator: Translator? = null
    class ViewHolder (val binding : ItemAudioListBinding) : RecyclerView.ViewHolder(binding.root){
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AudioAdapter.ViewHolder {
        val binding = ItemAudioListBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AudioAdapter.ViewHolder, position: Int) {
        holder.binding.tvSubTitle.text = item[position].name
//        holder.binding.tvSubTitle.text = item[position].
        val imageUrl = AppConst.imageBaseUrl + item[position].imageUrl
        Glide.with(holder.itemView)
            .load(imageUrl)
//            .placeholder()
            .into(holder.binding.ivAudio)
        holder.itemView.setOnClickListener {
            listener.onItemClick(item[position].id.toString())
        }
    }

    interface onItemClickListener{
        fun onItemClick(id : String)
    }

    fun translateText(
        sourceText: String,
        onResult: (String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage("pa") // English to Punjabi
            .build()

        val translator = Translation.getClient(options)

        // Using conditions that allow download over any network for robustness
        val conditions = DownloadConditions.Builder().build()

        translator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                translator.translate(sourceText)
                    .addOnSuccessListener { translatedText ->
                        onResult(translatedText)
                    }
                    .addOnFailureListener { exception ->
                        // Fails if translation model is still not ready/failed to load
                        Log.e("TRANSLATION_ERROR", "Translation failed: ", exception)
                        onError(exception)
                    }
            }
            .addOnFailureListener { exception ->
                // Fails if the model download failed (e.g., no internet, or storage issue)
                Log.e("MODEL_DOWNLOAD_ERROR", "Model download failed: ", exception)
                onError(exception)
            }
    }

    override fun getItemCount(): Int {
       return item.size
    }
}