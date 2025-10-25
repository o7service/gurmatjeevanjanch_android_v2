package com.o7services.gurmatjeevanjaach.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
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
            mainActivity.showProgress()
            getAllAudio()
        }

//        item.add(AudioDataClass(name = "ਭਾਈ ਅਜੀਤ ਸਿੰਘ - ਲੜੀਵਾਰ ਕਥਾ..." , description = "Bhai Amarjit Singh - Larivaar katha...", image = R.drawable.dummy_image.toString()))
//        item.add(AudioDataClass(name = "ਭਾਈ ਅਜੀਤ ਸਿੰਘ - ਲੜੀਵਾਰ ਕਥਾ..." , description = "Bhai Amarjit Singh - Larivaar katha..." ,image = R.drawable.dummy_image.toString()))
//        item.add(AudioDataClass(name = "ਭਾਈ ਅਜੀਤ ਸਿੰਘ - ਲੜੀਵਾਰ ਕਥਾ..." , description = "Bhai Amarjit Singh - Larivaar katha..."  ,image = R.drawable.dummy_image.toString()))
    }

    fun getAllAudio(){
       mainActivity.showProgress()
         RetrofitClient.instance.getAllSinger().enqueue(object : retrofit2.Callback<AllSingerResponse>{
            override fun onResponse(
                call: Call<AllSingerResponse?>,
                response: Response<AllSingerResponse?>
            ) {
                binding.swipeRefreshLayout.isRefreshing = false
                if (response.body()?.status == 200){
                    if (response.body()?.success == true){
                        val data = response.body()?.data
                        if (data != null){
                            mainActivity.hideProgress()
                            item.clear()
                            item.addAll(data)
                            adapter.notifyDataSetChanged()
                            mainActivity.hideNoData()
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
                }else if(response.body()?.status == 404){
                    (requireActivity() as MainActivity).hideProgress()
                    mainActivity.showNoData()
                    Log.d("Response", response.body()?.message.toString())

                }
            }

            override fun onFailure(
                call: Call<AllSingerResponse?>,
                t: Throwable
            ) {
                mainActivity.hideProgress()
                mainActivity.showNoData()
                binding.swipeRefreshLayout.isRefreshing = false
                Log.d("Response", t.message.toString())
            }
        })
    }


    override fun onItemClick(id : String , title: String) {
        var bundle = Bundle()
        bundle.putString("id", id)
        bundle.putString("title", title)
        findNavController().navigate(R.id.playAudioFragment, bundle)
    }
//    fun translateTextWithLibre(
//        sourceText: String,
//        onResult: (String) -> Unit,
//        onError: (Exception) -> Unit
//    ) {
//        val client = OkHttpClient()
//        val json = JSONObject()
//        json.put("q", sourceText)
//        json.put("source", "en")
//        json.put("target", "pa")
//        json.put("format", "text")
//        val body = RequestBody.create(
//            MediaType.get("application/json; charset=utf-8"),
//            json.toString()
//        )
//        val request = Request.Builder()
//            .url("https://libretranslate.com/translate")
//            .post(body)
//            .build()
//        client.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                onError(e)
//            }
//            override fun onResponse(call: Call, response: Response) {
//                response.body?.string()?.let {
//                    val translated = JSONObject(it).getString("translatedText")
//                    onResult(translated)
//                } ?: onError(Exception("Empty response"))
//            }
//        })
//    }
}