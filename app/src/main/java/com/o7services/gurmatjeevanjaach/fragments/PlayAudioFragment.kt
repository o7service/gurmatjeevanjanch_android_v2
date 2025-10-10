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

    var currentIndex = 0
    var id = ""
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
        Log.d("Response", MediaManager.currentTitle.toString())
        Log.d("Response", MediaManager.currentUrl.toString())
        binding.tvTitle.text = MediaManager.currentTitle ?: ""


        // Update Play/Pause Icon
//        if (MediaManager.isAudioPlaying()){
//            Glide.with(mainActivity)
//                .load(R.drawable.ic_audio_pause)
//                .into(binding.ivPlay)
//        }else{
//            Glide.with(mainActivity)
//                .load(R.drawable.ic_play_audio)
//                .into(binding.ivPlay)
//        }

        // Update SeekBar
        val duration = MediaManager.getDuration()
        val position = MediaManager.getCurrentPosition()

        binding.seekBar.max = duration
        binding.seekBar.progress = position

        binding.tvEndTime.text = formatTime(duration)
        binding.tvStartTime.text = formatTime(position)
        startSeekBarUpdates()
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
                playCurrentAudio(item[currentIndex].title.toString(),item[currentIndex].audioLink.toString())
            } else {
                playCurrentAudio(item[currentIndex].title.toString(),item[currentIndex].audioLink.toString())
            }
        }
        binding.ivNext.setOnClickListener {
            if (currentIndex < item.size - 1) {
                currentIndex++
                playCurrentAudio(item[currentIndex].title.toString(),item[currentIndex].audioLink.toString())
            } else {
                playCurrentAudio(item[currentIndex].title.toString(),item[currentIndex].audioLink.toString())

            }
        }
        binding.ivPlay.setOnClickListener {
            MediaManager.togglePlayPause()
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
                            loadAudios(id)
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
    }
    private fun playCurrentAudio(title : String, audioLink : String) {
        Glide.with(mainActivity)
            .load(R.drawable.ic_play_audio) // optional loading icon
            .into(binding.ivPlay)

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
            // Playback completed, reset icon to play
            Glide.with(mainActivity)
                .load(R.drawable.ic_play_audio)
                .into(binding.ivPlay)
        }

        MediaManager.onError = { errorMsg ->
            // Optional: Log or show a toast
            Log.e("AudioError", errorMsg)
            Glide.with(mainActivity)
                .load(R.drawable.ic_play_audio)
                .into(binding.ivPlay)
        }

        MediaManager.playAudioFromUrl(title,audioLink)

        // Set title text
        binding.tvTitle.text = item[currentIndex].title

        adapter.updateSelectedIndex(currentIndex)
    }

    private fun startSeekBarUpdates() {
        val duration = MediaManager.getDuration()
        binding.seekBar.max = duration
        binding.tvEndTime.text = formatTime(duration)

        val handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {
                val currentPosition = MediaManager.getCurrentPosition()
                if (MediaManager.isAudioPlaying()) {
                    binding.seekBar.progress = currentPosition
                    binding.tvStartTime.text = formatTime(currentPosition)
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
//                            val imageBaseUrl = AppConst.imageBaseUrl + data.imageUrl
//                            Glide.with(mainActivity)
//                                .load(imageBaseUrl)
////            .placeholder()
//                                .into(binding.ivAudio)
//                            binding.tvTitle.text = data.name
                            adapter = PlayAudioAdapter(item, categoryImage = audioImage , this@PlayAudioFragment)
                            linearLayoutManager = LinearLayoutManager(mainActivity)
                            binding.recyclerView.adapter = adapter
                            binding.recyclerView.layoutManager = linearLayoutManager
                            item.clear()
                            item.add(data)
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

    override fun onAudioClick(title : String, audioLink: String) {
        // Set the play icon to loading state if needed (optional)
        Glide.with(mainActivity)
            .load(R.drawable.ic_play_audio) // optional loading icon
            .into(binding.ivPlay)

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
            // Playback completed, reset icon to play
            Glide.with(mainActivity)
                .load(R.drawable.ic_play_audio)
                .into(binding.ivPlay)
        }

        MediaManager.onError = { errorMsg ->
            // Optional: Log or show a toast
            Log.e("AudioError", errorMsg)
            Glide.with(mainActivity)
                .load(R.drawable.ic_play_audio)
                .into(binding.ivPlay)
        }

        MediaManager.playAudioFromUrl(title, audioLink)
        // Set title text
        binding.tvTitle.text = item[currentIndex].title
        adapter.updateSelectedIndex(currentIndex)
    }

}