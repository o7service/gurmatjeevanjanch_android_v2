package com.o7services.gurmatjeevanjaach.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.o7services.gurmatjeevanjaach.dataclass.SocialLinkResponse
import com.o7services.gurmatjeevanjaach.dataclass.AllLinkRequest
import com.o7services.gurmatjeevanjaach.dataclass.AllLinkResponse
import com.o7services.gurmatjeevanjaach.dataclass.SingleLinkRequest
import com.o7services.gurmatjeevanjaach.dataclass.SingleLinkResponse
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
    var link = ""
    private val youtubeItems = mutableListOf<SliderItem.YouTubeVideo>()
    private val zoomItems = mutableListOf<SliderItem.CustomImageWithId>()
    private lateinit var sliderAdapter: SliderAdapter
    private var currentItems: List<SliderItem> = emptyList()
    lateinit var socialLinkAdapter : SocialLinkAdapter
    lateinit var mainActivity : MainActivity
    lateinit var gridLayoutManager: GridLayoutManager
    var item = ArrayList<SocialLinkResponse.Data>()
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
        MediaManager.currentTitleLiveData.observe(viewLifecycleOwner) { newTitle ->
            binding.tvPlayTitle.text = newTitle
        }
        updateMiniPlayer()
        getAllCategory()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
//        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("YouTube"))
//        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Zoom"))
//
//        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab) {
//                currentItems = when (tab.position) {
//                    0 -> youtubeItems
//                    1 -> zoomItems
//                    else -> emptyList()
//                }
//                sliderAdapter.updateItems(currentItems)
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab) {}
//            override fun onTabReselected(tab: TabLayout.Tab) {}
//        })
//        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
//            val tabView = LayoutInflater.from(context).inflate(R.layout.item_custom_tab, null)
//            tab.customView = tabView
//        }.attach()
//
//        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab?) {
//                val indicator = tab?.customView?.findViewById<View>(R.id.indicator)
//                indicator?.setBackgroundResource(R.drawable.bg_tab_selected)
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab?) {
//                val indicator = tab?.customView?.findViewById<View>(R.id.indicator)
//                indicator?.setBackgroundResource(R.drawable.bg_tab_unselected)
//            }
//
//            override fun onTabReselected(tab: TabLayout.Tab?) {}
//        })
//
//
//        binding.tabLayout.getTabAt(binding.viewPager.currentItem)
//            ?.customView?.findViewById<View>(R.id.indicator)
//            ?.setBackgroundResource(R.drawable.bg_tab_selected)
//        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//            override fun onPageSelected(position: Int) {
//                super.onPageSelected(position)
//
//                for (i in 0 until binding.tabLayout.tabCount) {
//                    val tab = binding.tabLayout.getTabAt(i)
//                    val indicator = tab?.customView?.findViewById<View>(R.id.indicator)
//                    if (i == position) {
//                        indicator?.setBackgroundResource(R.drawable.bg_tab_selected)
//                    } else {
//                        indicator?.setBackgroundResource(R.drawable.bg_tab_unselected)
//                    }
//                }
//            }
//
//        })

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            val tabView = LayoutInflater.from(context).inflate(R.layout.item_custom_tab, null)
            tab.customView = tabView
        }.attach()

     //   updateStripIndicators(binding.viewPager.currentItem)
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
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                val tab = binding.tabLayout.getTabAt(0) // Only one tab
                val indicator1 = tab?.customView?.findViewById<View>(R.id.indicator1)
                val indicator2 = tab?.customView?.findViewById<View>(R.id.indicator2)

                if (position == 0) {
                    // YouTube page
                    indicator1?.setBackgroundResource(R.drawable.bg_tab_selected)
                    indicator2?.setBackgroundResource(R.drawable.bg_tab_unselected)
                } else {
                    // Image page
                    indicator1?.setBackgroundResource(R.drawable.bg_tab_unselected)
                    indicator2?.setBackgroundResource(R.drawable.bg_tab_selected)
                }
            }
        })
// Set initial indicator states





        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                val nextItem = (binding.viewPager.currentItem + 1) % sliderAdapter.itemCount
                binding.viewPager.setCurrentItem(nextItem, true)
                handler.postDelayed(this, 5000)
            }
        }
        handler.postDelayed(runnable, 5000)

        binding.swipeRefreshLayout.setOnRefreshListener {
            (requireActivity() as MainActivity).showProgress()
            getAllCategory()
            playerUi()
        }
    }

//    private fun updateStripIndicators(selectedPosition: Int) {
//        for (i in 0 until binding.tabLayout.tabCount) {
//            val tab = binding.tabLayout.getTabAt(i)
//            val indicator = tab?.customView?.findViewById<View>(R.id.indicator)
//            if (i == selectedPosition) {
//                indicator?.setBackgroundResource(R.drawable.bg_tab_selected)
//            } else {
//                indicator?.setBackgroundResource(R.drawable.bg_tab_unselected)
//            }
//        }
//    }


    private fun playerUi(){
        updateMiniPlayer()
        binding.ivPlay.setOnClickListener {
            MediaManager.togglePlayPause()
            updatePlayPauseIcon()
        }
        binding.clCurrentMusic.setOnClickListener {
            // Navigate to PlayAudioFragment
//            val playAudioFragment = PlayAudioFragment()
//            parentFragmentManager.beginTransaction()
//                .replace(R.id.fragment_container, playAudioFragment)
//                .addToBackStack(null)
//                .commit()
        }
        Log.d("Response title", MediaManager.currentTitle.toString())
      //  binding.tvPlayTitle.text = MediaManager.currentTitle
        MediaManager.currentTitleLiveData.observe(viewLifecycleOwner) { newTitle ->
            binding.tvPlayTitle.text = newTitle
        }
    }

    private fun getSingleLink(id: String) {
        RetrofitClient.instance.getSingleLink(SingleLinkRequest(categoryId = id))
            .enqueue(object : retrofit2.Callback<SingleLinkResponse> {
                override fun onResponse(
                    call: Call<SingleLinkResponse?>,
                    response: Response<SingleLinkResponse?>
                ) {
                    Log.d("Response", "single link hit")
                    binding.swipeRefreshLayout.isRefreshing = false
                    mainActivity.hideProgress()

                    val body = response.body()
                    if (response.isSuccessful && body?.success == true && body.status == 200) {
                        val dataList = body.data
                        if (!dataList.isNullOrEmpty()) {
                            for (item in dataList) {
                                val link = item.link.orEmpty()
                                if (item.isLive == 1) {
                                    val videoId = extractYouTubeVideoId(link)
                                    if (!videoId.isNullOrEmpty()) {
                                        youtubeLink = link
                                        youtubeId = videoId
                                    }
                                } else {
                                    zoomLink = link
                                }
                            }
                            // Now update the slider once all data has been parsed
                            updateSlider(youtubeId, zoomLink)
                        } else {
                            Log.d("Response", "Data list is empty")
                        }
                    } else {
                        Log.d("Response", "Failed with status: ${body?.status}, message: ${body?.message}")
                    }
                }

                override fun onFailure(call: Call<SingleLinkResponse?>, t: Throwable) {
                    binding.swipeRefreshLayout.isRefreshing = false
                    mainActivity.hideProgress()
                    Log.e("NetworkError", "Failed to fetch single link", t)
                }
            })
    }

    private fun updateSlider(youtubeVideoId: String?, zoomLink: String?) {
    if (!isAdded || view == null || !isVisible) return
    val items = mutableListOf<SliderItem>()
    if (!youtubeVideoId.isNullOrEmpty()) {
        items.add(SliderItem.YouTubeVideo(youtubeVideoId))
    }
    if (!zoomLink.isNullOrEmpty()) {
        items.add(SliderItem.CustomImageWithId(R.drawable.dummy_image, zoomLink))
    }
    if (items.isEmpty()) {
        // Optionally handle empty state
        Log.d("Slider", "No YouTube or Zoom links to show")
    }
    val sliderAdapter = SliderAdapter(items, viewLifecycleOwner)
    binding.viewPager.adapter = sliderAdapter

    }
    private fun getAllCategory(){
        (requireActivity() as MainActivity).showProgress()
        RetrofitClient.instance.getAllCategory().enqueue(object : retrofit2.Callback<SocialLinkResponse>{
            override fun onResponse(
                call: Call<SocialLinkResponse?>,
                response: Response<SocialLinkResponse?>
            ) {
                binding.swipeRefreshLayout.isRefreshing = false
                if (response.body()?.status == 200){
                    if (response.body()?.success == true){

                        val data = response.body()?.data
                        if (data != null){
                            (mainActivity as MainActivity).hideProgress()
                            binding.clHome.visibility = View.VISIBLE
                            item.clear()
                            item.addAll(data.filter { it.isSingle == 0 }) // only add items with isSingle == true
                            mainActivity.hideNoData()
                            socialLinkAdapter.notifyDataSetChanged()
                            val youtubeItem = data.find { it.title.equals("Youtube", ignoreCase = true) }
                            if (youtubeItem?.id != null) {
                                youtubeId = youtubeItem.id.toString()
                                Log.d("Response", "YouTube ID = $youtubeId")
                                getSingleLink(youtubeId)
                            } else {

                                Log.d("Response", "YouTube item not found or ID is null")
                            }
                            val zoomItem = data.find { it.title.equals("Zoom", ignoreCase = true) }
                            if (zoomItem?.id != null) {
                                zoomId = zoomItem.id.toString()
                                Log.d("Response", "YouTube ID = $zoomId")
                                getSingleLink(zoomId)
                            } else {
                                Log.d("Response", "YouTube item not found or ID is null")
                            }

                            Log.d("Response", youtubeId)
                            getSingleLink(youtubeId)
                        }else{
                            (requireActivity() as MainActivity).hideProgress()
                            mainActivity.showNoData()
                            binding.clHome.visibility = View.INVISIBLE
                            Log.d("Response", response.body()?.message.toString())
                        }
                    }else{
                        (requireActivity() as MainActivity).hideProgress()
                        Log.d("Response", response.body()?.message.toString())
                    }
                }else if(response.body()?.status == 404){
                    (requireActivity() as MainActivity).hideProgress()
                    Log.d("Response", response.body()?.message.toString())
                }
            }
            override fun onFailure(
                call: Call<SocialLinkResponse?>,
                t: Throwable
            ) {
                binding.swipeRefreshLayout.isRefreshing = false
                (requireActivity() as MainActivity).hideProgress()
                Log.d("Response", t.message.toString())
            }
    // with this video, then show of zoom image by default and link form the getSingleCategory , after click on the image intent will work
        })
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
                     //   Log.d("Response", response.body()?.data?.id.toString())
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
            binding.tvPlayTitle.text = newTitle
        }
      //  binding.tvPlayTitle.text = MediaManager.currentTitle
        updateMiniPlayer()
        binding.ivPlay.setOnClickListener {
            MediaManager.togglePlayPause()
            updatePlayPauseIcon()
        }

        binding.clCurrentMusic.setOnClickListener {
            // Navigate to PlayAudioFragment
//            val playAudioFragment = PlayAudioFragment()
//            parentFragmentManager.beginTransaction()
//                .replace(R.id.fragment_container, playAudioFragment)
//                .addToBackStack(null)
//                .commit()
        }
        Log.d("Response title", MediaManager.currentTitle.toString())
    }

    private fun updateMiniPlayer() {
        if (!MediaManager.currentTitle.isNullOrEmpty()) {
        binding.clCurrentMusic.visibility = View.VISIBLE
   //     binding.tvPlayTitle.text = MediaManager.currentTitle
        MediaManager.currentTitleLiveData.observe(viewLifecycleOwner) { newTitle ->
            binding.tvPlayTitle.text = newTitle
        }
        updatePlayPauseIcon()
        } else {
        binding.clCurrentMusic.visibility = View.GONE
        }
    }

    private fun updatePlayPauseIcon() {
        if (MediaManager.isAudioPlaying()) {
    //    binding.tvPlayTitle.text = MediaManager.currentTitle
        MediaManager.currentTitleLiveData.observe(viewLifecycleOwner) { newTitle ->
            binding.tvPlayTitle.text = newTitle
        }
        Glide.with(mainActivity)
            .load(R.drawable.icon_paused)
            .into(binding.ivPlay)
        } else {
        binding.tvPlayTitle.text = MediaManager.currentTitle
        Glide.with(mainActivity)
            .load(R.drawable.ic_play_audio_home)
            .into(binding.ivPlay)
        }
    }
    fun extractYouTubeVideoId(url: String): String? {
        val patterns = listOf(
            Regex("""(?:v=)([A-Za-z0-9_-]{11})"""),
            Regex("""(?:youtu\.be/)([A-Za-z0-9_-]{11})"""),
            Regex("""(?:embed/)([A-Za-z0-9_-]{11})"""),
            Regex("""(?:shorts/)([A-Za-z0-9_-]{11})""")
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