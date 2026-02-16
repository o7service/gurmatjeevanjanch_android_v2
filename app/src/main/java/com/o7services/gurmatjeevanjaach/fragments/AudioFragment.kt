package com.o7services.gurmatjeevanjaach.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.mlkit.nl.translate.TranslatorOptions
import com.o7services.gurmatjeevanjaach.R
import com.o7services.gurmatjeevanjaach.activity.MainActivity
import com.o7services.gurmatjeevanjaach.adapter.AudioAdapter
import com.o7services.gurmatjeevanjaach.databinding.FragmentAudioBinding
import com.o7services.gurmatjeevanjaach.dataclass.AllLinkResponse
import com.o7services.gurmatjeevanjaach.dataclass.AllSingerResponse
import com.o7services.gurmatjeevanjaach.dataclass.AudioDataClass
import com.o7services.gurmatjeevanjaach.dataclass.SingleSingerRequest
import com.o7services.gurmatjeevanjaach.dataclass.SingleSingerResponse
import com.o7services.gurmatjeevanjaach.dataclass.SocialLinkResponse
import com.o7services.gurmatjeevanjaach.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Response

class AudioFragment : Fragment(), AudioAdapter.onItemClickListener {
    lateinit var binding : FragmentAudioBinding
    lateinit var adapter : AudioAdapter
    lateinit var mainActivity: MainActivity
    private var startPoint = 0
    private val limit = 10
    private var isLoading = false
    private var isLastPage = false

    lateinit var linearLayoutManager: LinearLayoutManager
    var item =  ArrayList<AllSingerResponse.Data>()
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
        binding = FragmentAudioBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = AudioAdapter(item, this)
        linearLayoutManager = LinearLayoutManager(mainActivity)
        binding.recyclerView.layoutManager = linearLayoutManager
        binding.recyclerView.adapter = adapter
        getAllAudio()
        binding.swipeRefreshLayout.setOnRefreshListener {
            startPoint = 0
            isLastPage = false
            isLoading = false
            getAllAudio()
        }
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(
                recyclerView: RecyclerView,
                dx: Int,
                dy: Int
            ) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) { // scroll down

                    val visibleItemCount = linearLayoutManager.childCount
                    val totalItemCount = linearLayoutManager.itemCount
                    val firstVisibleItemPosition =
                        linearLayoutManager.findFirstVisibleItemPosition()

                    if (!isLoading && !isLastPage) {

                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                        ) {
                            getAllAudio()
                        }
                    }
                }
            }
        })
    }

    fun getAllAudio() {

        if (isLoading || isLastPage) return

        isLoading = true
        mainActivity.showProgress()
//        val params = HashMap<String, Any?>()
//        params.put("startpoint", startPoint)
//        params.put("limit", limit)

        RetrofitClient.instance(mainActivity)
            .getAllSinger(startPoint , limit)
            .enqueue(object : retrofit2.Callback<AllSingerResponse> {
                override fun onResponse(
                    call: Call<AllSingerResponse>,
                    response: Response<AllSingerResponse>
                ) {
                    binding.swipeRefreshLayout.isRefreshing = false
                    mainActivity.hideProgress()

                    if (response.isSuccessful && response.body()?.success == true) {

                        val data = response.body()?.data ?: emptyList()

                        if (startPoint == 0) {
                            item.clear()
                        }
                        item.addAll(data)
                        isLoading = false
                        adapter.notifyDataSetChanged()

                        if (data.size < limit) {
                            isLastPage = true
                        } else {
                            startPoint += limit
                        }

                    } else {
                        mainActivity.showNoData()
                    }
                }

                override fun onFailure(call: Call<AllSingerResponse>, t: Throwable) {
                    mainActivity.hideProgress()
                    binding.swipeRefreshLayout.isRefreshing = false
                    isLoading = false
                    mainActivity.showNoData()
                }
            })
    }



    override fun onItemClick(id : String , title: String) {
        var bundle = Bundle()
        bundle.putString("id", id)
        bundle.putString("title", title)
        findNavController().navigate(R.id.playAudioFragment, bundle)
    }
}