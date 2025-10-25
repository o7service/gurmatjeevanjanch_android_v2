package com.o7services.gurmatjeevanjaach.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import com.o7services.gurmatjeevanjaach.R
import com.o7services.gurmatjeevanjaach.consts.AppConst
import com.o7services.gurmatjeevanjaach.databinding.ItemAudioListBinding
import com.o7services.gurmatjeevanjaach.dataclass.AllSingerResponse
import com.o7services.gurmatjeevanjaach.dataclass.AudioDataClass
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import okio.IOException
import org.json.JSONObject


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
        holder.binding.tvTitle.text = item[position].name
//        holder.binding.tvSubTitle.text = item[position].
        val imageUrl = AppConst.imageBaseUrl + item[position].imageUrl
        Glide.with(holder.itemView)
            .load(imageUrl)
            .error(R.drawable.no_image)
            .placeholder(R.drawable.no_image)
            .into(holder.binding.ivAudio)
        val originalText = item[position].name.toString()
        if (position ==  selectedIndex) {
            holder.itemView.setBackgroundColor(Color.parseColor("#000000"))
            holder.binding.tvTitle.setTextColor(Color.parseColor("#FFFFFFFF"))
        }else{
            holder.itemView.setBackgroundColor(Color.parseColor("#eef3ff"))
            holder.binding.tvTitle.setTextColor(Color.parseColor("#000000"))
        }
//        translateTextWithLibre(
//            originalText,
//            onResult = { translatedText ->
//                holder.itemView.post {
//                    holder.binding.tvSubTitle.text = translatedText
//                }
//            },
//            onError = { exception ->
//                // fallback to original text
//                holder.itemView.post {
//                    holder.binding.tvSubTitle.text = originalText
//                }
//                Log.e("TRANSLATION_ERROR", "LibreTranslate failed: ${exception.message}")
//            }
//        )

        holder.itemView.setOnClickListener {
            listener.onItemClick(item[position].id.toString() , item[position].name.toString())
        }

    }

    interface onItemClickListener{
        fun onItemClick(id : String , title : String)
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

    fun translateText(
        sourceText: String,
        onResult: (String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.HINDI) // English to Punjabi
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
    fun translateTextWithLibre(
        sourceText: String,
        onResult: (String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val client = OkHttpClient()
        val json = JSONObject()
        json.put("q", sourceText)
        json.put("source", "en")
        json.put("target", "pa")
        json.put("format", "text")
        val body = RequestBody.create(
            "application/json; charset=utf-8".toMediaType(),
            json.toString()
        )
        val request = Request.Builder()
            .url("https://libretranslate.de/translate") // reliable endpoint
            .post(body)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onError(e)
            }
            override fun onResponse(call: Call, response: Response) {
                val bodyString = response.body?.string()
                if (bodyString.isNullOrEmpty()) {
                    onError(Exception("Empty response"))
                    return
                }
                // Check if response is JSON
                if (bodyString.trim().startsWith("{")) {
                    try {
                        val translated = JSONObject(bodyString).getString("translatedText")
                        onResult(translated)
                    } catch (e: Exception) {
                        onError(e)
                    }
                } else {
                    // Response is not JSON, probably HTML error page
                    onError(Exception("Invalid response: $bodyString"))
                }
            }
        })
    }
}