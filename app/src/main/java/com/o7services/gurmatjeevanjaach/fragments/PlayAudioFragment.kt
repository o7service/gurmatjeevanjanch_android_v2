package com.o7services.gurmatjeevanjaach.fragments
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
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.o7services.gurmatjeevanjaach.R
import com.o7services.gurmatjeevanjaach.activity.MainActivity
import com.o7services.gurmatjeevanjaach.adapter.PlayAudioAdapter
import com.o7services.gurmatjeevanjaach.consts.AppConst
import com.o7services.gurmatjeevanjaach.databinding.FragmentPlayAudioBinding
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
    var textTitle = ""
    var tvSingerName = ""
    lateinit var mainActivity: MainActivity
    lateinit var tvTitle : TextView
    var currentIndex = 0
    private var audioStartPoint = 0
    private val audioLimit = 5
    private var isAudioLoading = false
    private var isAudioLastPage = false
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
            textTitle = it.getString("title", "")
        }
    }

    // want to change the height of seek bar like 4dp with round corners in android kotlin

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlayAudioBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        mainActivity.tvTitle.setText(textTitle)
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
                .load(R.drawable.icon_play_final)
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
        mainActivity.tvTitle.setText(textTitle)
//        if (MediaManager.isAudioPlaying()) {
//            Glide.with(mainActivity)
//                .load(R.drawable.icon_pause_solid)
//                .into(binding.ivPlay)
//        } else {
//            Glide.with(mainActivity)
//                .load(R.drawable.icon_play_final)
//                .into(binding.ivPlay)
//        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            currentIndex = 0
            adapter.updateSelectedIndex(currentIndex)
            adapter.updateCurrentAudioId(item[currentIndex].id.toString())
            mainActivity.showProgress()
            audioStartPoint = 0
            isAudioLastPage = false
            isAudioLoading = false
            getAllAudio()
            uiSet()
        }
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(
                recyclerView: RecyclerView,
                dx: Int,
                dy: Int
            ) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) {

                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition =
                        layoutManager.findFirstVisibleItemPosition()

                    if (!isAudioLoading && !isAudioLastPage) {

                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                        ) {
                            Log.d("Response id", id)
                           // mainActivity.showProgress()
                            loadAudios(id)
                        }
                    }
                }
            }
        })

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
                currentIndex = 0
                playCurrentAudio(item[currentIndex].title.toString(),item[currentIndex].audioLink.toString() , item[currentIndex].id.toString(),item[currentIndex].singerId.toString())
            }
            adapter.notifyDataSetChanged()
        }
        binding.ivPlay.setOnClickListener {
            if (MediaManager.isAudioPlaying()) {
                // If currently playing → pause
                MediaManager.pause()
                Glide.with(mainActivity)
                    .load(R.drawable.icon_play_final)
                    .into(binding.ivPlay)
            } else {
                // Not playing → decide what to do
                if (MediaManager.hasCurrentTrack()) {
                    // A track is already loaded → resume it
                    MediaManager.play()
                } else {
                    // No track loaded yet → play the first one
                    currentIndex = 0
                    playCurrentAudio(
                        item[currentIndex].title.toString(),
                        item[currentIndex].audioLink.toString(),
                        item[currentIndex].id.toString(),
                        item[currentIndex].singerId.toString()
                    )
                }
                Glide.with(mainActivity)
                    .load(R.drawable.icon_pause_final)
                    .into(binding.ivPlay)
            }
            adapter.notifyDataSetChanged()
        }


    }
    private fun updatePlayPauseIcon() {
        if (MediaManager.isAudioPlaying()) {
            Glide.with(mainActivity)
                .load(R.drawable.icon_pause_final)
                .into(binding.ivPlay)
        } else {
            Glide.with(mainActivity)
                .load(R.drawable.icon_play_final)
                .into(binding.ivPlay)
        }
    }
    fun getAllAudio(){
        mainActivity.showProgress()
        RetrofitClient.instance(mainActivity).getSingleSinger(SingleSingerRequest(id = id)).
        enqueue(object : retrofit2.Callback<SingleSingerResponse>{
            override fun onResponse(
                call: Call<SingleSingerResponse?>,
                response: Response<SingleSingerResponse?>
            ) {
                if (response.body()?.status == 200){
                    if (response.body()?.success == true){
                        val data = response.body()?.data
                        if (data != null){
                            tvSingerName = data.name.toString()
                            audioImage = data.imageUrl.toString()
                            val imageBaseUrl = AppConst.imageBaseUrl + data.imageUrl
                            Glide.with(mainActivity)
                                .load(imageBaseUrl)
                                .error(R.drawable.no_image)
                                .placeholder(R.drawable.no_image)
                                .into(binding.ivAudio)
                            loadAudios(id)
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
    private fun playCurrentAudio(title: String, audioLink: String, audioId: String, singerId: String) {
        Glide.with(mainActivity)
            .load(R.drawable.icon_play_final) // optional loading icon
            .into(binding.ivPlay)
        binding.tvTitle.setText(title)
        MediaManager.onPrepared = {
            val duration = MediaManager.getDuration()
            binding.seekBar.max = duration
            binding.tvEndTime.text = formatTime(duration)
            // Update play icon after audio starts
            updatePlayPauseIcon()
            adapter.notifyDataSetChanged()
        }
        MediaManager.onCompletion = {
            if (currentIndex < item.size - 1) {
                currentIndex++
                playCurrentAudio(
                    item[currentIndex].title.toString(),
                    item[currentIndex].audioLink.toString(),
                    item[currentIndex].id.toString(),
                    item[currentIndex].singerId.toString())
            } else {
                currentIndex = 0
//                Glide.with(mainActivity)
//                    .load(R.drawable.ic_replay)
//                    .into(binding.ivPlay)
                //binding.ivPlay.setOnClickListener {
                    playCurrentAudio(
                        item[currentIndex].title.toString(),
                        item[currentIndex].audioLink.toString(),
                        item[currentIndex].id.toString(),
                        item[currentIndex].singerId.toString()
                    )
              //  }
            }
            adapter.updateSelectedIndex(currentIndex)
            adapter.updateCurrentAudioId(item[currentIndex].id.toString())
            adapter.notifyDataSetChanged()
            updatePlayPauseIcon() // update icon on completion
        }

        MediaManager.onError = { errorMsg ->
            Log.e("AudioError", errorMsg)
            Glide.with(mainActivity)
                .load(R.drawable.icon_play_final)
                .into(binding.ivPlay)
            updatePlayPauseIcon()
        }
        MediaManager.playAudioFromUrl(title, audioLink, audioId, singerId , tvSingerName)

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
//    fun loadAudios(id : String){
//        RetrofitClient.instance(mainActivity).getSingleSingerAudio(SingleSingerAudioRequest(singerId = id)).
//        enqueue(object : retrofit2.Callback<SingleSingerAudioResponse>{
//            override fun onResponse(
//                call: Call<SingleSingerAudioResponse?>,
//                response: Response<SingleSingerAudioResponse?>
//            ) {
//                binding.swipeRefreshLayout.isRefreshing = false
//                if (response.body()?.status == 200){
//                    if (response.body()?.success == true){
//                        val data = response.body()?.data
//                        if (data != null){
//                                item.clear()
//                                item.addAll(data)
//                                adapter = PlayAudioAdapter(item, title = textTitle, categoryImage = audioImage, this@PlayAudioFragment)
//                                linearLayoutManager = LinearLayoutManager(mainActivity)
//                                binding.recyclerView.layoutManager = linearLayoutManager
//                                binding.recyclerView.adapter = adapter
//                                adapter.updateCurrentAudioId(MediaManager.currentAudioId ?: "")
//                                adapter.updateSelectedIndex(currentIndex)
//                                mainActivity.hideNoData()
//                                mainActivity.hideProgress()
//                                adapter.notifyDataSetChanged()
//
//                        }else{
//                            Log.d("Response", response.body()?.message.toString())
//                        }
//                    }else{
//                        Log.d("Response", response.body()?.message.toString())
//                    }
//                }else if(response.body()?.status == 404){
//                    (requireActivity() as MainActivity).hideProgress()
//                    mainActivity.showNoData()
//                    Log.d("Response", response.body()?.message.toString())
//                }
//            }
//
//            override fun onFailure(
//                call: Call<SingleSingerAudioResponse?>,
//                t: Throwable
//            ) {
//                binding.swipeRefreshLayout.isRefreshing = false
//                Log.d("Response", t.message.toString())
//            }
//        })
//    }
    // in loadAudios add pagination

    fun loadAudios(id: String) {
        if (isAudioLoading || isAudioLastPage) return
        isAudioLoading = true

        RetrofitClient.instance(mainActivity)
            .getSingleSingerAudio(
                SingleSingerAudioRequest(
                    singerId = id,
                    startpoint = audioStartPoint,
                    limit = audioLimit
                )
            )
            .enqueue(object : retrofit2.Callback<SingleSingerAudioResponse> {
                override fun onResponse(
                    call: Call<SingleSingerAudioResponse>,
                    response: Response<SingleSingerAudioResponse>
                ) {
                    binding.swipeRefreshLayout.isRefreshing = false
                    isAudioLoading = false
                    if (response.isSuccessful && response.body()?.success == true) {
                        val data = response.body()?.data ?: emptyList()
                        Log.d("Response play fragment", id)
                        Log.d("Response play fragment", response.body()?.data?.size.toString())
                        if (audioStartPoint == 0) {
                            item.clear()
                        }

                        item.addAll(data)
                        if (::adapter.isInitialized.not()) {
                            adapter = PlayAudioAdapter(
                                item,
                                title = textTitle,
                                categoryImage = audioImage,
                                this@PlayAudioFragment
                            )
                            binding.recyclerView.layoutManager =
                                LinearLayoutManager(mainActivity)
                            binding.recyclerView.adapter = adapter
                        }

                        adapter.notifyDataSetChanged()

                        if (data.size < audioLimit) {
                            isAudioLastPage = true
                        } else {
                            audioStartPoint += audioLimit
                        }

                        mainActivity.hideProgress()
                    }
                }

                override fun onFailure(
                    call: Call<SingleSingerAudioResponse>,
                    t: Throwable
                ) {
                    isAudioLoading = false
                    binding.swipeRefreshLayout.isRefreshing = false
                }
            })
    }


    override fun onAudioClick(title : String, audioLink: String , audioId : String , singerId : String) {
        // Set the play icon to loading state if needed (optional)
        currentIndex = item.indexOfFirst { it.id.toString() == audioId }
        if (currentIndex == -1) currentIndex = 0
        binding.tvTitle.text = title
        Log.d("Audio Id", audioId)
        Glide.with(mainActivity)
            .load(R.drawable.icon_play_final) // optional loading icon
            .into(binding.ivPlay)
        adapter.notifyDataSetChanged()
        MediaManager.onPrepared = {
            binding.tvTitle.text = item[currentIndex].title
            val duration = MediaManager.getDuration()
            binding.seekBar.max = duration
            binding.tvEndTime.text = formatTime(duration)
            adapter.updateSelectedIndex(currentIndex)
            adapter.updateCurrentAudioId(item[currentIndex].id.toString())
            // Set play icon after audio starts
            Glide.with(mainActivity)
                .load(R.drawable.icon_pause_final)
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
                currentIndex = 0
//                Glide.with(mainActivity)
//                    .load(R.drawable.ic_replay)  // <-- your replay icon here
//                    .into(binding.ivPlay)
//                binding.ivPlay.setOnClickListener {
                    playCurrentAudio(
                        item[currentIndex].title.toString(),
                        item[currentIndex].audioLink.toString(),
                        item[currentIndex].id.toString(),
                        item[currentIndex].singerId.toString()
                    )
              //  }
            }
            adapter.updateSelectedIndex(currentIndex)
            adapter.updateCurrentAudioId(item[currentIndex].id.toString())
            adapter.notifyDataSetChanged()
        }

        MediaManager.onError = { errorMsg ->
            // Optional: Log or show a toast
            Log.e("AudioError", errorMsg)
            Glide.with(mainActivity)
                .load(R.drawable.icon_play_final)
                .into(binding.ivPlay)
        }
        MediaManager.playAudioFromUrl(title, audioLink, audioId , singerId , tvSingerName)
        // Set title text
        adapter.updateCurrentAudioId(audioId)
    }

    override fun togglePlayPause() {
        MediaManager.togglePlayPause()
        updatePlayPauseIcon()
    }
}