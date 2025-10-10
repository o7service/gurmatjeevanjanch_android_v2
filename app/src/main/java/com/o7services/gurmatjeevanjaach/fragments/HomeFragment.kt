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
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.o7services.gurmatjeevanjaach.R
import com.o7services.gurmatjeevanjaach.SpannedItemDecor
import com.o7services.gurmatjeevanjaach.activity.MainActivity
import com.o7services.gurmatjeevanjaach.adapter.SliderAdapter
import com.o7services.gurmatjeevanjaach.adapter.SocialLinkAdapter
import com.o7services.gurmatjeevanjaach.databinding.FragmentHomeBinding
import com.o7services.gurmatjeevanjaach.dataclass.SocialLinkResponse
import com.o7services.gurmatjeevanjaach.dataclass.AllLinkRequest
import com.o7services.gurmatjeevanjaach.dataclass.AllLinkResponse
import com.o7services.gurmatjeevanjaach.dataclass.SliderItem
import com.o7services.gurmatjeevanjaach.retrofit.MediaManager
import com.o7services.gurmatjeevanjaach.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback
import kotlin.math.abs


class HomeFragment : Fragment() , SocialLinkAdapter.itemClickListener {
    lateinit var binding : FragmentHomeBinding
    lateinit var adapter : SocialLinkAdapter
    lateinit var sliderAdpater : SliderAdapter
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
        binding.tvPlayTitle.text = MediaManager.currentTitle
        updateMiniPlayer()
        getAllCategory()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = SocialLinkAdapter(item, this)
        binding.recyclerView.adapter = adapter
        gridLayoutManager = GridLayoutManager(mainActivity, 3, GridLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = GridLayoutManager(mainActivity,3)
        val items = listOf(
            SliderItem.YouTubeVideo("dcPRBQhqmrs"),
        )

        val adapter = SliderAdapter(items, viewLifecycleOwner)
        binding.viewPager.adapter = adapter
        binding.recyclerView.addItemDecoration(SpannedItemDecor(resources.getDimensionPixelOffset(
            com.intuit.sdp.R.dimen._12sdp),3,true))
        binding.recyclerView.adapter = adapter
        playerUi()
//        binding. viewPager.setPageTransformer { page, position ->
//            page.alpha = 0.25f + (1 - abs(position))
//        }
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                val nextItem = (binding.viewPager.currentItem + 1) % adapter.itemCount
                binding.viewPager.setCurrentItem(nextItem, true)
                handler.postDelayed(this, 5000) // 5 seconds
            }
        }
        handler.postDelayed(runnable, 5000)



        binding.swipeRefreshLayout.setOnRefreshListener {
            (requireActivity() as MainActivity).showProgress()
            getAllCategory()
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
            // Navigate to PlayAudioFragment
//            val playAudioFragment = PlayAudioFragment()
//            parentFragmentManager.beginTransaction()
//                .replace(R.id.fragment_container, playAudioFragment)
//                .addToBackStack(null)
//                .commit()
        }
        Log.d("Response title", MediaManager.currentTitle.toString())
        binding.tvPlayTitle.text = MediaManager.currentTitle

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
                            (requireActivity() as MainActivity).hideProgress()
                            item.clear()
                            item.addAll(data)
                            adapter.notifyDataSetChanged()
                        }else{
                            (requireActivity() as MainActivity).hideProgress()
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
                        Log.d("Response", response.body()?.data?.id.toString())
                        val data = response.body()?.data
                        if (data != null){
                            var bundle = Bundle()
                            bundle.putString("title", title)
                            bundle.putString("categoryImage", categoryImage)
                            bundle.putParcelable("data", data)
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
        binding.tvPlayTitle.text = MediaManager.currentTitle
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
        binding.tvPlayTitle.text = MediaManager.currentTitle
        updatePlayPauseIcon()
        } else {
        binding.clCurrentMusic.visibility = View.GONE
        }
    }

    private fun updatePlayPauseIcon() {
        if (MediaManager.isAudioPlaying()) {
        binding.tvPlayTitle.text = MediaManager.currentTitle
        Glide.with(mainActivity)
            .load(R.drawable.ic_audio_pause)
            .into(binding.ivPlay)
        } else {
        binding.tvPlayTitle.text = MediaManager.currentTitle
        Glide.with(mainActivity)
            .load(R.drawable.ic_play_audio)
            .into(binding.ivPlay)
        }
    }
}