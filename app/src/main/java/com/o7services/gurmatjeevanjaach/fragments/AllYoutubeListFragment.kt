package com.o7services.gurmatjeevanjaach.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.o7services.gurmatjeevanjaach.R
import com.o7services.gurmatjeevanjaach.activity.MainActivity
import com.o7services.gurmatjeevanjaach.adapter.YoutubeAdapter
import com.o7services.gurmatjeevanjaach.databinding.FragmentAllYoutubeListBinding
import com.o7services.gurmatjeevanjaach.dataclass.AllLinkResponse
import com.o7services.gurmatjeevanjaach.dataclass.SingleYoutubeDataClass
import com.o7services.gurmatjeevanjaach.dataclass.SocialLinkSingleResponse
import com.o7services.gurmatjeevanjaach.dataclass.YoutubeResponse
import com.o7services.gurmatjeevanjaach.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Response

class AllYoutubeListFragment : Fragment() , YoutubeAdapter.itemClickListener{
    lateinit var binding : FragmentAllYoutubeListBinding
    lateinit var mainActivity: MainActivity
    lateinit var linearLayoutManager: LinearLayoutManager
    var item = ArrayList<AllLinkResponse.Data>()
    var title = ""
    var categoryImage = ""

    lateinit var adapter: YoutubeAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity as MainActivity
        arguments?.let {
            title = it.getString("title", "")
            categoryImage = it.getString("categoryImage", "")
            val data = it.getParcelable<AllLinkResponse.Data>("data")
            data?.let {
                item.add(it)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllYoutubeListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity.tvTitle.text = title
        adapter  = YoutubeAdapter(item, categoryImage, this)
        linearLayoutManager = LinearLayoutManager(mainActivity)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = linearLayoutManager
        adapter.notifyDataSetChanged()
//        RetrofitClient.instance.getAllYoutube().enqueue(object : retrofit2.Callback<YoutubeResponse>{
//            override fun onResponse(
//                call: Call<YoutubeResponse?>,
//                response: Response<YoutubeResponse?>
//            ) {
//                if (response.body()?.status == 200){
//                    if (response.body()?.success == true){
//                        val data = response.body()?.data
//                        if (data != null){
//                            item.clear()
//                            item.addAll(data)
//                            adapter.notifyDataSetChanged()
//                        }else{
//                            Log.d("Response", response.body()?.message.toString())
//                        }
//                    }else{
//                        Log.d("Response", response.body()?.message.toString())
//                    }
//                }
//            }
//
//            override fun onFailure(
//                call: Call<YoutubeResponse?>,
//                t: Throwable
//            ) {
//                    Log.d("Response", t.message.toString())
//            }
//
//        })
    }

    override fun onItemClick(link : String) {
        var tvLink = Uri.parse(link)
        var intent = Intent(Intent.ACTION_VIEW, tvLink)
        startActivity(intent)

    }


}