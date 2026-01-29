package com.o7services.gurmatjeevanjaach.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.o7services.gurmatjeevanjaach.R
import com.o7services.gurmatjeevanjaach.SpannedItemDecor
import com.o7services.gurmatjeevanjaach.activity.MainActivity
import com.o7services.gurmatjeevanjaach.adapter.SliderAdapter
import com.o7services.gurmatjeevanjaach.adapter.SocialLinkAdapter
import com.o7services.gurmatjeevanjaach.databinding.FragmentHomeBinding
import com.o7services.gurmatjeevanjaach.dataclass.AllLinkRequest
import com.o7services.gurmatjeevanjaach.dataclass.AllLinkResponse
import com.o7services.gurmatjeevanjaach.dataclass.HomeResponse
import com.o7services.gurmatjeevanjaach.dataclass.SliderItem
import com.o7services.gurmatjeevanjaach.retrofit.MediaManager
import com.o7services.gurmatjeevanjaach.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Response
import kotlin.math.abs

class HomeFragment : Fragment() , SocialLinkAdapter.itemClickListener {
    lateinit var binding : FragmentHomeBinding
    var youtubeId  = ""
    var zoomId= ""
    var youtubeLink = ""
    var zoomLink = ""
    private var hasFetchedYouTube = false
    private var hasFetchedZoom = false
    var link = ""
    private val youtubeItems = mutableListOf<SliderItem.YouTubeVideo>()
    private val zoomItems = mutableListOf<SliderItem.CustomImageWithId>()
    private lateinit var sliderAdapter: SliderAdapter
    private var currentItems: List<SliderItem> = emptyList()
    lateinit var socialLinkAdapter : SocialLinkAdapter
    lateinit var mainActivity : MainActivity
    lateinit var gridLayoutManager: GridLayoutManager
    var item : ArrayList<HomeResponse.Categories> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity as MainActivity
        arguments?.let {
            
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        getHome()
        binding.viewPager.setCurrentItem(0, false)
        MediaManager.currentTitleLiveData.observe(viewLifecycleOwner) { newTitle ->
            binding.tvPlayTitle.text = "$newTitle - By ${MediaManager.currentSingerName}"
        }
        updateMiniPlayer()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getHome()
        socialLinkAdapter = SocialLinkAdapter(item, this)
        binding.recyclerView.adapter = socialLinkAdapter
        gridLayoutManager = GridLayoutManager(mainActivity, 3, GridLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = GridLayoutManager(mainActivity,3)
        binding.recyclerView.addItemDecoration(SpannedItemDecor(resources.getDimensionPixelOffset(
            com.intuit.sdp.R.dimen._12sdp),3,true))
        link = extractYouTubeVideoId(youtubeLink).toString()
        val items = listOf(
            SliderItem.YouTubeVideo(link)
        )
        val sliderAdapter = SliderAdapter(items, viewLifecycleOwner)
        binding.viewPager.adapter = sliderAdapter
        playerUi()
        binding. viewPager.setPageTransformer { page, position ->
            page.alpha = 0.25f + (1 - abs(position))
        }
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            val tabView = LayoutInflater.from(context).inflate(R.layout.item_custom_tab, null)
            tab.customView = tabView
        }.attach()

        val tab = binding.tabLayout.getTabAt(0)
        val indicator1 = tab?.customView?.findViewById<View>(R.id.indicator1)
        val indicator2 = tab?.customView?.findViewById<View>(R.id.indicator2)
        if (binding.viewPager.currentItem == 0) {
            indicator1?.setBackgroundResource(R.drawable.bg_tab_selected)
            indicator2?.setBackgroundResource(R.drawable.bg_tab_unselected)
        } else {
            indicator1?.setBackgroundResource(R.drawable.bg_tab_unselected)
            indicator2?.setBackgroundResource(R.drawable.bg_tab_selected)
        }
        binding.viewPager.getChildAt(0).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN,
                MotionEvent.ACTION_MOVE -> {
                    binding.swipeRefreshLayout.isEnabled = false
                }
                MotionEvent.ACTION_UP,
                MotionEvent.ACTION_CANCEL -> {
                    binding.swipeRefreshLayout.isEnabled = true
                }
            }
            false
        }
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val tab = binding.tabLayout.getTabAt(0)
                val indicator1 = tab?.customView?.findViewById<View>(R.id.indicator1)
                val indicator2 = tab?.customView?.findViewById<View>(R.id.indicator2)
                if (position == 0) {
                    indicator1?.setBackgroundResource(R.drawable.bg_tab_selected)
                    indicator2?.setBackgroundResource(R.drawable.bg_tab_unselected)
                } else {
                    indicator1?.setBackgroundResource(R.drawable.bg_tab_unselected)
                    indicator2?.setBackgroundResource(R.drawable.bg_tab_selected)
                }
            }
        })
        binding.icZoomForward.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(zoomLink))
            startActivity(intent)
        }
        binding.liveSmagam.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(zoomLink))
            startActivity(intent)
        }

        binding.cardZoom.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(zoomLink))
            startActivity(intent)
        }
        binding.clCurrentMusic.setOnClickListener {
            var bundle = Bundle()
            bundle.putString("id", MediaManager.currentSingerId)
            bundle.putString("title", MediaManager.currentSingerName)
            findNavController().navigate(R.id.playAudioFragment, bundle)
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            (requireActivity() as MainActivity).showProgress()
            getHome()
            playerUi()
        }
    }
    private fun playerUi(){
        updateMiniPlayer()
        binding.ivPlay.setOnClickListener {
            MediaManager.togglePlayPause()
            updatePlayPauseIcon()
        }
        binding.clCurrentMusic.setOnClickListener {
        }
        Log.d("Response title", "${MediaManager.currentTitle}- By ${MediaManager.currentSingerName}")
        MediaManager.currentTitleLiveData.observe(viewLifecycleOwner) { newTitle ->
            binding.tvPlayTitle.text = "$newTitle - By ${MediaManager.currentSingerName}"
        }
    }

    private fun getHome(){
        RetrofitClient.instance.home().enqueue(object : retrofit2.Callback<HomeResponse>{
            override fun onResponse(
                call: Call<HomeResponse?>,
                response: Response<HomeResponse?>
            ) {
                if(response.body()?.data != null){
                    var categoryData = response.body()?.data?.categories
                    (mainActivity as MainActivity).hideProgress()
                    binding.clHome.visibility = View.VISIBLE
                    binding.swipeRefreshLayout.isRefreshing = false
                    if (categoryData != null){
                        item.clear()
                        item.addAll(categoryData)
                        socialLinkAdapter.notifyDataSetChanged()
                        mainActivity.hideNoData()
                    }
                    var youtubeData = response.body()?.data?.youtubeLink
                    var zoomData = response.body()?.data?.zoomLink
                    if (youtubeData != null) {
                        binding.cardPager.visibility = View.VISIBLE
                        val videoId = extractYouTubeVideoId(youtubeData?.link.toString())
                        if (!videoId.isNullOrEmpty()) {
                            youtubeLink = youtubeData.link.toString()
                            youtubeId = videoId
                            hasFetchedYouTube = true
                            Log.d("YouTube Link", youtubeLink)
                        }
                    }else{
                        binding.cardPager.visibility = View.GONE
                    }
                    if (zoomData != null){
                        binding.liveSmagam.visibility = View.VISIBLE
                        zoomId = zoomData.id.toString()
                        zoomLink = zoomData.link.toString()
                    }
                    else{
                        binding.liveSmagam.visibility = View.GONE
                    }
                    if (youtubeId != null && zoomLink != null){
                        updateSlider(youtubeId, zoomLink)
                    }
                }else{
                    Log.d("Response", response.body()?.message.toString())
                }
            }

            override fun onFailure(
                call: Call<HomeResponse?>,
                t: Throwable
            ) {
                Log.d("Response", t.message.toString())
            }

        })
    }

    private fun updateSlider(youtubeVideoId: String?, zoomLink: String?) {
    if (!isAdded || view == null || !isVisible) return
    val items = mutableListOf<SliderItem>()
    if (!youtubeVideoId.isNullOrEmpty()) {
        items.add(SliderItem.YouTubeVideo(youtubeVideoId))
    }
//    if (!zoomLink.isNullOrEmpty()) {
//        items.add(SliderItem.CustomImageWithId(R.drawable.zoom_video, zoomLink))
//    }
    if (items.isEmpty()) {
        Log.d("Slider", "No YouTube  to show")
    }

    val sliderAdapter = SliderAdapter(items, viewLifecycleOwner)
    binding.viewPager.adapter = sliderAdapter
    }

    override fun onSocialClick(id : String, categoryImage : String, title : String) {
        RetrofitClient.instance.getLinkCategory(AllLinkRequest(categoryId = id)).
        enqueue(object : retrofit2.Callback<AllLinkResponse>{
            override fun onResponse(
                call: Call<AllLinkResponse?>,
                response: Response<AllLinkResponse?>
            ) {
                if (response.body()?.status == 200){
                    if (response.body()?.success == true){
                        val data = response.body()?.data
                        if (data != null){
                            var bundle = Bundle()
                            bundle.putString("title", title)
                            bundle.putString("categoryImage", categoryImage)
                            bundle.putParcelableArrayList("data", ArrayList(data))
                            mainActivity.hideNoData()
                            findNavController().navigate(R.id.allYoutubeListFragment, bundle)
                        }else{
                            Toast.makeText(mainActivity, response.body()?.message.toString(), Toast.LENGTH_SHORT).show()
                            Log.d("Response", response.body()?.message.toString())
                        }
                    }else{
                        Toast.makeText(mainActivity, response.body()?.message.toString(), Toast.LENGTH_SHORT).show()
                        Log.d("Response", response.body()?.message.toString())
                    }
                }
            }

            override fun onFailure(
                call: Call<AllLinkResponse?>,
                t: Throwable
            ) {
                Log.d("Response", t.message.toString())
            }

        })
        MediaManager.currentTitleLiveData.observe(viewLifecycleOwner) { newTitle ->
            binding.tvPlayTitle.text = "$newTitle - By ${MediaManager.currentSingerName}"
        }
        updateMiniPlayer()
        binding.ivPlay.setOnClickListener {
            MediaManager.togglePlayPause()
            updatePlayPauseIcon()
        }
        binding.clCurrentMusic.setOnClickListener {
        }
        Log.d("Response title", MediaManager.currentTitle.toString())
    }

    private fun updateMiniPlayer() {
        if (!MediaManager.currentTitle.isNullOrEmpty()) {
        binding.clCurrentMusic.visibility = View.VISIBLE
        MediaManager.currentTitleLiveData.observe(viewLifecycleOwner) { newTitle ->
            binding.tvPlayTitle.text = "$newTitle - By ${MediaManager.currentSingerName}"
        }
        updatePlayPauseIcon()
        } else {
        binding.clCurrentMusic.visibility = View.GONE
        }
    }

    private fun updatePlayPauseIcon() {
        if (MediaManager.isAudioPlaying()) {
        MediaManager.currentTitleLiveData.observe(viewLifecycleOwner) { newTitle ->
            binding.tvPlayTitle.text = "$newTitle - By ${MediaManager.currentSingerName}"
        }
        Glide.with(mainActivity)
            .load(R.drawable.icon_pause_final)
            .into(binding.ivPlay)
        }else {
            binding.tvPlayTitle.text = "${MediaManager.currentTitle} - By ${MediaManager.currentSingerName}"
        Glide.with(mainActivity)
            .load(R.drawable.icon_play_final)
            .into(binding.ivPlay)
        }
    }
    fun extractYouTubeVideoId(url: String): String? {
        val patterns = listOf(
            Regex("""(?:v=)([A-Za-z0-9_-]{11})"""),
            Regex("""(?:youtu\.be/)([A-Za-z0-9_-]{11})"""),
            Regex("""(?:embed/)([A-Za-z0-9_-]{11})"""),
            Regex("""(?:shorts/)([A-Za-z0-9_-]{11})"""),
            Regex("""(?:live/)([A-Za-z0-9_-]{11})""")
        )

        for (pattern in patterns) {
            val match = pattern.find(url)
            if (match != null) {
                return match.groups[1]?.value
            }
        }
        return null
    }


}