package com.o7services.gurmatjeevanjaach.fragments
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.o7services.gurmatjeevanjaach.R
import com.o7services.gurmatjeevanjaach.activity.MainActivity
import com.o7services.gurmatjeevanjaach.adapter.PlayAudioAdapter
import com.o7services.gurmatjeevanjaach.consts.AppConst
import com.o7services.gurmatjeevanjaach.databinding.FragmentPlayAudioBinding
import com.o7services.gurmatjeevanjaach.dataclass.AudioDataClass
import com.o7services.gurmatjeevanjaach.dataclass.SingleSingerAudioRequest
import com.o7services.gurmatjeevanjaach.dataclass.SingleSingerAudioResponse
import com.o7services.gurmatjeevanjaach.dataclass.SingleSingerRequest
import com.o7services.gurmatjeevanjaach.dataclass.SingleSingerResponse
import com.o7services.gurmatjeevanjaach.retrofit.MediaManager
import com.o7services.gurmatjeevanjaach.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Response
import java.util.concurrent.TimeUnit

class PlayAudioFragment : Fragment(), PlayAudioAdapter.playAudioInterface{
    lateinit var binding : FragmentPlayAudioBinding
    lateinit var adapter : PlayAudioAdapter
    lateinit var mainActivity: MainActivity
    lateinit var tvTitle : TextView
    var currentIndex = 0
    var id = ""
    var isCurrentAudioInList = false
    lateinit var linearLayoutManager: LinearLayoutManager
    var item = ArrayList<SingleSingerAudioResponse.Data>()
    var audioImage = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity as MainActivity
        arguments?.let {
            id = it.getString("id", "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlayAudioBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        Log.d("Response", "Current Title: ${MediaManager.currentTitle}")
        Log.d("Response", "Current Audio ID: ${MediaManager.currentAudioId}")
        Log.d("Response", id)
        if (id == MediaManager.currentSingerId) {
            binding.tvTitle.text = MediaManager.currentTitle ?: ""
            val duration = MediaManager.getDuration()
            val position = MediaManager.getCurrentPosition()
            binding.seekBar.max = duration
            binding.seekBar.progress = position
            binding.tvEndTime.text = formatTime(duration)
            binding.tvStartTime.text = formatTime(position)
            startSeekBarUpdates()
            updatePlayPauseIcon()
        } else {
            binding.tvTitle.text = ""
            binding.seekBar.progress = 0
            binding.seekBar.max = 0
            binding.tvStartTime.text = "00:00"
            binding.tvEndTime.text = "00:00"
            Glide.with(mainActivity)
                .load(R.drawable.ic_play_audio)
                .into(binding.ivPlay)
        }
    }

    private fun updateCurrentIndexByAudioId(audioId: String , singerId :String) {

        val index = item.indexOfFirst {it.id.toString() == audioId && it.singerId.toString() == singerId }
        if (index != -1) {
            currentIndex = index
        } else {
            currentIndex = 0
        }
        Log.d("Index Response", index.toString())
        adapter.updateSelectedIndex(currentIndex)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        if (MediaManager.isAudioPlaying()) {
//            Glide.with(mainActivity)
//                .load(R.drawable.ic_audio_pause)
//                .into(binding.ivPlay)
//        } else {
//            Glide.with(mainActivity)
//                .load(R.drawable.ic_play_audio)
//                .into(binding.ivPlay)
//        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            mainActivity.showProgress()
            getAllAudio()
            uiSet()
        }
        getAllAudio()
        uiSet()
    }
    fun uiSet(){
        startSeekBarUpdates()
        binding.ivPrevious.setOnClickListener {
            if (currentIndex > 0) {
                currentIndex--
                playCurrentAudio(item[currentIndex].title.toString(),item[currentIndex].audioLink.toString(), item[currentIndex].id.toString(),item[currentIndex].singerId.toString())
            } else {
                playCurrentAudio(item[currentIndex].title.toString(),item[currentIndex].audioLink.toString(), item[currentIndex].id.toString(),item[currentIndex].singerId.toString())
            }
            adapter.notifyDataSetChanged()
        }
        binding.ivNext.setOnClickListener {
            if (currentIndex < item.size - 1) {
                currentIndex++
                playCurrentAudio(item[currentIndex].title.toString(),item[currentIndex].audioLink.toString() ,item[currentIndex].id.toString(),item[currentIndex].singerId.toString())
            } else {
                playCurrentAudio(item[currentIndex].title.toString(),item[currentIndex].audioLink.toString() , item[currentIndex].id.toString(),item[currentIndex].singerId.toString())

            }
            adapter.notifyDataSetChanged()
        }
        binding.ivPlay.setOnClickListener {
            MediaManager.togglePlayPause()
            adapter.notifyDataSetChanged()
            if (MediaManager.isAudioPlaying()) {
                Glide.with(mainActivity)
                    .load(R.drawable.ic_audio_pause)
                    .into(binding.ivPlay)
            } else {
                Glide.with(mainActivity)
                    .load(R.drawable.ic_play_audio)
                    .into(binding.ivPlay)
            }
        }
    }
    private fun updatePlayPauseIcon() {
        if (MediaManager.isAudioPlaying()) {
            Glide.with(mainActivity)
                .load(R.drawable.ic_audio_pause)
                .into(binding.ivPlay)
        } else {
            Glide.with(mainActivity)
                .load(R.drawable.ic_play_audio)
                .into(binding.ivPlay)
        }
    }
    fun getAllAudio(){
        mainActivity.showProgress()
        RetrofitClient.instance.getSingleSinger(SingleSingerRequest(id = id)).
        enqueue(object : retrofit2.Callback<SingleSingerResponse>{
            override fun onResponse(
                call: Call<SingleSingerResponse?>,
                response: Response<SingleSingerResponse?>
            ) {
                if (response.body()?.status == 200){
                    if (response.body()?.success == true){

                        val data = response.body()?.data
                        if (data != null){
                            mainActivity.hideProgress()
                            audioImage = data.imageUrl.toString()
                            val imageBaseUrl = AppConst.imageBaseUrl + data.imageUrl
                            Glide.with(mainActivity)
                                .load(imageBaseUrl)
//            .placeholder()
                                .into(binding.ivAudio)
//                            binding.tvTitle.text = data.name
//                            item.clear()
//                            item.add(data)
//                            adapter.notifyDataSetChanged()
                        }else{
                            mainActivity.hideProgress()
                            mainActivity.showNoData()
                            Log.d("Response", response.body()?.message.toString())
                        }
                    }else{
                        mainActivity.hideProgress()
                        mainActivity.showNoData()
                        Log.d("Response", response.body()?.message.toString())
                    }
                }
            }

            override fun onFailure(
                call: Call<SingleSingerResponse?>,
                t: Throwable
            ) {
                mainActivity.hideProgress()
                mainActivity.showNoData()
                Log.d("Response", t.message.toString())
            }
        })
        loadAudios(id)
    }
    private fun playCurrentAudio(title: String, audioLink: String, audioId: String, singerId: String) {
        Glide.with(mainActivity)
            .load(R.drawable.ic_play_audio) // optional loading icon
            .into(binding.ivPlay)

        MediaManager.onPrepared = {
            val duration = MediaManager.getDuration()
            binding.seekBar.max = duration
            binding.tvEndTime.text = formatTime(duration)

            // Update play icon after audio starts
            updatePlayPauseIcon()
        }
        MediaManager.onCompletion = {
            if (currentIndex < item.size - 1) {
                currentIndex++
                playCurrentAudio(
                    item[currentIndex].title.toString(),
                    item[currentIndex].audioLink.toString(),
                    item[currentIndex].id.toString(),
                    item[currentIndex].singerId.toString()
                )
            } else {
                Glide.with(mainActivity)
                    .load(R.drawable.ic_replay)
                    .into(binding.ivPlay)
                binding.ivPlay.setOnClickListener {
                    playCurrentAudio(
                        item[currentIndex].title.toString(),
                        item[currentIndex].audioLink.toString(),
                        item[currentIndex].id.toString(),
                        item[currentIndex].singerId.toString()
                    )
                }
            }
            adapter.updateSelectedIndex(currentIndex)
            adapter.updateCurrentAudioId(item[currentIndex].id.toString())
            adapter.notifyDataSetChanged()
            updatePlayPauseIcon() // update icon on completion
        }

        MediaManager.onError = { errorMsg ->
            Log.e("AudioError", errorMsg)
            Glide.with(mainActivity)
                .load(R.drawable.ic_play_audio)
                .into(binding.ivPlay)
            updatePlayPauseIcon()
        }
        MediaManager.playAudioFromUrl(title, audioLink, audioId, singerId)

        // Set title text correctly
        binding.tvTitle.text = title
        adapter.updateSelectedIndex(currentIndex)
        adapter.updateCurrentAudioId(audioId)
        adapter.notifyDataSetChanged()
        updatePlayPauseIcon() // Update icon immediately after play command
    }
    private fun startSeekBarUpdates() {
        val duration = MediaManager.getDuration()
        binding.seekBar.max = duration
        binding.tvEndTime.text = formatTime(duration)

        val handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {
                if (id == MediaManager.currentSingerId) {
                    val currentPosition = MediaManager.getCurrentPosition()
                    val duration = MediaManager.getDuration()
                    binding.seekBar.max = duration
                    binding.seekBar.progress = currentPosition
                    binding.tvStartTime.text = formatTime(currentPosition)
                    binding.tvEndTime.text = formatTime(duration)
                }
                handler.postDelayed(this, 1000)
            }
        })
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    binding.tvStartTime.text = formatTime(progress)
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                MediaManager.seekTo(seekBar?.progress ?: 0)
            }
        })
    }

    private fun formatTime(ms: Int): String {
        val minutes = (ms / 1000) / 60
        val seconds = (ms / 1000) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    fun formatDuration(type : Long):String{
        var minutes = TimeUnit.MINUTES.convert(type,TimeUnit.MILLISECONDS)
        var seconds = TimeUnit.SECONDS.convert(type,TimeUnit.MILLISECONDS)-minutes*TimeUnit.SECONDS.convert(1,TimeUnit.MINUTES)
        return String.format("$minutes:$seconds")
    }
    fun loadAudios(id : String){
        RetrofitClient.instance.getSingleSingerAudio(SingleSingerAudioRequest(singerId = id)).
        enqueue(object : retrofit2.Callback<SingleSingerAudioResponse>{
            override fun onResponse(
                call: Call<SingleSingerAudioResponse?>,
                response: Response<SingleSingerAudioResponse?>
            ) {
                binding.swipeRefreshLayout.isRefreshing = false
                if (response.body()?.status == 200){
                    if (response.body()?.success == true){
                        val data = response.body()?.data
                        if (data != null){
                            adapter = PlayAudioAdapter(item, categoryImage = audioImage , this@PlayAudioFragment)
                            linearLayoutManager = LinearLayoutManager(mainActivity)
                            binding.recyclerView.adapter = adapter
                            binding.recyclerView.layoutManager = linearLayoutManager
                            item.clear()
                            item.addAll(data)
                            adapter.updateCurrentAudioId(MediaManager.currentAudioId ?: "")
                            adapter.notifyDataSetChanged()
                        }else{
                            Log.d("Response", response.body()?.message.toString())
                        }
                    }else{
                        Log.d("Response", response.body()?.message.toString())
                    }
                }else if(response.body()?.status == 404){
                    (requireActivity() as MainActivity).hideProgress()
                    mainActivity.showNoData()
                    Log.d("Response", response.body()?.message.toString())
                }
            }

            override fun onFailure(
                call: Call<SingleSingerAudioResponse?>,
                t: Throwable
            ) {
                binding.swipeRefreshLayout.isRefreshing = false
                Log.d("Response", t.message.toString())
            }
        })
    }

    // start time again move at other id
    override fun onAudioClick(title : String, audioLink: String , audioId : String , singerId : String) {
        // Set the play icon to loading state if needed (optional)
        Glide.with(mainActivity)
            .load(R.drawable.ic_play_audio) // optional loading icon
            .into(binding.ivPlay)
        adapter.notifyDataSetChanged()
        MediaManager.onPrepared = {
            val duration = MediaManager.getDuration()
            binding.seekBar.max = duration
            binding.tvEndTime.text = formatTime(duration)

            // Set play icon after audio starts
            Glide.with(mainActivity)
                .load(R.drawable.ic_audio_pause)
                .into(binding.ivPlay)
        }
        MediaManager.onCompletion = {
            if (currentIndex < item.size - 1) {
                currentIndex++
                playCurrentAudio(
                    item[currentIndex].title.toString(),
                    item[currentIndex].audioLink.toString(),
                    item[currentIndex].id.toString(),
                    item[currentIndex].singerId.toString()
                )
            } else {
                Glide.with(mainActivity)
                    .load(R.drawable.ic_replay)  // <-- your replay icon here
                    .into(binding.ivPlay)
                binding.ivPlay.setOnClickListener {
                    playCurrentAudio(
                        item[currentIndex].title.toString(),
                        item[currentIndex].audioLink.toString(),
                        item[currentIndex].id.toString(),
                        item[currentIndex].singerId.toString()
                    )
                }
            }

            adapter.updateSelectedIndex(currentIndex)
            adapter.updateCurrentAudioId(item[currentIndex].id.toString())
            adapter.notifyDataSetChanged()
        }

        MediaManager.onError = { errorMsg ->
            // Optional: Log or show a toast
            Log.e("AudioError", errorMsg)
            Glide.with(mainActivity)
                .load(R.drawable.ic_play_audio)
                .into(binding.ivPlay)
        }
        MediaManager.playAudioFromUrl(title, audioLink, audioId , singerId)
        // Set title text
        binding.tvTitle.text = item[currentIndex].title
        adapter.updateCurrentAudioId(audioId)
    }

    override fun togglePlayPause() {
        MediaManager.togglePlayPause()
        updatePlayPauseIcon()
    }
}